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
import javax.swing.JPasswordField;

public class login extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtEmail;
	private JPasswordField txtPass;
	private JButton btnLogin;
	private JButton btnRegistrar;
	private JLabel lblErrores;

	// UI text constants
	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String TXT_TITULO = "Iniciar Sesión";
	private final String TXT_EMAIL = "Email:";
	private final String TXT_PASS = "Contraseña:";
	private final String TXT_ENTRAR = "Entrar";
	private final String TXT_PREG_REG = "¿No estas registrado?";
	private final String TXT_REGISTRAR = "Registrarme";

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

		ImageIcon iconoOriginal = new ImageIcon(IMG_LOGO_PATH);
		Image imagen = iconoOriginal.getImage();

		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));

		contentPane.add(lblLogo);
		
		JLabel lblIniciarSesion = new JLabel(TXT_TITULO);
		lblIniciarSesion.setForeground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setBackground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblIniciarSesion.setBounds(352, 187, 237, 41);
		contentPane.add(lblIniciarSesion);
		
		JLabel lblCorreo = new JLabel(TXT_EMAIL);
		lblCorreo.setForeground(Color.WHITE);
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblCorreo.setBackground(Color.WHITE);
		lblCorreo.setBounds(112, 282, 205, 41);
		contentPane.add(lblCorreo);
		
		JLabel lblContrasena = new JLabel(TXT_PASS);
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 30));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(112, 349, 188, 41);
		contentPane.add(lblContrasena);
		
		txtEmail = new JTextField();
		txtEmail.setText("");
		txtEmail.setBounds(327, 282, 300, 35);
		contentPane.add(txtEmail);
		txtEmail.setColumns(10);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(327, 349, 300, 35);
		contentPane.add(txtPass);
		
		btnLogin = new JButton(TXT_ENTRAR);
		btnLogin.setForeground(new Color(240, 248, 255));
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnLogin.setBackground(new Color(139, 0, 0));
		btnLogin.setBounds(538, 413, 89, 41);
		contentPane.add(btnLogin);
		
		JLabel lblnoestasRegistrado = new JLabel(TXT_PREG_REG);
		lblnoestasRegistrado.setForeground(Color.WHITE);
		lblnoestasRegistrado.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblnoestasRegistrado.setBackground(new Color(128, 0, 0));
		lblnoestasRegistrado.setBounds(410, 500, 136, 35);
		contentPane.add(lblnoestasRegistrado);
		
		btnRegistrar = new JButton(TXT_REGISTRAR);
		btnRegistrar.setForeground(new Color(240, 248, 255));
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegistrar.setBackground(new Color(139, 0, 0));
		btnRegistrar.setBounds(400, 535, 156, 38);
		contentPane.add(btnRegistrar);
		
		lblErrores = new JLabel("");
		lblErrores.setForeground(Color.RED);
		lblErrores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblErrores.setBounds(327, 465, 300, 20);
		contentPane.add(lblErrores);
	}
	
	public void limpiarCampos() {
		this.txtEmail.setText("");
		this.txtPass.setText("");
	}

	
	public JTextField getTxtEmail() { return txtEmail; }
	public JPasswordField getTxtPass() { return txtPass; }
	public JButton getBtnLogin() { return btnLogin; }
	public JButton getBtnRegistrar() { return btnRegistrar; }
	public JLabel getLblErrores() { return lblErrores; }
}