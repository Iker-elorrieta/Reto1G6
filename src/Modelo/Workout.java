package Modelo;

import java.util.ArrayList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Workout implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
	private static String collectionName = "workouts";
    private String Nombre; //sera usado como id
    private String Descripcion;
    private String Video;
    private int Nivel;
    private String owner; // campo opcional para indicar propietario

    
    
    
    // campo calculado
    private double duracionMinutos;

    public Workout() {
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getVideo() {
        return Video;
    }

    public void setVideo(String video) {
        Video = video;
    }

    public int getNivel() {
        return Nivel;
    }

    public void setNivel(int nivel) {
        Nivel = nivel;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public double getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(double duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    // Obtener todos los workouts y calcular su duración sumando duraciones de ejercicios
    public static ArrayList<Workout> mObtenerWorkouts() {
        ArrayList<Workout> lista = new ArrayList<>();
        Firestore co = null;
        try {
            co = Conexion.conectar();
            ApiFuture<QuerySnapshot> query = co.collection("workouts").get();
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                Workout w = new Workout();
                w.setNombre(doc.getId());
                w.setDescripcion(doc.getString("Descripcion"));
                w.setVideo(doc.getString("Video"));
                Long nivelL = doc.getLong("Nivel");
                if (nivelL != null) w.setNivel(nivelL.intValue());
                // owner opcional
                String ownerStr = doc.getString("owner");
                if (ownerStr == null) ownerStr = doc.getString("Usuario");
                w.setOwner(ownerStr);

                // Calcular duración sumando ejercicios
                double durTotal = 0.0;
                try {
                    ArrayList<Ejercicio> ejercs = Ejercicio.mObtenerEjercicios(doc.getId());
                    for (Ejercicio e : ejercs) {
                        durTotal += e.getDuracionMinutos();
                    }
                } catch (Exception ex) {
                 
                }
                w.setDuracionMinutos(durTotal);

                lista.add(w);
            }
            co.close();
        } catch (Exception e) {
            System.out.println("Error: Clase Workouts - mObtenerWorkouts");
            e.printStackTrace();
        }
        return lista;
    }
    
    public ArrayList<Workout> obtenerWorkouts(Long nivelUsuario) {
        ArrayList<Workout> lista = new ArrayList<>();
        Firestore co = null;
        try {
            co = Conexion.conectar();
            ApiFuture<QuerySnapshot> query;
            if (nivelUsuario != null) {
                // filtrar por nivel si se pasa un valor
                query = co.collection(collectionName).whereLessThanOrEqualTo("Nivel", nivelUsuario).get();
            } else {
                query = co.collection(collectionName).get();
            }

            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                Workout w = new Workout();
                w.setNombre(doc.getId());
                w.setDescripcion(doc.getString("Descripcion"));
                w.setVideo(doc.getString("Video"));
                Long nivelL = doc.getLong("Nivel");
                if (nivelL != null) w.setNivel(nivelL.intValue());
                String ownerStr = doc.getString("owner");
                if (ownerStr == null) ownerStr = doc.getString("Usuario");
                w.setOwner(ownerStr);

                // Calcular duración sumando ejercicios relacionados a este workout
                double durTotal = 0.0;
                try {
                    ArrayList<Ejercicio> ejercs = Ejercicio.mObtenerEjercicios(doc.getId());
                    for (Ejercicio e : ejercs) {
                        durTotal += e.getDuracionMinutos();
                    }
                } catch (Exception ex) {
                    // ignorar errores al obtener ejercicios y dejar duración en 0
                }
                w.setDuracionMinutos(durTotal);

                lista.add(w);
            }
            co.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}