package Hilos;

import Controlador.Configuraciones;
import VentanaP.Ventana;
import static VentanaP.Ventana.balas;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

/**
 *
 * @author Francisco
 */
public class Perro extends JLabel implements Runnable
{

    String ruta;
    private Ventana v;
    private ImageIcon imagen;
    public int nP;
    public int x;
    public int y;
    public boolean estado = false;

    public Perro(String ruta, Ventana v, int nP)
    {
        this.ruta = ruta;
        this.v = v;
        this.nP = nP;
    }

    public void entrada()
    {
        try
        {
            int i = 0;
            imagen = new ImageIcon(ruta + (i % 4 < 2 ? "dogright" : "nuzzle") + (i % 2 + 1) + ".png");
            setBounds(-10, 270, imagen.getIconWidth(), imagen.getIconHeight());

            while (true)
            {
                if (getX() < 200)
                {
                    imagen = new ImageIcon(ruta + (i % 4 < 2 ? "dogright" : "nuzzle") + (i % 2 + 1) + ".png");
                    setIcon(imagen);
                    setLocation(getX() + 5, getY());
                } else
                {
                    if (i < 56)
                    {
                        imagen = new ImageIcon(ruta + "dogjump1.png");
                        setIcon(imagen);
                        setLocation(getX(), getY() - 5);
                    } else
                    {
                        imagen = new ImageIcon(ruta + "dogjump2.png");
                        setIcon(imagen);
                        setLocation(getX(), getY() + 5);
                        if (getY() > 270)
                        {
                            break;
                        }
                    }
                }
                if (i == 56)
                {
                    v.getFondoP().setLayer(this, JLayeredPane.PALETTE_LAYER);
                }
                i++;
                Thread.sleep(50);
            }

            v.iniciarJ(nP);
            inicializarComponentesJuego();
            manejarClics();
        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt();
        }
        estado = true;
    }

    private void inicializarComponentesJuego()
    {
        ImageIcon imagenB = new ImageIcon("src\\imagenes\\bala azul.PNG");
        v.bala = new JLabel(imagenB);
        v.bala.setBounds(0, 340, imagenB.getIconWidth(), imagenB.getIconHeight());
        v.getFondoP().add(v.bala, JLayeredPane.DRAG_LAYER);

        v.lBContador = new JLabel("X" + balas);
        Configuraciones.setFuenteEstilo8Bits(v.lBContador, 22,v);
        v.lBContador.setForeground(Color.WHITE);
        v.lBContador.setBounds(30, 350, 240, 30);
        v.getFondoP().add(v.lBContador, JLayeredPane.POPUP_LAYER);
    }

    private void manejarClics()
    {
        if (balas != 0)
        {
            v.getFondoP().addMouseListener(new MouseAdapter()
            {
                @Override
                public void mouseClicked(MouseEvent e)
                {
                    Configuraciones.mostrarCuadradoTemporal(v.getFondoP(), e.getX(), e.getY());
                    balas -= balas != 0 ? 1 : 0;
                    v.lBContador.setText("X" + balas);
                    new SonidoDisparo();

                }
            });
        }
    }

    public void perroS()
    {
        int yP = 300;
        y = yP; 
        boolean sOb = true; 

        setIcon(new ImageIcon(ruta + "oneDuck.png"));

        while (true)
        {

            if (sOb)
            {
                y -= 5;
            } else
            {
                y += 5;
            }

            setLocation(x, y);

            if (y <= yP - 80)
            {
                sOb = false;
            }
            if (y >= yP)
            {
                break;
            }
            try
            {
                Thread.sleep(50);
            } catch (InterruptedException e)
            {
                System.out.println(e.toString());
            }
        }
    }

    @Override
    public void run()
    {
        entrada();
    }
}
