package Modelo;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.IOException;
import java.io.File;

import LecturaPB.lectura; // delegamos en la clase lectura para crear los .dat

public class Backup {

	public static void main(String[] args) {
		// Ahora main no ejecuta automáticamente todas las operaciones.
		// Uso: java Modelo.Backup usuarios|workouts|both
		if (args == null || args.length == 0) {
			System.out.println("Uso: Backup [usuarios|workouts|both]");
			return;
		}

		int maxLevel = 3;
		for (String a : args) {
			switch (a.toLowerCase()) {
			case "usuarios":
				guardarUsuarios(new Usuario().mObtenerUsuarios());
				break;
			case "workouts":
				guardarWorkouts(new Workout().obtenerWorkouts((long) maxLevel));
				break;
			case "both":
				guardarUsuarios(new Usuario().mObtenerUsuarios());
				guardarWorkouts(new Workout().obtenerWorkouts((long) maxLevel));
				break;
			default:
				System.out.println("Argumento no válido: " + a);
			}
		}
	
	
HiloBackup hb = new HiloBackup();
	hb.start();
	}

	// Métodos auxiliares simples que delegan en LecturaPB.lectura
	public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		lectura.guardarUsuarios(usuarios);
	}

	public static void guardarWorkouts(ArrayList<Workout> workouts) {
		lectura.guardarWorkouts(workouts);
	}

}