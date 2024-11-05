/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import VentanaP.Ventana;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Francisco
 */
public class Configuraciones
{
    public static void setFuenteEstilo8Bits(JLabel contador, int size, Ventana v)
    {
        try
        {
            // Cargar la fuente personalizada
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("press_start_2p\\PressStart2P.ttf"));
            font = font.deriveFont(Font.BOLD, size); // Ajusta el tamaño y el estilo

            // Aplicar la fuente al JLabel
            contador.setFont(font);
            v.repaint(); // Redibuja la ventana para aplicar los cambios

        } catch (FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
    }
    
    public static void mostrarCuadradoTemporal(JLayeredPane panel, int x, int y)
    {
        try
        {
            // Hacer la pantalla negra y dibujar el cuadrado blanco
            Graphics g = panel.getGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

            // Dibujar el cuadrado blanco en la posición del clic
            g.setColor(Color.WHITE);
            g.fillRect(x - 10, y - 10, 20, 20);

            // Pausar por 50 ms
            Thread.sleep(50);

            // Restaurar el panel (vuelve a pintar)
            panel.repaint();
        } catch (InterruptedException ex)
        {
            ex.printStackTrace();
        }
    }
    
    public static void fondo(String ruta, Object profundidad, JLayeredPane fondoP)
    {
        // Crear un panel
        JLayeredPane panel = new JLayeredPane()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                // Fondo negro predeterminado
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        // Cargar las imágenes
        ImageIcon imagen = new ImageIcon(ruta); // Imagen adicional

        // Crear etiquetas con las imágenes
        JLabel etiquetaConImagen = new JLabel(imagen);

        // Establecer posiciones y dimensiones de las etiquetas
        etiquetaConImagen.setBounds(0, 0, imagen.getIconWidth(), imagen.getIconHeight()); // Imagen secundaria

        fondoP.add(etiquetaConImagen, profundidad);  // Imagen secundaria
    }
    
}
