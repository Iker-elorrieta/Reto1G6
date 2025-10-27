package Modelo;

import com.google.cloud.Date;



public class historicoWorkout {

	private Workout id_workout; 
	private int ejercicios_hechos; 
	private int tiempo; 
	private Date fecha; 
	
	public historicoWorkout(Workout workout, int ejerciciosCompletados, int tiempoTotal, Date fecha) {
		this.id_workout = workout;
		this.ejercicios_hechos = ejerciciosCompletados;
		this.tiempo = tiempoTotal;
		this.fecha = fecha;
	}

	public Workout getId_workout() {
		return id_workout;
	}

	public void setId_workout(Workout id_workout) {
		this.id_workout = id_workout;
	}

	public int getEjercicios_hechos() {
		return ejercicios_hechos;
	}

	public void setEjercicios_hechos(int ejercicios_hechos) {
		this.ejercicios_hechos = ejercicios_hechos;
	}

	public int getTiempo() {
		return tiempo;
	}

	public void setTiempo(int tiempo) {
		this.tiempo = tiempo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	
	
	
}
