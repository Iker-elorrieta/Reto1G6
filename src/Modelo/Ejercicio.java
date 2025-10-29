package Modelo;

import java.util.ArrayList;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Ejercicio extends Workout {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String Nombre; //sera usado como id
    private String Descripcion;
    private String Imagen;

    // Campos calculados a partir de las Series
    private int seriesCount;
    private double duracionMinutos;
    private double avgTiempoDescanso;
    private double avgTiempoSerie;

    public Ejercicio() {
    }

    public Ejercicio(String nombre, String descripcion, String imagen) {
        this.Nombre = nombre;
        this.Descripcion = descripcion;
        this.Imagen = imagen;
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

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public int getSeriesCount() {
        return seriesCount;
    }

    public void setSeriesCount(int seriesCount) {
        this.seriesCount = seriesCount;
    }

    public double getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(double duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public double getAvgTiempoDescanso() {
        return avgTiempoDescanso;
    }

    public void setAvgTiempoDescanso(double avgTiempoDescanso) {
        this.avgTiempoDescanso = avgTiempoDescanso;
    }

    public double getAvgTiempoSerie() {
        return avgTiempoSerie;
    }

    public void setAvgTiempoSerie(double avgTiempoSerie) {
        this.avgTiempoSerie = avgTiempoSerie;
    }

    // Obtiene los ejercicios para un workout y calcula métricas basadas en sus Series
    public ArrayList<Ejercicio> mObtenerEjercicios(String workoutId) {
         ArrayList<Ejercicio> lista = new ArrayList<>();
         Firestore co = null;
         try {
             co = Conexion.conectar();
             ApiFuture<QuerySnapshot> query = co.collection("workouts")
                     .document(workoutId)
                     .collection("ejercicios")
                     .get();
             QuerySnapshot querySnapshot = query.get();
             for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                 Ejercicio e = new Ejercicio();
                 e.setNombre(doc.getId());
                 e.setDescripcion(doc.getString("Descripcion"));
                 e.setImagen(doc.getString("Imagen"));

                 // Obtener series asociadas y calcular métricas
                 ArrayList<Serie> series = new Serie().mObtenerSeries(workoutId, doc.getId());
                 //int totalSeries = 0;
                 double totalDuracion = 0.0;
                 double totalDesc = 0.0;
                 double totalTserie = 0.0;
                 for (Serie s : series) {
                    // totalSeries += s.getCantidad();
                     totalDuracion += s.getDuracionMinutos();
                     totalDesc += s.getTiempo_descanso();
                     totalTserie += s.getTiempo_serie();
                 }
                 e.setSeriesCount(series.size());
                 e.setDuracionMinutos(totalDuracion);
                 if (series.size() > 0) {
                     e.setAvgTiempoDescanso(totalDesc / series.size());
                     e.setAvgTiempoSerie(totalTserie / series.size());
                 } else {
                     e.setAvgTiempoDescanso(0);
                     e.setAvgTiempoSerie(0);
                 }

                 lista.add(e);
             }
             co.close();
         } catch (Exception ex) {
             System.out.println("Error Obtener Ejercicios");
             ex.printStackTrace();
         }
         return lista;
     }
}