package Vista;


import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;

import Controlador.controlador;
import Modelo.Usuario;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.IOException;

// New import for password field
import javax.swing.JPasswordField;

public class registro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtApellido; 
	private JTextField txtFechaNac;
	private JTextField txtCorreo;
	private JPasswordField txtPass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					registro frame = new registro();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public registro() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 934, 643);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		 contentPane.setBackground(new Color(0, 0, 0));
		
		 JLabel lblLogo = new JLabel("");
		 lblLogo.setBounds(369, 11, 174, 165);

		 ImageIcon iconoOriginal = new ImageIcon("media/logo1.png");
		 Image imagen = iconoOriginal.getImage();

		 Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		 lblLogo.setIcon(new ImageIcon(imagenEscalada));

		 contentPane.add(lblLogo);
		
		JLabel lblRegistrarse = new JLabel("Registrarse");
		lblRegistrarse.setForeground(UIManager.getColor("Button.highlight"));
		lblRegistrarse.setBackground(UIManager.getColor("Button.highlight"));
		lblRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblRegistrarse.setBounds(369, 196, 179, 41);
		contentPane.add(lblRegistrarse);
		
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblNombre.setBackground(Color.WHITE);
		lblNombre.setBounds(98, 282, 164, 35);
		contentPane.add(lblNombre);
		
		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setForeground(Color.WHITE);
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblApellido.setBackground(Color.WHITE);
		lblApellido.setBounds(98, 328, 217, 35);
		contentPane.add(lblApellido);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(327, 282, 300, 35);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtApellido = new JTextField();
		txtApellido.setColumns(10);
		txtApellido.setBounds(327, 328, 300, 35);
		contentPane.add(txtApellido);
		
		JButton btnEntrar = new JButton("Volver");
		btnEntrar.setForeground(new Color(240, 248, 255));
		btnEntrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEntrar.setBackground(new Color(139, 0, 0));
		btnEntrar.setBounds(10, 11, 89, 41);
		contentPane.add(btnEntrar);
		
		JButton btnRegistrar = new JButton("Registrarme");
		btnRegistrar.setForeground(new Color(240, 248, 255));
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegistrar.setBackground(new Color(139, 0, 0));
		btnRegistrar.setBounds(673, 463, 149, 38);
		contentPane.add(btnRegistrar);
		
		txtFechaNac = new JTextField();
		txtFechaNac.setColumns(10);
		txtFechaNac.setBounds(327, 374, 300, 35);
		contentPane.add(txtFechaNac);
		
		txtCorreo = new JTextField();
		txtCorreo.setColumns(10);
		txtCorreo.setBounds(327, 420, 300, 35);
		contentPane.add(txtCorreo);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(327, 466, 300, 35);
		contentPane.add(txtPass);
		
		JLabel lblFNacimiento = new JLabel("F. Nacimiento:");
		lblFNacimiento.setForeground(Color.WHITE);
		lblFNacimiento.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblFNacimiento.setBackground(Color.WHITE);
		lblFNacimiento.setBounds(98, 374, 217, 35);
		contentPane.add(lblFNacimiento);
		
		JLabel lblCorreo = new JLabel("Correo:");
		lblCorreo.setForeground(Color.WHITE);
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblCorreo.setBackground(Color.WHITE);
		lblCorreo.setBounds(98, 420, 217, 35);
		contentPane.add(lblCorreo);
		
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(98, 466, 217, 35);
		contentPane.add(lblContrasena);

		btnRegistrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String nombre = txtNombre.getText().trim();
				String apellidos = txtApellido.getText().trim();
				String fechaStr = txtFechaNac.getText().trim();
				String correo = txtCorreo.getText().trim();
				String pass = new String(txtPass.getPassword()).trim();

				if (nombre.isEmpty() || apellidos.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
					JOptionPane.showMessageDialog(registro.this, "Rellena todos los campos obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				String emailtipo = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
				if (!correo.matches(emailtipo)) {
					JOptionPane.showMessageDialog(registro.this, "Introduce un correo electrónico válido.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (pass.length() < 6) {
					JOptionPane.showMessageDialog(registro.this, "La contraseña debe tener al menos 6 caracteres.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				Date fecha = null;
				if (!fechaStr.isEmpty()) {
					// Normalizar separadores y parsear dd-MM-yyyy
					String normalized = fechaStr.replace('/', '-').trim();
					SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
					try {
						fecha = df.parse(normalized);
					} catch (ParseException pe) {
						JOptionPane.showMessageDialog(registro.this, "Formato de fecha incorrecto. Usa dd-MM-aaaa (ej: 31-12-1990)", "Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
				}

				Usuario u = new Usuario(nombre, apellidos, correo, pass, fecha);
				try {
					boolean ok = controlador.guardarUsuario(u);
					if (ok) {
						JOptionPane.showMessageDialog(registro.this, "Usuario creado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						
						registro.this.dispose();
						login l = new login();
						l.setVisible(true);
					} else {
						JOptionPane.showMessageDialog(registro.this, "Error al crear el usuario. Intenta de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(registro.this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		
		btnEntrar.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				registro.this.dispose();
				login l = new login();
				l.setVisible(true);
			}
		});
	}
}