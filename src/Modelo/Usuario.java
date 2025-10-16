package Modelo;

import java.util.Date;

public class Usuario {
	
	private String nombre;
	private String apellidos;
	private String email;
	private String pass;
	private Date fechaNacimiento;
	private double nivel;
	

	public Usuario(String nombre, String apellidos, String email, String pass, Date fechaNacimiento) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.pass = pass;
        this.fechaNacimiento = fechaNacimiento;
        this.nivel = 0; 

    }

    public Usuario(String nombre, String apellidos, String email, String pass, Date fechaNacimiento, double nivel) {

        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.pass = pass;
        this.fechaNacimiento = fechaNacimiento;
        this.nivel = nivel;
    }

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public double getNivel() {
		return nivel;
	}

	public void setNivel(double nivel) {
		this.nivel = nivel;
	}
    
    
    
}
