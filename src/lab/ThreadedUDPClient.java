package lab;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;

import data.Connection;
import data.Packet;
import data.PacketHandler;

/**
 * A class for handling a multi-threaded instance of a UDP client
 * @author Chiranjeevi Ramamurth
 *
 */
public class ThreadedUDPClient implements Runnable {
	static ArrayList<Object> arr=new ArrayList<>();
	private Connection connection;
	private boolean running;
	
	private DatagramSocket socket;
	private Thread process, send, receive;
	
	public ThreadedUDPClient(String addr, int port) {
		try {
			socket = new DatagramSocket();
			connection = new Connection(socket, InetAddress.getByName(addr), port, 0);
			this.init();
		} catch (SocketException | UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Initialize the client
	 */
	private void init() {
		process = new Thread(this, "server_process");
		process.start();
	}
	
	/**
	 * Send some data
	 * @param the data
	 */
	public void send(final byte[] data) {
		send = new Thread("Sending Thread") {
			public void run() {
				connection.send(data);
			}
		};
		
		send.start();
	}
	
	/**
	 * Receive data on the given server connection
	 */
	public void receive(final PacketHandler handler) {
		receive = new Thread("receive_thread") {
			public void run() {
				while(running) {
					byte[] buffer = new byte[1024];
					DatagramPacket dgpacket = new DatagramPacket(buffer, buffer.length);
					
					try {
						socket.receive(dgpacket);
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println("Numero de paquetes recibidos: "+ dgpacket.getLength());
					System.out.println(dgpacket.getData());
					arr.add(dgpacket.getData());
					handler.process(new Packet(dgpacket.getData(), dgpacket.getAddress(), dgpacket.getPort()));
				}
			}
		};
		
		receive.start();
	}
	
	/**
	 * Close the current connection for this client
	 */
	public void close() {
		connection.close();
		running = false;
	}
	public void impr() {
		File file = new File("./data/respuesta.txt");
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			byte[] valorClaro=new byte[1];
			
			for(int i=0;i<arr.size()-1;i++) {
				
				valorClaro=concat(valorClaro,(byte[])arr.get(i));
				
				
			}
			os.write(valorClaro);
			os.flush();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	public static  byte[] concat(byte[] first, byte[] second) {
		System.out.println("1: "+first);
		System.out.println("2: "+second);
		  byte[] result = Arrays.copyOf(first, first.length + second.length);
		  System.arraycopy(second, 0, result, first.length, second.length);
		  System.out.println("3: "+result);
		  return result;
		}
	@Override
	public void run() {
		running = true;
	}

}
