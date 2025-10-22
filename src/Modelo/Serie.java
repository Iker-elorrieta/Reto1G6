package Modelo;

import java.util.ArrayList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Serie extends Ejercicio {
    
    private String Nombre; //sera usado como id
    private int Cantidad;
    private int Tiempo_serie;
    private int Tiempo_descanso;

    public Serie() {
    }

    public Serie(String nombre, int cantidad, int tiempoSerie, int tiempoDescanso) {
        this.Nombre = nombre;
        this.Cantidad = cantidad;
        this.Tiempo_serie = tiempoSerie;
        this.Tiempo_descanso = tiempoDescanso;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int cantidad) {
        Cantidad = cantidad;
    }

    public int getTiempo_serie() {
        return Tiempo_serie;
    }

    public void setTiempo_serie(int tiempo_serie) {
        Tiempo_serie = tiempo_serie;
    }

    public int getTiempo_descanso() {
        return Tiempo_descanso;
    }

    public void setTiempo_descanso(int tiempo_descanso) {
        Tiempo_descanso = tiempo_descanso;
    }

    // Calcula la duración total de esta serie en minutos
    public double getDuracionMinutos() {
        int cantidad = (Cantidad > 0) ? Cantidad : 0;
        if (cantidad == 0) return 0.0;
        int totalSegundos = cantidad * Tiempo_serie;
        if (cantidad > 1) {
            totalSegundos += (cantidad - 1) * Tiempo_descanso;
        }
        return totalSegundos / 60.0;
    }

    // Obtiene todas las Series para un ejercicio específico dentro de un workout
    public static ArrayList<Serie> mObtenerSeries(String workoutId, String ejercicioId) {
        ArrayList<Serie> lista = new ArrayList<>();
        Firestore co = null;
        try {
            co = Conexion.conectar();
            ApiFuture<QuerySnapshot> query = co.collection("workouts")
                    .document(workoutId)
                    .collection("ejercicios")
                    .document(ejercicioId)
                    .collection("Series")
                    .get();
            QuerySnapshot querySnapshot = query.get();
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                Serie s = new Serie();
                s.setNombre(doc.getId());
                Long cantidadL = doc.getLong("Cantidad");
                Long tiempoSerieL = doc.getLong("Tiempo_serie");
                Long tiempoDescL = doc.getLong("Tiempo_descanso");
                if (cantidadL != null) s.setCantidad(cantidadL.intValue());
                if (tiempoSerieL != null) s.setTiempo_serie(tiempoSerieL.intValue());
                if (tiempoDescL != null) s.setTiempo_descanso(tiempoDescL.intValue());
                lista.add(s);
            }
            co.close();
        } catch (Exception e) {
            System.out.println("Error: Clase Serie - mObtenerSeries");
            e.printStackTrace();
        }
        return lista;
    }
}