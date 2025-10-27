package Modelo;

import java.io.IOException;

import LecturaPB.lectura;

public class HiloBackup extends Thread {

	
	
	public HiloBackup() {
	}
	
	


	public void run() {
		// Para llamar la clase lectura y guardar los datos en los archivos .dat
		try {
			ProcessBuilder pb = new ProcessBuilder("java", "-jar", "backups.jar");

			// Redirigimos la salida para ver mensajes en la consola 
			pb.inheritIO();
			Process proces = pb.start();
			try {
				int exit = proces.waitFor();
				System.out.println("Proceso Lectura finalizado con código: " + exit);

				// También generar el XML del histórico de workouts localmente
				try {
					lectura.guardarHistoricoWorkoutsXML();
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (InterruptedException ie) {
				Thread.currentThread().interrupt();
				System.out.println("Espera del proceso interrumpida");
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	
	
	
	
}