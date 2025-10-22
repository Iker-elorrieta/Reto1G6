package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import Modelo.Ejercicio;
import Modelo.Serie;
import Modelo.Usuario;
import Modelo.Workouts;

public class ControladorUsuario implements ActionListener {

    private Vista.login vistaLogin;
    private Vista.registro vistaRegistro;
    private Vista.menu vistaMenu;
    private Vista.perfil vistaPerfil;
    private Vista.workouts vistaWorkouts;
    private Vista.ejercicios vistaEjercicios;
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

        // Botón para abrir pantalla de workouts
        this.vistaMenu.getBtnEmpezar().addActionListener(this);
        this.vistaMenu.getBtnEmpezar().setActionCommand("ABRIR_WORKOUTS");
    }

    private void inicializarControladorPerfil() {
        this.vistaPerfil.setearUsuario(this.usuarioActual);

        this.vistaPerfil.getBtnVolver().addActionListener(this);
        this.vistaPerfil.getBtnVolver().setActionCommand("VOLVER_MENU");


        this.vistaPerfil.getBtnCambiarDatos().addActionListener(this);
        this.vistaPerfil.getBtnCambiarDatos().setActionCommand("CAMBIAR_DATOS");
    }

    // Inicializador para la vista workouts
    private void inicializarControladorWorkouts() {

        this.vistaWorkouts.getBtnVolver().addActionListener(this);
        this.vistaWorkouts.getBtnVolver().setActionCommand("VOLVER_WORKOUTS");

        // Poblar tabla de workouts
        try {
            ArrayList<Workouts> lista = Workouts.mObtenerWorkouts();
            DefaultTableModel model = (DefaultTableModel) this.vistaWorkouts.getTableWorkouts().getModel();
            // limpiar
            model.setRowCount(0);
            for (Workouts w : lista) {
                // agregar solo workouts que el usuario pueda ver (propietario o nivel suficiente)
                boolean permitido = false;
                if (this.usuarioActual != null) {
                    if (w.getOwner() != null && !w.getOwner().isEmpty() && this.usuarioActual.getEmail() != null && w.getOwner().equalsIgnoreCase(this.usuarioActual.getEmail())) {
                        permitido = true;
                    } else if (this.usuarioActual.getNivel() >= w.getNivel()) {
                        permitido = true;
                    }
                }
                if (permitido) {
                    Object[] row = new Object[] { w.getNombre(), w.getNivel(), String.format("%.1f", w.getDuracionMinutos()), w.getVideo() };
                    model.addRow(row);
                }
             }
             if (model.getRowCount() == 0) {
                 javax.swing.JOptionPane.showMessageDialog(null, "No hay workouts disponibles para tu nivel", "Información", javax.swing.JOptionPane.INFORMATION_MESSAGE);
             }
         } catch (Exception e) {
             System.out.println("Error al cargar workouts en la vista");
             e.printStackTrace();
         }
 
         // Doble clic en fila para abrir ejercicios
         this.vistaWorkouts.getTableWorkouts().addMouseListener(new MouseAdapter() {
             @Override
             public void mouseClicked(MouseEvent e) {
                 if (e.getClickCount() == 2) {
                     int row = vistaWorkouts.getTableWorkouts().getSelectedRow();
                     if (row >= 0) {
                         // Como filtramos la lista al poblarla, cualquier fila seleccionada es accesible
                         String workoutId = String.valueOf(vistaWorkouts.getTableWorkouts().getValueAt(row, 0));
                         mAbrirEjercicios(workoutId);
                     }
                 }
             }
         });
    }

    // Nuevo constructor para manejar workouts con usuario
    public ControladorUsuario(Vista.workouts vistaWorkouts, Vista.menu vistaMenu, Usuario usuario) {
        this.vistaWorkouts = vistaWorkouts;
        this.vistaMenu = vistaMenu;
        this.usuarioActual = usuario;
        this.inicializarControladorWorkouts();
    }

    // Sobrecarga: permitir crear controlador de workouts sin usuario (compatibilidad)
    public ControladorUsuario(Vista.workouts vistaWorkouts, Vista.menu vistaMenu) {
        this(vistaWorkouts, vistaMenu, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();

        if ("LOGIN_USUARIO".equals(comando)) {
            this.mLoginUsuario();
        } else if ("CAMBIAR_DATOS".equals(comando)) {
            this.mCambiarDatos();
        } else if ("ABRIR_WORKOUTS".equals(comando)) {
            this.mAbrirWorkouts();
        } else if ("VOLVER_WORKOUTS".equals(comando)) {
            this.mVolverDesdeWorkouts();
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

    private void mAbrirWorkouts() {


        Vista.workouts ventanaWorkouts = new Vista.workouts();

        this.vistaMenu.setVisible(false);

        ventanaWorkouts.setVisible(true);
        // pasar el usuario para control de permisos
        new ControladorUsuario(ventanaWorkouts, this.vistaMenu, this.usuarioActual);
    }

    private void mVolverDesdeWorkouts() {
        if (this.vistaWorkouts != null) {
            this.vistaWorkouts.setVisible(false);
            this.vistaWorkouts.dispose();
        }
        if (this.vistaMenu != null) {
            this.vistaMenu.setVisible(true);
        }
    }

    // Abrir la vista de ejercicios para un workout concreto
    private void mAbrirEjercicios(String workoutId) {
        try {
            vistaEjercicios = new Vista.ejercicios();

            // Poblar tabla de ejercicios
            DefaultTableModel model = (DefaultTableModel) vistaEjercicios.getTableEjercicios().getModel();
            model.setRowCount(0);
            ArrayList<Ejercicio> lista = Ejercicio.mObtenerEjercicios(workoutId);
            for (Ejercicio e : lista) {
                Object[] row = new Object[] { e.getNombre(), String.format("%.1f", e.getDuracionMinutos()), e.getSeriesCount(), String.format("%.1f", e.getAvgTiempoDescanso()), String.format("%.1f", e.getAvgTiempoSerie()) };
                model.addRow(row);
            }

            // listener de selección para actualizar descripcion e imagen
            vistaEjercicios.getTableEjercicios().getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                @Override
                public void valueChanged(ListSelectionEvent e) {
                    int row = vistaEjercicios.getTableEjercicios().getSelectedRow();
                    if (row >= 0) {
                        String ejercicioId = String.valueOf(vistaEjercicios.getTableEjercicios().getValueAt(row, 0));
                        // buscar ejercicio en lista
                        Ejercicio seleccionado = null;
                        for (Ejercicio ex : lista) if (ex.getNombre().equals(ejercicioId)) { seleccionado = ex; break; }
                        if (seleccionado != null) {
                            vistaEjercicios.getLblDescripcion().setText("Descripción: " + (seleccionado.getDescripcion() != null ? seleccionado.getDescripcion() : ""));
                            String img = seleccionado.getImagen();
                            if (img != null && !img.isEmpty()) {
                                try {
                                    javax.swing.JLabel lbl = vistaEjercicios.getLblImg();
                                    java.awt.Image orig = null;
                                    // Intentar cargar desde fichero absoluto/relativo
                                    try {
                                        java.io.File f = new java.io.File(img);
                                        if (f.exists()) {
                                            orig = new javax.swing.ImageIcon(f.getAbsolutePath()).getImage();
                                        }
                                    } catch (Exception __fex) {
                                        // ignore
                                    }
                                    // Si no se cargó desde fichero, intentar desde resources (classpath)
                                    if (orig == null) {
                                        try {
                                            java.net.URL res = ControladorUsuario.class.getResource("/" + img);
                                            if (res != null) {
                                                orig = new javax.swing.ImageIcon(res).getImage();
                                            }
                                        } catch (Exception __re) {
                                            // ignore
                                        }
                                    }
                                    // Último recurso: dejar que ImageIcon intente interpretar como URL/path
                                    if (orig == null) {
                                        try {
                                            javax.swing.ImageIcon tmp = new javax.swing.ImageIcon(img);
                                            orig = tmp.getImage();
                                        } catch (Exception __ux) {
                                            orig = null;
                                        }
                                    }
                                    int lw = lbl.getWidth();
                                    int lh = lbl.getHeight();
                                    if (lw <= 0) lw = 335; // fallback to design size
                                    if (lh <= 0) lh = 220;
                                    if (orig != null) {
                                        int ow = orig.getWidth(null);
                                        int oh = orig.getHeight(null);
                                        if (ow <= 0 || oh <= 0) {
                                            // cannot determine original size, try direct set
                                            lbl.setIcon(new javax.swing.ImageIcon(orig));
                                            lbl.setText("");
                                        } else {
                                            double scale = Math.min((double)lw / ow, (double)lh / oh);
                                            int nw = (int)Math.max(1, Math.round(ow * scale));
                                            int nh = (int)Math.max(1, Math.round(oh * scale));
                                            java.awt.Image scaled = orig.getScaledInstance(nw, nh, java.awt.Image.SCALE_SMOOTH);
                                            lbl.setIcon(new javax.swing.ImageIcon(scaled));
                                            lbl.setText("");
                                        }
                                    } else {
                                        lbl.setIcon(null);
                                        lbl.setText("Imagen no disponible");
                                    }
                                } catch (Exception exx) {
                                    vistaEjercicios.getLblImg().setText("Imagen no disponible");
                                    vistaEjercicios.getLblImg().setIcon(null);
                                }
                            } else {
                                vistaEjercicios.getLblImg().setText("Imagen no disponible");
                                vistaEjercicios.getLblImg().setIcon(null);
                            }
                        }
                    }
                }
            });

            // boton volver en ejercicios vuelve a workouts
            vistaEjercicios.getBtnVolver().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    vistaEjercicios.setVisible(false);
                    vistaEjercicios.dispose();
                    if (vistaWorkouts != null) vistaWorkouts.setVisible(true);
                }
            });

            // mostrar vista
            if (vistaWorkouts != null) {
                vistaWorkouts.setVisible(false);
            }
            vistaEjercicios.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error al abrir ejercicios", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}