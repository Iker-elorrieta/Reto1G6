package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

public class EjercicioConSeries extends Ejercicio implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Serie> series;

    public EjercicioConSeries() {
        super();
        this.series = new ArrayList<>();
    }

    public ArrayList<Serie> getSeries() {
        return series;
    }

    public void setSeries(ArrayList<Serie> series) {
        this.series = series;
    }
}
