package Modelo;

import java.io.Serializable;
import java.util.ArrayList;

// Contenedor serializable para guardar workout con sus ejercicios y series
public class WorkoutCompleto implements Serializable {
    private static final long serialVersionUID = 1L;

    private Workout workout;
    private ArrayList<EjercicioConSeries> ejercicios;

    public WorkoutCompleto() {
        this.ejercicios = new ArrayList<>();
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public ArrayList<EjercicioConSeries> getEjercicios() {
        return ejercicios;
    }

    public void setEjercicios(ArrayList<EjercicioConSeries> ejercicios) {
        this.ejercicios = ejercicios;
    }
}
