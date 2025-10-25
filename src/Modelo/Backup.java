package Modelo;

import java.util.ArrayList;

import LecturaPB.lectura; // delegamos en la clase lectura para crear los .dat

public class Backup {

    public static void main(String[] args) {

        int maxLevel = 3;
        for (String a : args) {
            String arg = a.toLowerCase();

            if (arg.equals("usuarios")) {
                guardarUsuarios(new Usuario().mObtenerUsuarios());

            } else if (arg.equals("workouts")) {
                guardarWorkouts(new Workout().obtenerWorkouts((long) maxLevel));

            } else if (arg.equals("both")) {
                guardarUsuarios(new Usuario().mObtenerUsuarios());
                guardarWorkouts(new Workout().obtenerWorkouts((long) maxLevel));

            } else {
                System.out.println("Argumento no válido: " + a);
            }
        }

        HiloBackup hb = new HiloBackup();
        hb.start();
    }

    public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
        lectura.guardarUsuarios(usuarios);
    }

    public static void guardarWorkouts(ArrayList<Workout> workouts) {
        lectura.guardarWorkouts(workouts);
    }
}
