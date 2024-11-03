package Hilos;

import VentanaP.Ventana;
import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 *
 * @author Francisco
 */
public class Pato extends JLabel implements Runnable
{

    String ruta;
    String color;
    private Ventana v;
    private ImageIcon imagen;
    private boolean vivo = true; // Coordenada Y del cuadrado
    private boolean escapa = false; // Coordenada Y del cuadrado
    private boolean clicked; // Indica si el cuadrado fue clickeado
    Random random = new Random();
    Random random2 = new Random();
    public int x, y = random.nextInt(200 - 0 + 1);
    double mC; //Multiplicador de color
    public int dy; //Cambio en altura
    public int dx = dy = 1; //Direccion

    public Pato(String color, Ventana v)
    {
        ruta = "src\\imagenes\\Pato\\" + color + "\\";
        this.color = color;
        this.v = v;
        this.mC = (color.equals("negro") ? 1 : color.equals("azul") ? 0.75 : 0.5);
    }

    public boolean isVivo()
    {
        return vivo;
    }

    public void setVivo(boolean vivo)
    {
        this.vivo = vivo;
    }

    public boolean isEscapa()
    {
        return escapa;
    }

    public void setEscapa(boolean escapa)
    {
        this.escapa = escapa;
    }

    private void finalizar()
    {
        if (!vivo)
        {
            v.perroS("src\\imagenes\\Perro\\", getX());
        }
        v.hiloTerminado();
    }

    @Override
    public void run()
    {
        int i = 0;
        int j, j2 = 0;
        int k = y;
        int choques = 5;
        try
        {
            if (random2.nextInt(1000 - 0 + 1) % 2 == 0)
            {
                j = -60;
            } else
            {
                j = 500;
            }
            dy = (random2.nextInt(1000 - 0 + 1) % 2 == 0 ? 1 : -1);
            while (vivo && getY() > -10)
            {
                if ((choques > 0 || (getX() != 255 && getX() != 240)) && Ventana.balas > 0)
                {
                    imagen = new ImageIcon(ruta + (dx == 1 ? "right" : "left") + (dy == -1 ? "U" : dy == 1 ? "D" : "") + (i % 4 + 1) + ".png");
                    setIcon(imagen);
                    i++;
                    if (j > 470)
                    {
                        dx = -1;
                        choques -= escapa ? 1 : 0;
                    } else
                    {
                        if (j < 10)
                        {
                            dx = 1;
                            int nA = (random.nextInt(200 + 1));
                            choques -= escapa ? 1 : 0;
                            dy = nA % 3 == 0 ? 0 : dy == -1 ? -1 : 1;
                        }
                    }
                    if (k > 220)
                    {
                        dy = -1;
                    } else
                    {
                        if (k < 0)
                        {
                            dy = 1;
                        }
                    }
                    k += dy * 5;
                    j += dx * 5;
                    setBounds(j, k, imagen.getIconWidth(), imagen.getIconHeight());
                    x = getX();
                    y = getY();
                    Thread.sleep((int) (60 * mC));
                } else
                {
                    imagen = new ImageIcon(ruta + "\\away" + (i % 4 + 1) + ".png");
                    setIcon(imagen);
                    i++;
                    j2 += 5;
                    setBounds(x, y - j2, imagen.getIconWidth(), imagen.getIconHeight());
                    x = getX();
                    y = getY();
                    Thread.sleep(50);
                }
            }
            j = 0;
            while (j < 240 && !vivo)
            {
                imagen = new ImageIcon(ruta + "\\fall" + (i % 2 + 1) + ".png");
                setIcon(imagen);
                i++;
                j += 5;
                setBounds(x, y + j, imagen.getIconWidth(), imagen.getIconHeight());
                Thread.sleep(50);
            }
            Ventana.contador += (!vivo ? (color.equals("negro") ? 200 : color.equals("azul") ? 250 : 300) : 0);

        } catch (InterruptedException e)
        {
            Thread.currentThread().interrupt(); // Restaurar estado de interrupción
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            setIcon(null);
            Ventana.contHilos--; // Decrementa el contador cuando un pato termina

            finalizar();
        }
    }
}
