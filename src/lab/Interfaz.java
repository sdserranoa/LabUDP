package lab;

import java.util.Scanner;

public class Interfaz extends Thread{
	Maestro maestro;
	public Interfaz() {
		this.maestro=maestro;
	}
public void run() {
	 System.out.println("Elija un archivo para enviar (1 o 2):");
		System.out.println("1.Archivo de texto 250MB");
		System.out.println("2.Archivo de texto 100MB");
		 Scanner in = new Scanner(System.in);
	       
 String archivo;
 
	try {
		archivo = in.nextLine();
		

		if(archivo.equals("1")) {

			maestro.send="./data/Archivo1.txt";
			
		}else {
			maestro.send="./data/Archivo2.txt";
			
		}
	
	}catch(Exception e) {
		
	}
	
}
}
