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

import javax.swing.JPasswordField;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException; // added missing import

public class login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuarioCorreo;
	private JPasswordField txtPass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					login frame = new login();
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
	public login() {
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
		
		JLabel lblIniciarSesion = new JLabel("Iniciar Sesión");
		lblIniciarSesion.setForeground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setBackground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblIniciarSesion.setBounds(352, 187, 237, 41);
		contentPane.add(lblIniciarSesion);
		
		JLabel lblCorreo = new JLabel("Usuario:");
		lblCorreo.setForeground(Color.WHITE);
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblCorreo.setBackground(Color.WHITE);
		lblCorreo.setBounds(112, 282, 205, 41);
		contentPane.add(lblCorreo);
		
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(112, 349, 188, 41);
		contentPane.add(lblContrasena);
		
		txtUsuarioCorreo = new JTextField();
		txtUsuarioCorreo.setText("");
		txtUsuarioCorreo.setBounds(327, 282, 300, 35);
		contentPane.add(txtUsuarioCorreo);
		txtUsuarioCorreo.setColumns(10);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(327, 349, 300, 35);
		contentPane.add(txtPass);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.setForeground(new Color(240, 248, 255));
		btnEntrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEntrar.setBackground(new Color(139, 0, 0));
		btnEntrar.setBounds(538, 413, 89, 41);
		contentPane.add(btnEntrar);
		
		JLabel lblnoestasRegistrado = new JLabel("¿No estas registrado?");
		lblnoestasRegistrado.setForeground(Color.WHITE);
		lblnoestasRegistrado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnoestasRegistrado.setBackground(new Color(128, 0, 0));
		lblnoestasRegistrado.setBounds(410, 500, 136, 35);
		contentPane.add(lblnoestasRegistrado);
		
		JButton btnRegistrar = new JButton("Registrarme");
		btnRegistrar.setForeground(new Color(240, 248, 255));
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegistrar.setBackground(new Color(139, 0, 0));
		btnRegistrar.setBounds(400, 535, 156, 38);
		contentPane.add(btnRegistrar);

		// Action: abrir ventana de registro
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				login.this.dispose();
				registro r = new registro();
				r.setVisible(true);
			}
		});

		// Action: intentar login usando controlador.autenticar
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String email = txtUsuarioCorreo.getText().trim();
				String pass = new String(txtPass.getPassword()).trim();

				if (email.isEmpty() || pass.isEmpty()) {
					JOptionPane.showMessageDialog(login.this, "Rellena usuario y contraseña.", "Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				try {
					boolean ok = controlador.autenticar(email, pass);
					if (ok) {
						JOptionPane.showMessageDialog(login.this, "Inicio de sesión correcto.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
						// Aquí puedes abrir la ventana principal de la app
						// Por ahora cerramos login
						login.this.dispose();
					} else {
						JOptionPane.showMessageDialog(login.this, "Usuario o contraseña incorrectos.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (IOException ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(login.this, "Error de conexión: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
}