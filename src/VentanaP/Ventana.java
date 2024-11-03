package VentanaP;

import Hilos.Pato;
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
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Francisco
 */
public class Ventana extends JFrame
{

    Dimension tamP = new Dimension(512, 450);
    public int alt = tamP.height;
    private JLabel animacionLabel;
    private JLabel perro;
    public static int contador = 0;
    public static int balas;
    public JLabel bala;
    public JLabel lContador;
    public JLabel lBContador;
    public int anc = tamP.width;
    private JLayeredPane fondoP;
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
        lContador = new JLabel("Puntuacion: 0");
        setFuenteEstilo8Bits(lContador, 12);
        lContador.setForeground(Color.WHITE);
        lContador.setBounds(280, 20, 240, 30);
        fondoP.add(lContador, JLayeredPane.POPUP_LAYER);

        fondo("src\\imagenes\\world.png", JLayeredPane.MODAL_LAYER);
        fondo("src\\imagenes\\fondo.png", JLayeredPane.DEFAULT_LAYER);
//        iniciarAnimacion(numPatos);
        perro("src\\imagenes\\Perro\\dog", JLayeredPane.POPUP_LAYER, numPatos);
        this.add(fondoP);
        this.setVisible(true);
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

    public void perro(String ruta, Object profundidad, int np)
    {
        JLayeredPane panel = new JLayeredPane()
        {
            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        ImageIcon imagen = new ImageIcon(ruta + "right" + 1 + ".png");
        perro = new JLabel(imagen);
        perro.setBounds(-10, 270, imagen.getIconWidth(), imagen.getIconHeight());
        fondoP.add(perro, profundidad);
        int delay = 50;
        Timer timer = new Timer(delay, new ActionListener()
        {
            int x = 1;
            int y = 1;
            int z = 1;

            @Override
            public void actionPerformed(ActionEvent e)
            {
                perro.setIcon(new ImageIcon(ruta + "right" + (x % 4 + 1) + ".png"));
                perro.setLocation(x * 5, perro.getY());

                if (perro.getX() >= 200)
                {
                    perro.setIcon(new ImageIcon(ruta + "jump" + (x % 2 + 1) + ".png"));
                    perro.setLocation(perro.getX(), perro.getY() - (z == 1 ? y : 0) * 5);
                    if (perro.getY() < 200 || z > 1)
                    {
                        fondoP.setLayer(perro, JLayeredPane.PALETTE_LAYER);
                        perro.setIcon(new ImageIcon(ruta + "jump" + (x % 2 + 1) + ".png"));
                        perro.setLocation(perro.getX(), perro.getY() + z * 5);
                        if (perro.getY() > 290)
                        {
                            ((Timer) e.getSource()).stop();
                            perro.setIcon(new ImageIcon(ruta + "laught" + (x % 2 + 1) + ".png"));
                            iniciarJ(np);
                            ImageIcon imagenB = new ImageIcon("src\\imagenes\\bala azul.PNG");
                            bala = new JLabel(imagenB);
                            bala.setBounds(0, 340, imagenB.getIconWidth(), imagenB.getIconHeight());
                            fondoP.add(bala, JLayeredPane.DRAG_LAYER);
                            lBContador = new JLabel("X" + balas);
                            setFuenteEstilo8Bits(lBContador, 22);
                            lBContador.setForeground(Color.WHITE);
                            lBContador.setBounds(30, 350, 240, 30);
                            fondoP.add(lBContador, JLayeredPane.POPUP_LAYER);
                            if (balas != 0)
                            {
                                fondoP.addMouseListener(new MouseAdapter()
                                {

                                    @Override
                                    public void mouseClicked(MouseEvent e)
                                    {

                                        // Si se clickea en el fondo
                                        mostrarCuadradoTemporal(fondoP, e.getX(), e.getY());
                                        balas -= balas != 0 ? 1 : 0;
                                        lBContador.setText("X" + balas);
                                        Thread shot = new Thread(() -> sonidoD());
                                        shot.start();

                                    }
                                });
                            }
                        } else
                        {
                            z++;
                        }
                    } else
                    {
                        if (z == 1)
                        {
                            y++;
                        }
                    }
                } else
                {
                    x++;
                }
            }
        });
        timer.start();
    }

    public void perroS(String ruta, int xP)
    {
        int yP = 300;
        int delay = 50;

        Timer timer = new Timer(delay, new ActionListener()
        {
            int y = yP; // Iniciar en la posición Y del pato caído
            boolean sOb = true; // Bandera para controlar el movimiento

            @Override
            public void actionPerformed(ActionEvent e)
            {
                perro.setIcon(new ImageIcon(ruta + "oneDuck.png"));

                if (sOb)
                {
                    y -= 5;
                } else
                {
                    y += 5;
                }
                perro.setLocation(xP, y);
                if (y <= yP - 80)
                {
                    sOb = false;
                }
                if (y >= yP)
                {
                    ((Timer) e.getSource()).stop();
                }
            }
        });

        // Iniciar el temporizador
        timer.start();
    }

    public void iniciarJ(int nP)
    {

        pato = new Pato[nP];
        hPato = new Thread[nP];
        for (int i = 0; i < nP; i++)
        {
            int nA = random.nextInt(200 - 0 + 1);
            pato[i] = new Pato((nA % 3 == 0 ? "negro" : nA % 3 == 1 ? "azul" : "rojo"), this); // Pasa la instancia de Ventana
            Hilos(pato[i], JLayeredPane.PALETTE_LAYER);
            hPato[i] = new Thread(pato[i]); // hilo Pato
            hPato[i].setDaemon(true); // Hace que el hilo pato sea un hilo demonio
            hPato[i].start();
            contHilos++; // Incrementa el contador de hilos
        }
    }

    public void Hilos(Pato etiquetaConImagen, Object profundidad)
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
                Thread shot = new Thread(() -> sonidoD());
                shot.start();
            }
        });
        fondoP.add(etiquetaConImagen, profundidad);  // Imagen secundaria
    }

    private static void sonidoD()
    {
        SonidoDisparo a = new SonidoDisparo();
        Thread b = new Thread(a);
        b.start();
    }

    private void mostrarCuadradoTemporal(JLayeredPane panel, int x, int y)
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

        lContador.setText("Puntuacion: " + contador);
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
            fondoP.add(flyA, JLayeredPane.DRAG_LAYER);
            fondoP.repaint();
            add(fondoP);
        }
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
        int nP = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese el numero de patos", "ingrese Patos", JOptionPane.QUESTION_MESSAGE));
        Ventana m = new Ventana(nP);
        m.setVisible(true);
    }
}
