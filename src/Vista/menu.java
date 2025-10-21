package Vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.text.DecimalFormat;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class menu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnPerfil;
	private JButton btnCerrarSesion; 
	private JLabel lblbienvenido; 
	private JLabel lblNivel; 
	private JButton btnEmpezar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					menu frame = new menu();
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
	public menu() {
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
		
		JLabel lblMenu = new JLabel("Menú");
		lblMenu.setForeground(UIManager.getColor("Button.highlight"));
		lblMenu.setBackground(UIManager.getColor("Button.highlight"));
		lblMenu.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblMenu.setBounds(423, 34, 91, 41);
		contentPane.add(lblMenu);
		
		lblbienvenido = new JLabel("Bienvenido, ");
		lblbienvenido.setForeground(Color.WHITE);
		lblbienvenido.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblbienvenido.setBackground(Color.WHITE);
		lblbienvenido.setBounds(308, 109, 330, 41);
		contentPane.add(lblbienvenido);
		
		lblNivel = new JLabel("Tu nivel es: ");
		lblNivel.setForeground(new Color(128, 0, 0));
		lblNivel.setFont(new Font("Tahoma", Font.BOLD, 26));
		lblNivel.setBackground(Color.WHITE);
		lblNivel.setBounds(379, 491, 237, 41);
		contentPane.add(lblNivel);
		
		btnPerfil = new JButton("Perfil");
		btnPerfil.setForeground(new Color(240, 248, 255));
		btnPerfil.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnPerfil.setBackground(new Color(139, 0, 0));
		btnPerfil.setBounds(775, 34, 89, 41);
		contentPane.add(btnPerfil);
		
		btnEmpezar = new JButton("Empezar entrenamiento");
		btnEmpezar.setForeground(new Color(240, 248, 255));
		btnEmpezar.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnEmpezar.setBackground(new Color(139, 0, 0));
		btnEmpezar.setBounds(324, 218, 237, 41);
		contentPane.add(btnEmpezar);
		
		JButton btnHistorico = new JButton("Ver Histórico");
		btnHistorico.setForeground(new Color(240, 248, 255));
		btnHistorico.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnHistorico.setBackground(new Color(139, 0, 0));
		btnHistorico.setBounds(324, 288, 237, 41);
		contentPane.add(btnHistorico);
		
	
		btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.setForeground(new Color(240, 248, 255));
		btnCerrarSesion.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnCerrarSesion.setBackground(new Color(139, 0, 0));
		btnCerrarSesion.setBounds(324, 354, 237, 41);
		contentPane.add(btnCerrarSesion);

	}

	
	public void setBienvenido(String nombre) {
		this.lblbienvenido.setText("Bienvenido, " + nombre);
	}

	public void setNivelText(String nivel) {
		try {
			double v = Double.parseDouble(nivel);
			DecimalFormat df = new DecimalFormat("#.##");
			String formatted = df.format(v);
			this.lblNivel.setText("Tu nivel es: " + formatted);
		} catch (NumberFormatException ex) {
			// Si no es un número, mostramos tal cual
			this.lblNivel.setText("Tu nivel es: " + nivel);
		}
	}

	public JButton getBtnPerfil() { return btnPerfil; }
	public JButton getBtnEmpezar() { return btnEmpezar; }
	public JButton getBtnCerrarSesion() { return btnCerrarSesion; }
	public JLabel getLblbienvenido() { return lblbienvenido; }
	public JLabel getLblNivel() { return lblNivel; }
}