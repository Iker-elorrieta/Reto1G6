package Modelo;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

public class Usuario {

	
	private String idUsuario;
	private String nombre;
	private String apellidos;
	private String email;
	private String pass;
	private Date fechaNacimiento;
	private double nivel;
	private String tipoUsuario;

	private static String collectionName = "usuarios";
	private static String fieldNombre = "nombre";
	private static String fieldApellidos = "apellidos";
	private static String fieldEmail = "email";
	private static String fieldPass = "pass";
	private static String fieldFechaNacimiento = "fnacimiento";
	private static String fieldNivel = "nivel";
	private static String fieldTipoUsuario = "tipoUsuario";

	
	public Usuario() {
	}

	public Usuario(String nombre, String apellidos, String email, String pass, Date fechaNacimiento) {
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.email = email;
		this.pass = pass;
		this.fechaNacimiento = fechaNacimiento;
		this.nivel = 0;
		this.tipoUsuario = "CLIENTE";
	}

	
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
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

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	
	public Usuario mObtenerUsuario(String idUsuario) {
		Firestore co = null;

		try {
			co = Conexion.conectar();
			DocumentSnapshot usuario = co.collection(collectionName).document(idUsuario).get().get();

			if (usuario.exists()) {
				setIdUsuario(usuario.getId());
				setNombre(usuario.getString(fieldNombre));
				setApellidos(usuario.getString(fieldApellidos));
				setEmail(usuario.getString(fieldEmail));
				setPass(usuario.getString(fieldPass));
				setNivel(usuario.getDouble(fieldNivel));
				setTipoUsuario(usuario.getString(fieldTipoUsuario));
				
				
				String fechaStr = usuario.getString(fieldFechaNacimiento);
				if (fechaStr != null) {
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					setFechaNacimiento(df.parse(fechaStr));
				}
			}

		} catch (Exception e) {
			System.out.println("Error: Clase Usuario, metodo mObtenerUsuario");
			e.printStackTrace();
		}

		return this;
	}

	public ArrayList<Usuario> mObtenerUsuarios() {
		Firestore co = null;
		ArrayList<Usuario> listaUsuarios = new ArrayList<Usuario>();

		try {
			co = Conexion.conectar();
			ApiFuture<QuerySnapshot> query = co.collection(collectionName).get();
			QuerySnapshot querySnapshot = query.get();
			List<QueryDocumentSnapshot> usuarios = querySnapshot.getDocuments();
			
			for (QueryDocumentSnapshot usuario : usuarios) {
				Usuario u = new Usuario();
				u.setIdUsuario(usuario.getId());
				u.setNombre(usuario.getString(fieldNombre));
				u.setApellidos(usuario.getString(fieldApellidos));
				u.setEmail(usuario.getString(fieldEmail));
				u.setPass(usuario.getString(fieldPass));
				u.setNivel(usuario.getDouble(fieldNivel));
				u.setTipoUsuario(usuario.getString(fieldTipoUsuario));
				
				
				String fechaStr = usuario.getString(fieldFechaNacimiento);
				if (fechaStr != null) {
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					u.setFechaNacimiento(df.parse(fechaStr));
				}
				
				listaUsuarios.add(u);
			}
			co.close();

		} catch (Exception e) {
			System.out.println("Error: Clase Usuario, metodo mObtenerUsuarios");
			e.printStackTrace();
		}

		return listaUsuarios;
	}

	public boolean mAnadirUsuario() {
		Firestore co = null;

		try {
			co = Conexion.conectar();
			
			
			ApiFuture<QuerySnapshot> queryFuture = co.collection(collectionName).whereEqualTo(fieldEmail, email).get();
			QuerySnapshot querySnapshot = queryFuture.get();
			if (querySnapshot != null && !querySnapshot.isEmpty()) {
				throw new IOException("El correo ya está registrado.");
			}

			Map<String, Object> nuevoUsuario = new HashMap<>();
			nuevoUsuario.put(fieldNombre, nombre);
			nuevoUsuario.put(fieldApellidos, apellidos);
			nuevoUsuario.put(fieldEmail, email);
			nuevoUsuario.put(fieldPass, pass);
			nuevoUsuario.put(fieldNivel, nivel);
			nuevoUsuario.put(fieldTipoUsuario, tipoUsuario);

			
			if (fechaNacimiento != null) {
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				nuevoUsuario.put(fieldFechaNacimiento, df.format(fechaNacimiento));
			}

			DocumentReference usuarioRef = co.collection(collectionName).document();
			usuarioRef.set(nuevoUsuario);
			co.close();
			return true;
			
		} catch (Exception e) {
			System.out.println("Error: Clase Usuario, metodo mAnadirUsuario");
			e.printStackTrace();
		}
		return false;
	}

	public boolean mAutenticarUsuario(String email, String pass) {
		Firestore co = null;

		try {
			co = Conexion.conectar();
			ApiFuture<QuerySnapshot> queryFuture = co.collection(collectionName).whereEqualTo(fieldEmail, email).get();
			QuerySnapshot querySnapshot = queryFuture.get();
			
			if (querySnapshot != null && !querySnapshot.isEmpty()) {
				for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
					String storedPass = doc.getString(fieldPass);
					if (storedPass != null && storedPass.equals(pass)) {
						return true;
					}
				}
			}
			co.close();
			
		} catch (Exception e) {
			System.out.println("Error: Clase Usuario, metodo mAutenticarUsuario");
			e.printStackTrace();
		}
		return false;
	}
}