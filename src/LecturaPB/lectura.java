package LecturaPB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import Modelo.Usuario;
import Modelo.Workout;

public class lectura {

	private static final String FILE_USERS = "backups/usuario.dat";
	private static final String FILE_WORKOUTS = "backups/workouts.dat";

	public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_USERS))) {
				for (Usuario usu : usuarios) {
					oos.writeObject(usu);
				}
				System.out.println("Colección de usuarios guardada correctamente.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void guardarWorkouts(ArrayList<Workout> workouts) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_WORKOUTS))) {
				for (Workout wot : workouts) {
					oos.writeObject(wot);
				}
				System.out.println("Colección de workouts/ejercicios guardada correctamente.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
