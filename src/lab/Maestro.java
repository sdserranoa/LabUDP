/**
 * 
 */
package lab;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import data.Packet;
import data.PacketHandler;





/**
 * @author user
 *
 */
public class Maestro {

	private static ServerSocket ss;	
	private static final String MAESTRO = "MAESTRO: ";
	private static final int numeroThreads = 25;
	private static int cont=1;
	static Object o=new Object();
	static Object p=new Object();
	public static String log = "";
	public static String ruta = "./resultados.txt";
	private static ThreadedUDPServer server;

	/**
	 * @param args
	 */
	public static void main(String[] args)throws Exception {
		// TODO Auto-generated method stub
/*
		System.out.println(MAESTRO + "Establezca puerto de conexion:");
		InputStreamReader isr = new InputStreamReader(System.in);
		BufferedReader br = new BufferedReader(isr);
		int ip = Integer.parseInt(br.readLine());
		System.out.println(MAESTRO + "Empezando servidor maestro en puerto " + ip);

		File file = null;
		String ruta = "./resultados.txt";

		file = new File(ruta);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter fw = new FileWriter(file);
		fw.close();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
		Date date = new Date();  
		log+="Fecha: "+formatter.format(date)+"\r\n";
		ExecutorService executor = Executors.newFixedThreadPool(numeroThreads);

		ss = new ServerSocket(ip);
		System.out.println(MAESTRO + "Socket creado.");

		byte[] bytesArray;

		System.out.println("Elija un archivo para enviar (1 o 2):");
		System.out.println("1.Archivo de texto 250MB");
		System.out.println("2.Archivo de texto 100MB");
		String send="";

		if(br.readLine().equals("1")) {

			send="./data/Archivo1.txt";
			log+="Archivo Enviado: archivo1 \r\n";
		}else {
			send="./data/Archivo2.txt";
			log+="Archivo Enviado: archivo2 \r\n";
		}

		File sendFile = new File(send);
		bytesArray = new byte[(int) sendFile.length()]; 
		FileInputStream fis = new FileInputStream(sendFile);
		BufferedInputStream bis = new BufferedInputStream(fis);
		bis.read(bytesArray,0,bytesArray.length);
		System.out.println("¿A cuantos clientes desea enviar el archivo?");
		String clientes=br.readLine();
		ArrayList<Cliente> clients=new ArrayList<Cliente>();
		try {
			FileWriter fw1 = new FileWriter(new File(ruta),true);
			fw1.write(log + "\r");
			fw1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		for (int i=0;true;i++) {

			if(cont>=Integer.parseInt(clientes)) {
				synchronized(p) {
					p.wait();	
				}
				synchronized(o) {
					o.notifyAll();
				}

			}
			try { 
				log="";
				Socket sc = ss.accept();
				System.out.println(MAESTRO + "Cliente " + i + " aceptado.");
				log+=MAESTRO + "Cliente " + i + " aceptado.";
				try {
					FileWriter fw1 = new FileWriter(new File(ruta),true);
					fw1.write(log + "\r");
					fw1.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
				Cliente d = new Cliente(sc,i,bytesArray);
				clients.add(d);
				executor.execute(d);
			} catch (IOException e) {
				System.out.println(MAESTRO + "Error creando el socket cliente.");
				e.printStackTrace();
			}

		}

*/

		server= new ThreadedUDPServer(1337);
		server.receive(new PacketHandler() {

			@Override
			public void process(Packet packet) {
				String data = new String(packet.getData()).trim();
		
				if(data.equals("Preparado")) {
					ThreadedUDPServer.CLIENTS.add(packet.getConnection());
					server.send(new Packet("OK".getBytes(), packet.getAddr(), packet.getPort()));
					System.out.println("Recibiendo: ");
					System.out.println(new String(packet.getData()).trim());
					 data = new String(packet.getData()).trim();
					 System.out.println("Elija un archivo para enviar (1 o 2):");
						System.out.println("1.Archivo de texto 250MB");
						System.out.println("2.Archivo de texto 100MB");
					 BufferedReader reader =
			                   new BufferedReader(new InputStreamReader(System.in));
			        String archivo;
					try {
						archivo = reader.readLine();
						String send="";

						if(archivo.equals("1")) {

							send="./data/Archivo1.txt";
							log+="Archivo Enviado: archivo1 \r\n";
						}else {
							send="./data/Archivo2.txt";
							log+="Archivo Enviado: archivo2 \r\n";
						}

						File sendFile = new File(send); 
						byte[] bytesArray = new byte[(int) sendFile.length()]; 
						System.out.println(bytesArray.length);
					
						for(int i=0;i<bytesArray.length;i+=1024) {
							byte[] temp=Arrays.copyOfRange(bytesArray, i, i+1024);
						
							server.send(new Packet(temp, packet.getAddr(), packet.getPort()));
						}
						
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				if(data.equals("Recibido")) {
					server.send(new Packet("Fin".getBytes(), packet.getAddr(), packet.getPort()));
					System.out.println(new String(packet.getData()).trim());
					 data = new String(packet.getData()).trim();
					 
				}
			}
			
		});
	
	}

	public static void reply(Packet packet) {
		server.broadcast(new String(packet.toString()).getBytes());
	}
	public static void dormir() {
		try {
			synchronized(o) {
				synchronized(p) {

					p.notify();
				}
				cont++;
				o.wait();
			}


		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static synchronized void log(long t1,long t2) {
		try {
			log="Tiempo de envío: "+(t2-t1+"\r\n");
			log+="Archivo recibido exitosamente \r\n";
			FileWriter fw1 = new FileWriter(new File(ruta),true);
			fw1.write(log + "\r");
			fw1.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
