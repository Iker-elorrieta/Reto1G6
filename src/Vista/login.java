package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JButton;

public class login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JTextField txtContrasena;

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
		
		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setForeground(Color.WHITE);
		lblUsuario.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblUsuario.setBackground(Color.WHITE);
		lblUsuario.setBounds(112, 282, 156, 41);
		contentPane.add(lblUsuario);
		
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(112, 349, 188, 41);
		contentPane.add(lblContrasena);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(327, 282, 300, 35);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtContrasena = new JTextField();
		txtContrasena.setColumns(10);
		txtContrasena.setBounds(327, 349, 300, 35);
		contentPane.add(txtContrasena);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.setForeground(new Color(240, 248, 255));
		btnEntrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEntrar.setBackground(new Color(139, 0, 0));
		btnEntrar.setBounds(538, 413, 89, 41);
		contentPane.add(btnEntrar);
		
		JLabel lblnoEstasRegistrado = new JLabel("¿No estas registrado?");
		lblnoEstasRegistrado.setForeground(Color.WHITE);
		lblnoEstasRegistrado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnoEstasRegistrado.setBackground(new Color(128, 0, 0));
		lblnoEstasRegistrado.setBounds(410, 500, 136, 35);
		contentPane.add(lblnoEstasRegistrado);
		
		JButton btnRegistrar = new JButton("Registrarme");
		btnRegistrar.setForeground(new Color(240, 248, 255));
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegistrar.setBackground(new Color(139, 0, 0));
		btnRegistrar.setBounds(400, 535, 156, 38);
		contentPane.add(btnRegistrar);
	}
}
