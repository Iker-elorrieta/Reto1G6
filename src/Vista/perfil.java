package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import Modelo.Usuario;
import java.text.SimpleDateFormat;


@SuppressWarnings("serial")//esto es automatico para quitar el warning
public class perfil extends JFrame {

	private JPanel contentPane;
	private JTextField txtNombre;
	private JTextField txtApellidos; 
	private JTextField txtFechaNac;
	private JPasswordField txtPass;
	private JButton btnCambiarDatos;
	private JButton btnVolver;
	private JLabel lblErrores;

	/**
	 * Create the frame.
	 */
	public perfil() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 934, 643);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(0, 0, 0));
		
		JLabel lblLogo = new JLabel("");
		lblLogo.setBounds(10, 11, 174, 165);

		ImageIcon iconoOriginal = new ImageIcon("media/logo1.png");
		Image imagen = iconoOriginal.getImage();

		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));

		contentPane.add(lblLogo);
		
		JLabel lblMenu = new JLabel("Tu Perfil");
		lblMenu.setForeground(UIManager.getColor("Button.highlight"));
		lblMenu.setBackground(UIManager.getColor("Button.highlight"));
		lblMenu.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblMenu.setBounds(418, 35, 140, 41);
		contentPane.add(lblMenu);
		
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
		
		txtApellidos = new JTextField();
		txtApellidos.setColumns(10);
		txtApellidos.setBounds(327, 328, 300, 35);
		contentPane.add(txtApellidos);
		
		txtFechaNac = new JTextField();
		txtFechaNac.setColumns(10);
		txtFechaNac.setBounds(327, 374, 300, 35);
		contentPane.add(txtFechaNac);
		
		txtPass = new JPasswordField();
		txtPass.setBounds(327, 420, 300, 35);
		contentPane.add(txtPass);
		
		JLabel lblFNacimiento = new JLabel("F. Nacimiento:");
		lblFNacimiento.setForeground(Color.WHITE);
		lblFNacimiento.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblFNacimiento.setBackground(Color.WHITE);
		lblFNacimiento.setBounds(98, 374, 217, 35);
		contentPane.add(lblFNacimiento);
		
		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setForeground(Color.WHITE);
		lblContrasena.setFont(new Font("Tahoma", Font.PLAIN, 27));
		lblContrasena.setBackground(Color.WHITE);
		lblContrasena.setBounds(98, 420, 217, 35);
		contentPane.add(lblContrasena);
		
		btnCambiarDatos = new JButton("Cambiar datos");
		btnCambiarDatos.setForeground(new Color(240, 248, 255));
		btnCambiarDatos.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCambiarDatos.setBackground(new Color(139, 0, 0));
		btnCambiarDatos.setBounds(358, 474, 237, 41);
		contentPane.add(btnCambiarDatos);
		
		btnVolver = new JButton("Volver");
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(747, 35, 119, 41);
		contentPane.add(btnVolver);
		
		lblErrores = new JLabel("");
		lblErrores.setForeground(Color.RED);
		lblErrores.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblErrores.setBounds(327, 563, 400, 20);
		contentPane.add(lblErrores);
		
	}



	public void setearUsuario(Usuario u) {
		if (u == null) {
		
			limpiarCampos();
		} else {
			String nombre = u.getNombre();
			if (nombre == null) {
				nombre = "";
			}
			txtNombre.setText(nombre);

			String apellidos = u.getApellidos();
			if (apellidos == null) {
				apellidos = "";
			}
			txtApellidos.setText(apellidos);

			txtFechaNac.setText(formatearFecha(u.getFechaNacimiento()));

			String pass = u.getPass();
			if (pass == null) {
				pass = "";
			}
			txtPass.setText(pass);
		}
	}


  
     private String formatearFecha(java.util.Date d) {
         if (d == null) return "";
         SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
         return df.format(d);
     }

 
     private void limpiarCampos() {
         txtNombre.setText("");
         txtApellidos.setText("");
         txtFechaNac.setText("");
         txtPass.setText("");
     }


     public JTextField getTxtNombre() { return txtNombre; }
     public JTextField getTxtApellidos() { return txtApellidos; }
     public JTextField getTxtFechaNac() { return txtFechaNac; }
     public JPasswordField getTxtPass() { return txtPass; }
     public JButton getBtnCambiarDatos() { return btnCambiarDatos; }
     public JButton getBtnVolver() { return btnVolver; }
     public JLabel getLblErrores() { return lblErrores; }

}