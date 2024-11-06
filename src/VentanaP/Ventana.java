package VentanaP;

import Controlador.Configuraciones;
import Hilos.Perro;
import javax.swing.*;
import java.awt.*;

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
    public Configuraciones c = new Configuraciones(this);
    
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
        Configuraciones.setFuenteEstilo8Bits(lContador, 12, this);
        lContador.setForeground(Color.WHITE);
        lContador.setBounds(250, 20, 240, 30);
        fondoP.add(lContador, JLayeredPane.POPUP_LAYER);

        Configuraciones.fondo("src\\imagenes\\world.png", JLayeredPane.MODAL_LAYER, fondoP);
        Configuraciones.fondo("src\\imagenes\\fondo.png", JLayeredPane.DEFAULT_LAYER, fondoP);
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
