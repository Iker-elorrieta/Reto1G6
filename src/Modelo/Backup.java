package Modelo;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.io.IOException;

public class Backup {

	public static void main(String[] args) {
		int maxLevel = 3;
		
		guardarUsuarios(new Usuario().mObtenerUsuarios());
		guardarWorkouts(new Workout().obtenerWorkouts((long) maxLevel));
	}

	private static final String FILE_USERS = "backups/usuario.dat";
	private static final String FILE_WORKOUTS = "backups/workouts.dat";

	public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_USERS))) {
			for (Usuario usu : usuarios) {
				oos.writeObject(usu);
			}
			System.out.println("Colección de usuarios guardada correctamente.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void guardarWorkouts(ArrayList<Workout> workouts) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_WORKOUTS))) {
			for (Workout wot : workouts) {
				oos.writeObject(wot);
			}
			System.out.println("Colección de workouts/ejercicios guardada correctamente.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
