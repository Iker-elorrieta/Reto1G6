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

public class registro extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtApellidos; 
	private JTextField txtFechaNac;
	private JTextField txtEmail;
	private JPasswordField txtPass;
	private JButton btnRegistrar;
	private JButton btnVolver;
	private JLabel lblErrores;

	// UI constants
	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String TXT_REGISTRARSE = "Registrarse";
	private final String TXT_NOMBRE = "Nombre:";
	private final String TXT_APELLIDO = "Apellido:";
	private final String BTN_VOLVER_TXT = "Volver";
	private final String BTN_REGISTRAR_TXT = "Registrarme";
	private final String TXT_FNAC = "F. Nacimiento:";
	private final String TXT_CORREO = "Correo:";
	private final String TXT_PASS = "Contraseña:";

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

		ImageIcon iconoOriginal = new ImageIcon(IMG_LOGO_PATH);
		Image imagen = iconoOriginal.getImage();

		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));

		contentPane.add(lblLogo);
		
		JLabel lblRegistrarse = new JLabel(TXT_REGISTRARSE);
		lblRegistrarse.setForeground(UIManager.getColor("Button.highlight"));
		lblRegistrarse.setBackground(UIManager.getColor("Button.highlight"));
		lblRegistrarse.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblRegistrarse.setBounds(369, 196, 179, 41);
		contentPane.add(lblRegistrarse);
		
		JLabel lblNombre = new JLabel(TXT_NOMBRE);
		lblNombre.setForeground(Color.WHITE);
		lblNombre.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblNombre.setBackground(Color.WHITE);
		lblNombre.setBounds(98, 282, 164, 35);
		contentPane.add(lblNombre);
		
		JLabel lblApellido = new JLabel(TXT_APELLIDO);
		lblApellido.setForeground(Color.WHITE);
		lblApellido.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblApellido.setBackground(Color.WHITE);
		lblApellido.setBounds(98, 328, 217, 35);
		contentPane.add(lblApellido);
		
		txtNombre = new JTextField();
		txtNombre.setBounds(327, 282, 300, 35);
		contentPane.add(txtNombre);
		txtNombre.setColumns(10);
		
		txtApellidos = new JTextField();
		txtApellidos.setColumns(10);
		txtApellidos.setBounds(327, 328, 300, 35);
		contentPane.add(txtApellidos);
		
		btnVolver = new JButton(BTN_VOLVER_TXT);
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(10, 11, 89, 41);
		contentPane.add(btnVolver);
		
		btnRegistrar = new JButton(BTN_REGISTRAR_TXT);
		btnRegistrar.setForeground(new Color(240, 248, 255));
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRegistrar.setBackground(new Color(139, 0, 0));
		btnRegistrar.setBounds(673, 463, 149, 38);
		contentPane.add(btnRegistrar);
		
		txtFechaNac = new JTextField();
		txtFechaNac.setColumns(10);
		txtFechaNac.setBounds(327, 374, 300, 35);
		contentPane.add(txtFechaNac);
		
		txtEmail = new JTextField();
		txtEmail.setColumns(10);
		txtEmail.setBounds(327, 420, 300, 35);
		contentPane.add(txtEmail);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(327, 466, 300, 35);
		contentPane.add(txtPass);
		
		JLabel lblFNacimiento = new JLabel(TXT_FNAC);
		lblFNacimiento.setForeground(Color.WHITE);
		lblFNacimiento.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblFNacimiento.setBackground(Color.WHITE);
		lblFNacimiento.setBounds(98, 374, 217, 35);
		contentPane.add(lblFNacimiento);
		
		JLabel lblCorreo = new JLabel(TXT_CORREO);
		lblCorreo.setForeground(Color.WHITE);
		lblCorreo.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblCorreo.setBackground(Color.WHITE);
		lblCorreo.setBounds(98, 420, 217, 35);
		contentPane.add(lblCorreo);
		
		JLabel lblContrasena = new JLabel(TXT_PASS);
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(98, 466, 217, 35);
		contentPane.add(lblContrasena);
		
		lblErrores = new JLabel("");
		lblErrores.setForeground(Color.RED);
		lblErrores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblErrores.setBounds(327, 520, 400, 20);
		contentPane.add(lblErrores);
	}
	
	public void limpiarCampos() {
		this.txtNombre.setText("");
		this.txtApellidos.setText("");
		this.txtFechaNac.setText("");
		this.txtEmail.setText("");
		this.txtPass.setText("");
		this.lblErrores.setText("");
	}

	
	public JTextField getTxtNombre() { return txtNombre; }
	public JTextField getTxtApellidos() { return txtApellidos; }
	public JTextField getTxtFechaNac() { return txtFechaNac; }
	public JTextField getTxtEmail() { return txtEmail; }
	public JPasswordField getTxtPass() { return txtPass; }
	public JButton getBtnRegistrar() { return btnRegistrar; }
	public JButton getBtnVolver() { return btnVolver; }
	public JLabel getLblErrores() { return lblErrores; }
}