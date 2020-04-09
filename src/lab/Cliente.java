/**
 * 
 */
package lab;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
import org.bouncycastle.util.encoders.Hex;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import data.Packet;
import data.PacketHandler;



public class Cliente extends Thread {
	private static ThreadedUDPClient client;

	public Cliente (){

	}

	public static void main(String[] args) {


		client = new ThreadedUDPClient("localhost", 1337);
		client.receive(new PacketHandler() {

			@Override
			public void process(Packet packet) {

				String data = new String(packet.getData());
				System.out.println("Recibiendo: ");

				System.out.println(packet.getData());
				if(new String(packet.getData()).trim().equals("HASH"))
				{
					String fromServerH = new String(packet.getData()).trim();
					try {MessageDigest digest;
					digest = MessageDigest.getInstance("SHA-256");

					byte[] hash = digest.digest(client.compararHash());
					String sha256hex = new String(Hex.encode(hash));
					System.out.println("El hash generado es: "+sha256hex);

					if(sha256hex.equals(fromServerH)){
					}
					} catch (NoSuchAlgorithmException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(new String(packet.getData()).trim().equals("Fin")) 
				{
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
