/**
 * 
 */
package lab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * @author user
 *
 */

import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


import data.Packet;
import data.PacketHandler;
 
public class Cliente extends Thread {
	private static ThreadedUDPClient client;
	
	public Cliente (){
		
	}
	
    public static void main(String[] args) {
 /*
        //puerto del servidor
        final int PUERTO_SERVIDOR = 5000;
        //buffer donde se almacenara los mensajes
        byte[] buffer = new byte[65536];
 
        try {
            //Obtengo la localizacion de localhost
            InetAddress direccionServidor = InetAddress.getByName("localhost");
 
            //Creo el socket de UDP
            DatagramSocket socketUDP = new DatagramSocket();
 
            String mensaje = "¡Hola mundo desde el cliente!";
 
            //Convierto el mensaje a bytes
            buffer = mensaje.getBytes();
 
            //Creo un datagrama
            DatagramPacket pregunta = new DatagramPacket(buffer, buffer.length, direccionServidor, PUERTO_SERVIDOR);
 
            //Lo envio con send
            System.out.println("Envio el datagrama");
            socketUDP.send(pregunta);
 
            //Preparo la respuesta
            DatagramPacket peticion = new DatagramPacket(buffer, buffer.length);
 
            //Recibo la respuesta
            socketUDP.receive(peticion);
            System.out.println("Recibo la peticion");
 
            //Cojo los datos y lo muestro
            mensaje = new String(peticion.getData());
            System.out.println(mensaje);
 
            //cierro el socket
            socketUDP.close();
 
        } catch (SocketException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
 */
    	client = new ThreadedUDPClient("localhost", 1337);
    	client.receive(new PacketHandler() {
    		
			@Override
			public void process(Packet packet) {
				
				String data = new String(packet.getData());
				System.out.println("Recibiendo: ");
				
				System.out.println(packet.getData());
				
				if(new String(packet.getData()).trim().equals("Fin")) {
					
					client.impr();
					
				}
			
			}
			
		});
		
		client.send("Preparado".getBytes());
		try {
			sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.send("Recibido".getBytes());
		
	
		
		

		// Starts writing the bytes in it 
		 
    }
 
}
