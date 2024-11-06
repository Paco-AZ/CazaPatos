/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Hilos.Pato;
import Hilos.SonidoDisparo;
import VentanaP.Ventana;
import static VentanaP.Ventana.balas;
import static VentanaP.Ventana.contHilos;
import static VentanaP.Ventana.contador;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Francisco
 */
public class Configuraciones
{
    public Pato[] pato;
    public Thread hPato[];
    public Ventana v;
    Random random = new Random();
    
    public Configuraciones (Ventana v)
    {
        this.v = v;
    }
    
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
    
    public void iniciarJ(int nP)
    {

        pato = new Pato[nP];
        hPato = new Thread[nP];
        for (int i = 0; i < nP; i++)
        {
            int nA = random.nextInt(200 - 0 + 1);
            pato[i] = new Pato((nA % 3 == 0 ? "negro" : nA % 3 == 1 ? "azul" : "rojo"), v); // Pasa la instancia de Ventana
            patos(pato[i], JLayeredPane.PALETTE_LAYER);
            hPato[i] = new Thread(pato[i]); // hilo Pato
            hPato[i].setDaemon(true); // Hace que el hilo pato sea un hilo demonio
            hPato[i].start();
            contHilos++; // Incrementa el contador de hilos
        }
    }

    public void patos(Pato etiquetaConImagen, Object profundidad)
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

        etiquetaConImagen.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                // Obtener las coordenadas donde se hizo clic
                int x = e.getX();
                int y = e.getY();
                // Iniciar hilo para cambiar el color de fondo y mostrar el cuadrado temporalmente
                new Thread(() -> Configuraciones.mostrarCuadradoTemporal(v.getFondoP(), x + 70 - etiquetaConImagen.getWidth() + etiquetaConImagen.x, y + 70 - etiquetaConImagen.getHeight() + etiquetaConImagen.y)).start();
                etiquetaConImagen.setVivo(false);
                v.lBContador.setText("X" + (--balas));
                new SonidoDisparo();
            }
        });
        v.getFondoP().add(etiquetaConImagen, profundidad);  // Imagen secundaria
    }

    public synchronized void hiloTerminado()
    {

        v.lContador.setText("Numero de patos: " + contador);
        //Comandos para que escapen los patos
        if (contHilos < 5)
        {
            for (int i = 0; i < pato.length; i++)
            {
                if (pato[i].isVivo() && !pato[i].isEscapa())
                {
                    pato[i].setEscapa(true);
                }
            }
            ImageIcon flyAI = new ImageIcon("src\\imagenes\\flyAway.png");
            JLabel flyA = new JLabel(flyAI);
            flyA.setBounds(320, 350, flyAI.getIconWidth(), flyAI.getIconHeight());
            v.getFondoP().add(flyA, JLayeredPane.DRAG_LAYER);
            v.getFondoP().repaint();
            v.add(v.getFondoP());
        }
        if (balas <= 0 && contHilos != 0)
        {
            ImageIcon gameOI = new ImageIcon("src\\imagenes\\gameOver.png");
            JLabel gameOver = new JLabel(gameOI);
            gameOver.setBounds(170, 100, gameOI.getIconWidth(), gameOI.getIconHeight());
            v.getFondoP().add(gameOver, JLayeredPane.DRAG_LAYER);
            v.getFondoP().repaint();
            v.add(v.getFondoP());
            if (contHilos != 0)
            {
                contHilos = 0;
                hiloTerminado();
            }
        }
        if (contHilos == 0)
        {
            v.lContador.setBounds(v.anc / 2 - v.lContador.getWidth(), v.alt / 2 - v.lContador.getHeight() - 50,
                    v.lContador.getWidth() * 3, v.lContador.getHeight() * 3);
            Configuraciones.setFuenteEstilo8Bits(v.lContador, 27, v);
            v.lContador.setForeground(Color.red);
            v.repaint(); // Redibuja la ventana para aplicar los cambios

            try
            {
                Thread.sleep(2000); // Pausa el hilo actual durante 5 segundos
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.exit(0); // Finaliza la aplicación
            v.dispose();
        }
    }
}
