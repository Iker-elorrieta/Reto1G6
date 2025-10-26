package Modelo;

import java.io.IOException;

public class HiloBackup extends Thread {

	
	
	public HiloBackup() {
	}
	
	


	public void run() {
		// Para llamar la clase lectura y guardar los datos en los archivos .dat
		try {
			// Ejecutamos la clase LecturaPB.lectura desde el directorio bin
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "backups.jar");

			// Redirigimos la salida para ver mensajes en la consola del padre (opcional y sencillo)
			pb.inheritIO();
			Process proces = pb.start();
			try {
				int exit = proces.waitFor();
				System.out.println("Proceso Lectura finalizado con código: " + exit);
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				System.out.println("Espera del proceso interrumpida");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
	
	
}
