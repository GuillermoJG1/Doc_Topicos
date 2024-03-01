/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Tareas;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Clase principal de la aplicación

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RegistroIMCApp {
    private JFrame frame;//Introduccion del JFrame
    private JTextField nombreField, edadField, pesoField, alturaField, imcField;//Ingreso de variables de entrada para los recuadros
    private JTextArea registrosTextArea;//registro de textos entre cada recuadro 
    private List<RegistroIMC> registros;//lista que alacenara los registros una vez almacenados
    private int indiceActual;

    // Constructor de la aplicación
    public RegistroIMCApp() {
        // Configuración de la interfaz de usuario
        frame = new JFrame("Registro de IMC");//Titlo del frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//Linea que termine el programa una vez termine de ejecutarse (que se cierre)
        frame.setSize(400, 300);//Tamaño de la pestaña del panel el cual se utlizara 
//        frame.setLayout(null); +++++++++++++++++++++++++++++++++++++++++++

        // Inicialización de la lista de registros y el índice actual
        registros = new ArrayList<>();//guardaddo de los registos en el array list 
        indiceActual = -1;

        // Creación de la interfaz de usuario
        createUI();
    }

    // Método para crear la interfaz de usuario
    private void createUI() {
        // Creación del panel principal con una disposición de cuadrícula
        JPanel panel = new JPanel(new GridLayout(7, 2));//permite el acomodo de los paneles  de tal manera que estas son acomodados por filas y columnas 

        // Creación de etiquetas y campos de texto
        JLabel[] labels = {
            new JLabel("Nombre:"),//Creacion de etiqueta de Nombre
            new JLabel("Edad:"),//Creacion de etiqueta  de edad
            new JLabel("Peso (kg):"),//Creacion de etiqueta de peso
            new JLabel("Altura (m):"),//Creacion de etiqueta de altura
            new JLabel("IMC:")//Creacion de etiqueta de texto de IMC
        };
        JTextField[] fields = {
            nombreField = new JTextField(),//Creador de campo de texto de nombre 
            edadField = new JTextField(),//Creacion de campo de texto de edad
            pesoField = new JTextField(),//Creacion de campo de texto de peso
            alturaField = new JTextField(),//Creacion de campo de texto de altura
            imcField = new JTextField()//Creacion de campo de texto de IMC
        };

        // Configuración del campo IMC como no editable
        imcField.setEditable(false);//Aqui configuramos el campo de IMC como no editable

        // Creación de botones con nombres y manejadores de eventos asociados
        JButton[] buttons = {
            new JButton("Calcular IMC"),
            new JButton("Nuevo Registro"),
            new JButton("Guardar Registro Actual"),
            new JButton("Guardar en Archivo"),
            new JButton("Siguiente Registro"),
            new JButton("Registro Anterior")
        };
        ActionListener[] buttonListeners = {
            e -> calcularIMC(),//se le agrea la accion al boton calcular 
            e -> nuevoRegistro(),
            e -> guardarRegistro(),
            e -> guardarEnArchivo(),
            e -> siguienteRegistro(),
            e -> registroAnterior()
        };

        // Asignación de manejadores de eventos a los botones
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(buttonListeners[i]);
        }

        // Configuración del área de texto
        registrosTextArea = new JTextArea();
        registrosTextArea.setEditable(false);

        // Adición de componentes al panel
        for (int i = 0; i < labels.length; i++) {
            panel.add(labels[i]);
            panel.add(fields[i]);
        }
        for (JButton button : buttons) {
            panel.add(button);
        }

        // Adición de componentes al contenedor principal
        frame.add(panel, BorderLayout.CENTER);
        frame.add(registrosTextArea, BorderLayout.SOUTH);

        // Hacer visible la ventana
        frame.setVisible(true);
    }

    // Método para calcular el IMC
    private void calcularIMC() {
        try {
            double peso = Double.parseDouble(pesoField.getText());
            double altura = Double.parseDouble(alturaField.getText());

            double imc = peso / (altura * altura);

            imcField.setText(String.format("%.2f", imc));
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese valores válidos para peso y altura.");
        }
    }

    // Método para crear un nuevo registro
    private void nuevoRegistro() {
        limpiarCampos();
        indiceActual = -1;
    }

    // Método para guardar el registro actual
    private void guardarRegistro() {
        try {
            String nombre = nombreField.getText();
            int edad = Integer.parseInt(edadField.getText());
            double peso = Double.parseDouble(pesoField.getText());
            double altura = Double.parseDouble(alturaField.getText());
            double imc = Double.parseDouble(imcField.getText());

            RegistroIMC registro = new RegistroIMC(nombre, edad, peso, altura, imc);

            if (indiceActual == -1) {
                registros.add(registro);
                indiceActual = registros.size() - 1;
            } else {
                registros.set(indiceActual, registro);
            }

            actualizarTextArea();
        } catch (NumberFormatException e) {
            mostrarMensajeError("Ingrese valores válidos para todos los campos.");
        }
    }

    // Método para actualizar el área de texto con la información de los registros
    private void actualizarTextArea() {
        registrosTextArea.setText("");
        registros.forEach(registro -> registrosTextArea.append(registro + "\n"));
    }

    // Método para guardar todos los registros en un archivo de texto
    private void guardarEnArchivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("registros.txt"))) {
            for (RegistroIMC registro : registros) {
                writer.write(registro.toString());
                writer.newLine();
            }
            JOptionPane.showMessageDialog(frame, "Registros guardados en el archivo 'registros.txt'.", "Guardado", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            mostrarMensajeError("Error al guardar en el archivo.");
        }
    }

    // Método para avanzar al siguiente registro
    private void siguienteRegistro() {
        if (indiceActual < registros.size() - 1) {
            indiceActual++;
            cargarRegistro();
        }
    }

    // Método para retroceder al registro anterior
    private void registroAnterior() {
        if (indiceActual > 0) {
            indiceActual--;
            cargarRegistro();
        }
    }

    // Método para cargar la información del registro actual en los campos de texto
    private void cargarRegistro() {
        RegistroIMC registro = registros.get(indiceActual);
        nombreField.setText(registro.getNombre());
        edadField.setText(String.valueOf(registro.getEdad()));
        pesoField.setText(String.valueOf(registro.getPeso()));
        alturaField.setText(String.valueOf(registro.getAltura()));
        imcField.setText(String.valueOf(registro.getImc()));
    }

    // Método para limpiar los campos de entrada
    private void limpiarCampos() {
        for (JTextField field : new JTextField[]{nombreField, edadField, pesoField, alturaField, imcField}) {
            field.setText("");
        }
    }

    // Método para mostrar mensajes de error
    private void mostrarMensajeError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // Clase principal que inicia la aplicación
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistroIMCApp::new);
    }
}

// Clase que representa un registro de IMC
class RegistroIMC {
    private String nombre;
    private int edad;
    private double peso;
    private double altura;
    private double imc;

    // Constructor de la clase RegistroIMC
    public RegistroIMC(String nombre, int edad, double peso, double altura, double imc) {
        this.nombre = nombre;
        this.edad = edad;
        this.peso = peso;
        this.altura = altura;
        this.imc = imc;
    }

    // Métodos de acceso para obtener los valores de los atributos
    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public double getPeso() {
        return peso;
    }

    public double getAltura() {
        return altura;
    }

    public double getImc() {
        return imc;
    }

    // Método toString para representar el registro como una cadena de texto
    @Override
    public String toString() {
        return "Nombre: " + nombre + ", Edad: " + edad + ", Peso: " + peso + ", Altura: " + altura + ", IMC: " + imc;
    }
}
