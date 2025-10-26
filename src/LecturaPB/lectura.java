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

	private static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			File f = new File(FILE_USERS);
			if (!f.exists()) f.createNewFile();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				if (usuarios != null) {
					for (Usuario usu : usuarios) {
						oos.writeObject(usu);
					}
				}
				System.out.println("guardarUsuarios: escrito en " + f.getAbsolutePath());
			}
		} catch (IOException e) {
			System.err.println("guardarUsuarios: error al escribir fichero: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static void guardarWorkouts(ArrayList<Workout> workouts) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			File f = new File(FILE_WORKOUTS);
			if (!f.exists()) f.createNewFile();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				if (workouts != null) {
					for (Workout wot : workouts) {
						oos.writeObject(wot);
					}
				}
				System.out.println("guardarWorkouts: escrito en " + f.getAbsolutePath());
			}
		} catch (IOException e) {
			System.err.println("guardarWorkouts: error al escribir fichero: " + e.getMessage());
			e.printStackTrace();
		}
	}

	
	private static void generarBackupsDesdeServidor() {
		ArrayList<Usuario> usuarios = null;
		ArrayList<Workout> workouts = null;
		int maxLevel = 3;
		try {
			usuarios = new Usuario().mObtenerUsuarios();
			System.out.println("generarBackupsDesdeServidor: usuarios obtenidos: " + (usuarios != null ? usuarios.size() : 0));
		} catch (Exception e) {
			System.err.println("generarBackupsDesdeServidor: no se pudieron obtener usuarios: " + e.getMessage());
			usuarios = new ArrayList<>();
		}
		try {
			workouts = new Workout().obtenerWorkouts((long) maxLevel);
			System.out.println("generarBackupsDesdeServidor: workouts obtenidos: " + (workouts != null ? workouts.size() : 0));
		} catch (Exception e) {
			System.err.println("generarBackupsDesdeServidor: no se pudieron obtener workouts: " + e.getMessage());
			workouts = new ArrayList<>();
		}
		guardarUsuarios(usuarios);
		guardarWorkouts(workouts);
	}

	
	public static void main(String[] args) {
		System.out.println("lectura.main: inicio");
		generarBackupsDesdeServidor();
		System.out.println("lectura.main: fin");
	}
}