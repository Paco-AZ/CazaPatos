package VentanaP;

import Hilos.Pato;
import Hilos.Perro;
import Hilos.SonidoDisparo;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Francisco
 */
public class Ventana extends JFrame
{

    Dimension tamP = new Dimension(512, 450);
    public int alt = tamP.height;
    private JLabel animacionLabel;
    public static int contador = 0;
    public static int balas;
    public JLabel bala;
    public JLabel lContador;
    public JLabel lBContador;
    public int anc = tamP.width;
    private JLayeredPane fondoP;
    public Perro perro;
    public Pato[] pato;
    public Thread hPato[];
    Random random = new Random();
    public static int contHilos; // contador de hilos activos

    public Ventana(int numPatos)
    {
        balas = (int) (numPatos * 1.5) + 1;
        setTitle("Caza patos");
        setSize(anc, alt);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        fondoP = new JLayeredPane();
        fondoP.setPreferredSize(tamP);
        lContador = new JLabel("Numero de patos: " + numPatos);
        setFuenteEstilo8Bits(lContador, 12);
        lContador.setForeground(Color.WHITE);
        lContador.setBounds(250, 20, 240, 30);
        fondoP.add(lContador, JLayeredPane.POPUP_LAYER);

        fondo("src\\imagenes\\world.png", JLayeredPane.MODAL_LAYER);
        fondo("src\\imagenes\\fondo.png", JLayeredPane.DEFAULT_LAYER);
//        iniciarAnimacion(numPatos);
        perro = new Perro("src/imagenes/Perro/", this, numPatos);
        Thread a = new Thread(perro);
        fondoP.add(perro, JLayeredPane.POPUP_LAYER);
        a.start();
        
        this.add(fondoP);
        this.setVisible(true);
    }

    public JLayeredPane getFondoP()
    {
        return fondoP;
    }

    public void setFondoP(JLayeredPane fondoP)
    {
        this.fondoP = fondoP;
    }

    public void fondo(String ruta, Object profundidad)
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
        etiquetaConImagen.setBounds(0, 0, anc, alt); // Imagen secundaria

        fondoP.add(etiquetaConImagen, profundidad);  // Imagen secundaria
    }

    

    public void iniciarJ(int nP)
    {

        pato = new Pato[nP];
        hPato = new Thread[nP];
        for (int i = 0; i < nP; i++)
        {
            int nA = random.nextInt(200 - 0 + 1);
            pato[i] = new Pato((nA % 3 == 0 ? "negro" : nA % 3 == 1 ? "azul" : "rojo"), this); // Pasa la instancia de Ventana
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
                new Thread(() -> mostrarCuadradoTemporal(fondoP, x + 70 - etiquetaConImagen.getWidth() + etiquetaConImagen.x, y + 70 - etiquetaConImagen.getHeight() + etiquetaConImagen.y)).start();
                etiquetaConImagen.setVivo(false);
                lBContador.setText("X" + (--balas));
                new SonidoDisparo();
            }
        });
        fondoP.add(etiquetaConImagen, profundidad);  // Imagen secundaria
    }

    public void mostrarCuadradoTemporal(JLayeredPane panel, int x, int y)
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

    public synchronized void hiloTerminado()
    {

        lContador.setText("Numero de patos: " + contador);
        //Comandos para que escapen los patos
//        if (contHilos < 5)
//        {
//            for (int i = 0; i < pato.length; i++)
//            {
//                if (pato[i].isVivo() && !pato[i].isEscapa())
//                {
//                    pato[i].setEscapa(true);
//                }
//            }
//            ImageIcon flyAI = new ImageIcon("src\\imagenes\\flyAway.png");
//            JLabel flyA = new JLabel(flyAI);
//            flyA.setBounds(320, 350, flyAI.getIconWidth(), flyAI.getIconHeight());
//            fondoP.add(flyA, JLayeredPane.DRAG_LAYER);
//            fondoP.repaint();
//            add(fondoP);
//        }
        if (balas <= 0 && contHilos != 0)
        {
            ImageIcon gameOI = new ImageIcon("src\\imagenes\\gameOver.png");
            JLabel gameOver = new JLabel(gameOI);
            gameOver.setBounds(170, 100, gameOI.getIconWidth(), gameOI.getIconHeight());
            fondoP.add(gameOver, JLayeredPane.DRAG_LAYER);
            fondoP.repaint();
            add(fondoP);
            if (contHilos != 0)
            {
                contHilos = 0;
                hiloTerminado();
            }
        }
        if (contHilos == 0)
        {
            lContador.setBounds(anc / 2 - lContador.getWidth(), alt / 2 - lContador.getHeight() - 50,
                    lContador.getWidth() * 3, lContador.getHeight() * 3);
            setFuenteEstilo8Bits(lContador, 27);
            lContador.setForeground(Color.red);
            repaint(); // Redibuja la ventana para aplicar los cambios

            try
            {
                Thread.sleep(2000); // Pausa el hilo actual durante 5 segundos
            } catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            System.exit(0); // Finaliza la aplicación
            dispose();
        }
    }

    public void setFuenteEstilo8Bits(JLabel contador, int size)
    {
        try
        {
            // Cargar la fuente personalizada
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("press_start_2p\\PressStart2P.ttf"));
            font = font.deriveFont(Font.BOLD, size); // Ajusta el tamaño y el estilo

            // Aplicar la fuente al JLabel
            contador.setFont(font);
            repaint(); // Redibuja la ventana para aplicar los cambios

        } catch (FontFormatException | IOException e)
        {
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {

        do
        {
            try
            {
                String oP = (JOptionPane.showInputDialog(null,
                        "Ingrese el numero de patos", "ingrese Patos",
                        JOptionPane.QUESTION_MESSAGE));
                if (oP == null)
                {
                    JOptionPane.showMessageDialog(null, "Se cancelo la ejecucion", "Cerrando", JOptionPane.CLOSED_OPTION);
                    return;
                } else
                {
                    if (oP.trim().isEmpty())
                    {
                        JOptionPane.showMessageDialog(null, "No se ingreso ningun valor", "Advertencia", JOptionPane.WARNING_MESSAGE);
                        continue;
                    }
                }
                int nP = Integer.parseInt(oP);
                if (nP > 0)
                {
                    Ventana m = new Ventana(nP);
                    m.setVisible(true);
                    break;
                } else
                {
                    JOptionPane.showMessageDialog(null, "El valor debe ser mayor a 0", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e)
            {
                JOptionPane.showMessageDialog(null, "Entrada no valida, Ingrese una cantidad positiva", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        } while (true);
    }
}
