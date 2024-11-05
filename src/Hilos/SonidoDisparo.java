package Hilos;


import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SonidoDisparo implements Runnable
{

    public SonidoDisparo()
    {
        Thread shot = new Thread(this);
        shot.start();
    }

    
    public static void reproducirSonido(String archivo)
    {
        try (FileInputStream fis = new FileInputStream(archivo))
        {
            Player player = new Player(fis);
            player.play(); // Reproduce el archivo MP3 hasta que termine
        } catch (FileNotFoundException e)
        {
            System.err.println("Archivo no encontrado: " + e.getMessage());
        } catch (JavaLayerException e)
        {
            System.err.println("Error al reproducir el sonido: " + e.getMessage());
        } catch (IOException e)
        {
            System.err.println("Error de entrada/salida: " + e.getMessage());
        }
    }

    @Override
    public void run()
    {
        reproducirSonido("shot.mp3");
    }
    
}
