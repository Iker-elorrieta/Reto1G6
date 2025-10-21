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
	private Vista.menu vistaMenu;
	private Vista.perfil vistaPerfil;
	private Usuario usuarioActual;


	public ControladorUsuario(Vista.login vistaLogin) {
		this.vistaLogin = vistaLogin;
		this.inicializarControladorLogin();
	}
	
	
	public ControladorUsuario(Vista.registro vistaRegistro) {
		this.vistaRegistro = vistaRegistro;
		this.inicializarControladorRegistro();
	}

	public ControladorUsuario(Vista.menu vistaMenu, Usuario usuario) {
		this.vistaMenu = vistaMenu;
		this.usuarioActual = usuario;
		this.inicializarControladorMenu();
	}

	public ControladorUsuario(Vista.perfil vistaPerfil, Vista.menu vistaMenu, Usuario usuario) {
		this.vistaPerfil = vistaPerfil;
		this.vistaMenu = vistaMenu;
		this.usuarioActual = usuario;
		this.inicializarControladorPerfil();
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

	private void inicializarControladorMenu() {
		this.vistaMenu.getBtnPerfil().addActionListener(this);
		this.vistaMenu.getBtnPerfil().setActionCommand("ABRIR_PERFIL");

		this.vistaMenu.getBtnCerrarSesion().addActionListener(this);
		this.vistaMenu.getBtnCerrarSesion().setActionCommand("CERRAR_SESION");
	}

	private void inicializarControladorPerfil() {
		this.vistaPerfil.setearUsuario(this.usuarioActual);

		this.vistaPerfil.getBtnVolver().addActionListener(this);
		this.vistaPerfil.getBtnVolver().setActionCommand("VOLVER_MENU");

	
		this.vistaPerfil.getBtnCambiarDatos().addActionListener(this);
		this.vistaPerfil.getBtnCambiarDatos().setActionCommand("CAMBIAR_DATOS");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String comando = e.getActionCommand();

		if ("LOGIN_USUARIO".equals(comando)) {
			this.mLoginUsuario();
		} else if ("CAMBIAR_DATOS".equals(comando)) {
			this.mCambiarDatos();
		} else if ("ABRIR_REGISTRO".equals(comando)) {
			this.mAbrirRegistro();
		} else if ("REGISTRAR_USUARIO".equals(comando)) {
			this.mRegistrarUsuario();
		} else if ("VOLVER_LOGIN".equals(comando)) {
			this.mVolverLogin();
		} else if ("ABRIR_PERFIL".equals(comando)) {
			this.mAbrirPerfil();
		} else if ("CERRAR_SESION".equals(comando)) {
			this.mCerrarSesion();
		} else if ("VOLVER_MENU".equals(comando)) {
			this.mVolverAMenu();
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

			new ControladorUsuario(ventanaMenu, usuario);

			this.vistaLogin.limpiarCampos();

			this.vistaLogin.getLblErrores().setText("Login exitoso - Bienvenido al GymApp");
		} else {
			this.vistaLogin.getLblErrores().setText("Credenciales incorrectas");
		}
	}
	

	private void mAbrirPerfil() {
		
		if (this.usuarioActual == null || this.vistaMenu == null) return;

		this.vistaMenu.setVisible(false);
		
		Vista.perfil ventanaPerfil = new Vista.perfil();
		
		new ControladorUsuario(ventanaPerfil, this.vistaMenu, this.usuarioActual);
		ventanaPerfil.setVisible(true);
	}

	private void mCerrarSesion() {
		if (this.vistaMenu != null) {
			this.vistaMenu.setVisible(false);
			this.vistaMenu.dispose();
		}
		Vista.login ventanaLogin = new Vista.login();
		ventanaLogin.setVisible(true);
		new ControladorUsuario(ventanaLogin);
	}

	
	private void mVolverAMenu() {
		if (this.vistaPerfil != null) {
			this.vistaPerfil.setVisible(false);
			this.vistaPerfil.dispose();
		}
		if (this.vistaMenu != null) {
			this.vistaMenu.setVisible(true);
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

	private void mCambiarDatos() {
    if (this.vistaPerfil == null || this.usuarioActual == null) return;

    String nombre = this.vistaPerfil.getTxtNombre().getText().trim();
    String apellidos = this.vistaPerfil.getTxtApellidos().getText().trim();
    String fechaNacStr = this.vistaPerfil.getTxtFechaNac().getText().trim();
    String pass = new String(this.vistaPerfil.getTxtPass().getPassword()).trim();

    if (nombre.isEmpty() || apellidos.isEmpty()) {
        this.vistaPerfil.getLblErrores().setText("Nombre y apellidos son obligatorios");
        return;
    }

  
    java.util.Date fechaNacimiento = null;
    if (!fechaNacStr.isEmpty()) {
        try {
            String normalized = fechaNacStr.replace('/', '-').trim();
            java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("dd-MM-yyyy");
            fechaNacimiento = df.parse(normalized);
        } catch (java.text.ParseException pe) {
            this.vistaPerfil.getLblErrores().setText("Formato de fecha incorrecto. Usa dd-MM-aaaa");
            return;
        }
    }

 
    this.usuarioActual.setNombre(nombre);
    this.usuarioActual.setApellidos(apellidos);
    if (!pass.isEmpty()) {
        this.usuarioActual.setPass(pass);
    }
    this.usuarioActual.setFechaNacimiento(fechaNacimiento);

    boolean ok = this.usuarioActual.mActualizarUsuario();
    if (ok) {
        this.vistaPerfil.getLblErrores().setText("Datos actualizados correctamente");
        if (this.vistaMenu != null) {
            String nombreParaMostrar = (this.usuarioActual.getNombre() != null && !this.usuarioActual.getNombre().isEmpty()) ? this.usuarioActual.getNombre() : this.usuarioActual.getEmail();
            this.vistaMenu.setBienvenido(nombreParaMostrar);
            this.vistaMenu.setNivelText(String.valueOf(this.usuarioActual.getNivel()));
        }
    } else {
        this.vistaPerfil.getLblErrores().setText("Error al actualizar usuario en servidor");
    }
}
}