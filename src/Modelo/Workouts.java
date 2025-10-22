package Modelo;

import java.util.ArrayList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Workouts {
    
    private String Nombre; //sera usado como id
    private String Descripcion;
    private String Video;
    private int Nivel;
    private String owner; // campo opcional para indicar propietario

    // campo calculado
    private double duracionMinutos;

    public Workouts() {
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
    public static ArrayList<Workouts> mObtenerWorkouts() {
        ArrayList<Workouts> lista = new ArrayList<>();
        Firestore co = null;
        try {
            co = Conexion.conectar();
            ApiFuture<QuerySnapshot> query = co.collection("workouts").get();
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                Workouts w = new Workouts();
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
                    // ignore
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
}