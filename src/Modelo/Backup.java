package Modelo;

import java.util.ArrayList;

import LecturaPB.lectura; // delegamos en la clase lectura para crear los .dat

public class Backup {

    public static void main(String[] args) {

        int maxLevel = 3;
        Backup b = new Backup();
        for (String a : args) {
            String arg = a.toLowerCase();

            if (arg.equals("usuarios")) {
                b.guardarUsuarios(new Usuario().mObtenerUsuarios());

            } else if (arg.equals("workouts")) {
                b.guardarWorkouts((ArrayList<Workout>) new Workout().obtenerWorkouts((long) maxLevel));

            } else if (arg.equals("both")) {
                b.guardarUsuarios(new Usuario().mObtenerUsuarios());
                b.guardarWorkouts((ArrayList<Workout>) new Workout().obtenerWorkouts((long) maxLevel));

            } else {
                System.out.println("Argumento no válido: " + a);
            }
        }

        HiloBackup hb = new HiloBackup();
        hb.start();
    }

    public void guardarUsuarios(ArrayList<Usuario> usuarios) {
        lectura.guardarUsuarios(usuarios);
    }

    public void guardarWorkouts(ArrayList<Workout> workouts) {
        lectura.guardarWorkouts(workouts);
    }
}