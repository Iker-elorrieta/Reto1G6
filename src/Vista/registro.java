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

public class registro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JTextField txtContrasena;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

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
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(327, 282, 300, 35);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		txtContrasena = new JTextField();
		txtContrasena.setColumns(10);
		txtContrasena.setBounds(327, 328, 300, 35);
		contentPane.add(txtContrasena);
		
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
		
		textField = new JTextField();
		textField.setColumns(10);
		textField.setBounds(327, 374, 300, 35);
		contentPane.add(textField);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(327, 420, 300, 35);
		contentPane.add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(327, 466, 300, 35);
		contentPane.add(textField_2);
		
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
		
		JLabel lblContrasena_3 = new JLabel("Contraseña:");
		lblContrasena_3.setForeground(Color.WHITE);
		lblContrasena_3.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblContrasena_3.setBackground(Color.WHITE);
		lblContrasena_3.setBounds(98, 466, 217, 35);
		contentPane.add(lblContrasena_3);
	}
}
