package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import Modelo.Usuario;

public class ControladorUsuario implements ActionListener {

	private Vista.login vistaLogin;
	private Vista.registro vistaRegistro;


	public ControladorUsuario(Vista.login vistaLogin) {
		this.vistaLogin = vistaLogin;
		this.inicializarControladorLogin();
	}
	
	

	
	public ControladorUsuario(Vista.registro vistaRegistro) {
		this.vistaRegistro = vistaRegistro;
		this.inicializarControladorRegistro();
	}

	private void inicializarControladorLogin() {
		
		this.vistaLogin.getBtnLogin().addActionListener(this);
		this.vistaLogin.getBtnLogin().setActionCommand("LOGIN_USUARIO");

		this.vistaLogin.getBtnRegistrar().addActionListener(this);
		this.vistaLogin.getBtnRegistrar().setActionCommand("ABRIR_REGISTRO");
	}

	private void inicializarControladorRegistro() {
	
		this.vistaRegistro.getBtnRegistrar().addActionListener(this);
		this.vistaRegistro.getBtnRegistrar().setActionCommand("REGISTRAR_USUARIO");

		this.vistaRegistro.getBtnVolver().addActionListener(this);
		this.vistaRegistro.getBtnVolver().setActionCommand("VOLVER_LOGIN");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		switch (comando) {
		case "LOGIN_USUARIO":
			this.mLoginUsuario();
			break;
		case "ABRIR_REGISTRO":
			this.mAbrirRegistro();
			break;
		case "REGISTRAR_USUARIO":
			this.mRegistrarUsuario();
			break;
		case "VOLVER_LOGIN":
			this.mVolverLogin();
			break;
		default:
			break;
		}
	}

	private void mLoginUsuario() {
		String email = this.vistaLogin.getTxtEmail().getText().trim();
		String pass = new String(this.vistaLogin.getTxtPass().getPassword()).trim();

		if (email.isEmpty() || pass.isEmpty()) {
			this.vistaLogin.getLblErrores().setText("Email y contraseña son obligatorios");
			return;
		}

		String emailId = email.trim().toLowerCase();
		Usuario usuario = new Usuario();
		if (usuario.mAutenticarUsuario(emailId, pass)) {
	
			usuario = usuario.mObtenerUsuario(emailId);


			Vista.menu ventanaMenu = new Vista.menu();
			String nombreParaMostrar = (usuario.getNombre() != null && !usuario.getNombre().isEmpty()) ? usuario.getNombre() : usuario.getEmail();
			ventanaMenu.setBienvenido(nombreParaMostrar);
			ventanaMenu.setNivelText(String.valueOf(usuario.getNivel()));

			
			ventanaMenu.setVisible(true);

			
			this.vistaLogin.setVisible(false);
			this.vistaLogin.dispose();

			
			ventanaMenu.getBtnCerrarSesion().addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					ventanaMenu.setVisible(false);
					ventanaMenu.dispose();				
					Vista.login ventanaLogin = new Vista.login();
					ventanaLogin.setVisible(true);
					new ControladorUsuario(ventanaLogin);
				}
			});

		
			this.vistaLogin.limpiarCampos();

			this.vistaLogin.getLblErrores().setText("Login exitoso - Bienvenido al GymApp");
		} else {
			this.vistaLogin.getLblErrores().setText("Credenciales incorrectas");
		}
	}
	

	private void mAbrirRegistro() {
		this.vistaLogin.setVisible(false);
		this.vistaLogin.dispose();
		
		Vista.registro ventanaRegistro = new Vista.registro();
		ventanaRegistro.setVisible(true);
		new ControladorUsuario(ventanaRegistro);
	}

	private void mRegistrarUsuario() {
		String nombre = this.vistaRegistro.getTxtNombre().getText().trim();
		String apellidos = this.vistaRegistro.getTxtApellidos().getText().trim();
		String email = this.vistaRegistro.getTxtEmail().getText().trim();
		String pass = new String(this.vistaRegistro.getTxtPass().getPassword()).trim();
		String fechaNacStr = this.vistaRegistro.getTxtFechaNac().getText().trim();

		if (nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || pass.isEmpty()) {
			this.vistaRegistro.getLblErrores().setText("Todos los campos son obligatorios");
			return;
		}

	
		String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
		if (!email.matches(emailRegex)) {
			this.vistaRegistro.getLblErrores().setText("Email inválido");
			return;
		}

		if (pass.length() < 6) {
			this.vistaRegistro.getLblErrores().setText("La contraseña debe tener al menos 6 caracteres");
			return;
		}

		Date fechaNacimiento = null;
		if (!fechaNacStr.isEmpty()) {
			try {
				String normalized = fechaNacStr.replace('/', '-').trim();
				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				fechaNacimiento = df.parse(normalized);
			} catch (ParseException pe) {
				this.vistaRegistro.getLblErrores().setText("Formato de fecha incorrecto. Usa dd-MM-aaaa");
				return;
			}
		}

		Usuario usuario = new Usuario(nombre, apellidos, email, pass, fechaNacimiento);

		if (usuario.mAnadirUsuario()) {
			this.vistaRegistro.getLblErrores().setText("Usuario registrado correctamente");
			this.vistaRegistro.limpiarCampos();
		} else {
			this.vistaRegistro.getLblErrores().setText("Error al registrar usuario - El email ya existe");
		}
	}

	private void mVolverLogin() {
		this.vistaRegistro.setVisible(false);
		this.vistaRegistro.dispose();
		
		Vista.login ventanaLogin = new Vista.login();
		ventanaLogin.setVisible(true);
		new ControladorUsuario(ventanaLogin);
	}
}