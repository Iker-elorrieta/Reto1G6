package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class entrenamiento extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private JLabel lblTWorkout;
	private JLabel lblTEjercicio;
	private JLabel lblTSerie;
	private JLabel lblTDescanso;
	private JLabel lblNumSeriesRestantes;
	private JLabel lblNumDescansoRestantes;
	private JLabel lblNombreEjercicio;
	private JLabel lblNombreWorkout;
	private JLabel lblDescripcionEjercicio;
	private JLabel lblImagenEjercicio;
	private JButton btnTerminar;
	private JButton btnPararEmpezar;

	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String TXT_TITULO = "Entrenamiento";

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					entrenamiento frame = new entrenamiento();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public entrenamiento() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 934, 643);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.BLACK);

		JLabel lblLogo = new JLabel("");
		lblLogo.setBounds(10, 11, 184, 165);

		ImageIcon iconoOriginal = new ImageIcon(IMG_LOGO_PATH);
		Image imagen = iconoOriginal.getImage();
		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));
		contentPane.add(lblLogo);

		JLabel lblIniciarSesion = new JLabel(TXT_TITULO);
		lblIniciarSesion.setForeground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setBackground(UIManager.getColor("Button.highlight"));
		lblIniciarSesion.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblIniciarSesion.setBounds(356, 71, 237, 41);
		contentPane.add(lblIniciarSesion);

		JPanel panel = new JPanel();
		panel.setBackground(new Color(139, 0, 0));
		panel.setBounds(10, 251, 174, 53);
		contentPane.add(panel);
		panel.setLayout(null);
		
				lblTWorkout = new JLabel("Tiempo total: 00:00 mins");
				lblTWorkout.setBounds(0, 0, 174, 53);
				panel.add(lblTWorkout);
				lblTWorkout.setForeground(Color.WHITE);

		btnTerminar = new JButton("Terminar");
		btnTerminar.setBackground(new Color(139, 0, 0));
		btnTerminar.setForeground(Color.BLACK);
		btnTerminar.setBounds(530, 528, 90, 53);
		contentPane.add(btnTerminar);

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBackground(new Color(139, 0, 0));
		panel_1.setBounds(10, 315, 174, 53);
		contentPane.add(panel_1);
		
				lblTEjercicio = new JLabel("Tiempo Ejercicio: 00:00 mins");
				lblTEjercicio.setBounds(0, 0, 174, 53);
				panel_1.add(lblTEjercicio);
				lblTEjercicio.setForeground(Color.WHITE);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBackground(new Color(139, 0, 0));
		panel_3.setBounds(734, 315, 174, 53);
		contentPane.add(panel_3);
		
				lblNombreEjercicio = new JLabel("Ejercicio:");
				lblNombreEjercicio.setBounds(0, 0, 174, 53);
				panel_3.add(lblNombreEjercicio);
				lblNombreEjercicio.setForeground(Color.WHITE);

		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBackground(new Color(139, 0, 0));
		panel_2.setBounds(734, 251, 174, 53);
		contentPane.add(panel_2);
		
				lblNombreWorkout = new JLabel("Entrenamiento:");
				lblNombreWorkout.setBounds(0, 0, 174, 53);
				panel_2.add(lblNombreWorkout);
				lblNombreWorkout.setForeground(Color.WHITE);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBackground(new Color(139, 0, 0));
		panel_5.setBounds(10, 187, 898, 53);
		contentPane.add(panel_5);
		
				lblDescripcionEjercicio = new JLabel("Descripción:");
				lblDescripcionEjercicio.setBounds(0, 11, 898, 42);
				panel_5.add(lblDescripcionEjercicio);
				lblDescripcionEjercicio.setVerticalAlignment(SwingConstants.TOP);
				lblDescripcionEjercicio.setForeground(Color.WHITE);

		JPanel panel_5_1 = new JPanel();
		panel_5_1.setLayout(null);
		panel_5_1.setBackground(new Color(139, 0, 0));
		panel_5_1.setBounds(231, 251, 217, 245);
		contentPane.add(panel_5_1);

		JLabel lbl1 = new JLabel("TIEMPO SERIE");
		lbl1.setFont(new Font("Tahoma", Font.BOLD, 18));
		lbl1.setForeground(Color.WHITE);
		lbl1.setBounds(38, 11, 144, 27);
		panel_5_1.add(lbl1);

		lblTSerie = new JLabel("00:00");
		lblTSerie.setForeground(Color.WHITE);
		lblTSerie.setFont(new Font("Tahoma", Font.BOLD, 49));
		lblTSerie.setBounds(38, 51, 144, 119);
		panel_5_1.add(lblTSerie);

		lblNumSeriesRestantes = new JLabel("Series restantes: 0");
		lblNumSeriesRestantes.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNumSeriesRestantes.setBounds(48, 181, 114, 53);
		panel_5_1.add(lblNumSeriesRestantes);
		lblNumSeriesRestantes.setForeground(Color.WHITE);

		JPanel panel_5_1_1 = new JPanel();
		panel_5_1_1.setLayout(null);
		panel_5_1_1.setBackground(new Color(139, 0, 0));
		panel_5_1_1.setBounds(472, 251, 217, 245);
		contentPane.add(panel_5_1_1);

		JLabel lbl2 = new JLabel("  TIEMPO DESCANSO");
		lbl2.setForeground(Color.WHITE);
		lbl2.setFont(new Font("Tahoma", Font.BOLD, 18));
		lbl2.setBounds(10, 11, 197, 27);
		panel_5_1_1.add(lbl2);

		lblTDescanso = new JLabel("00:00");
		lblTDescanso.setForeground(Color.WHITE);
		lblTDescanso.setFont(new Font("Tahoma", Font.BOLD, 49));
		lblTDescanso.setBounds(35, 56, 144, 119);
		panel_5_1_1.add(lblTDescanso);

		lblNumDescansoRestantes = new JLabel("Descansos restantes: 0");
		lblNumDescansoRestantes.setForeground(Color.WHITE);
		lblNumDescansoRestantes.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblNumDescansoRestantes.setBounds(45, 181, 134, 53);
		panel_5_1_1.add(lblNumDescansoRestantes);

		btnPararEmpezar = new JButton("Empezar");
		btnPararEmpezar.setForeground(Color.BLACK);
		btnPararEmpezar.setBackground(new Color(139, 0, 0));
		btnPararEmpezar.setBounds(291, 528, 90, 53);
		contentPane.add(btnPararEmpezar);

		lblImagenEjercicio = new JLabel("");
		lblImagenEjercicio.setBounds(10, 379, 174, 117);
		contentPane.add(lblImagenEjercicio);

	}

	public JButton getBtnTerminar() {
		return btnTerminar;
	}

	public JButton getBtnPararEmpezar() {
		return btnPararEmpezar;
	}

	public JLabel getLblTWorkout() {
		return lblTWorkout;
	}

	public JLabel getLblTSerie() {
		return lblTSerie;
	}

	public JLabel getLblTDescanso() {
		return lblTDescanso;
	}

	public JLabel getLblTEjercicio() {
		return lblTEjercicio;
	}

	public void setNombreWorkout(String nombre) {
		String texto = "";
		if (nombre != null) {
			texto = nombre;
		}
		this.lblNombreWorkout.setText("Entrenamiento: " + texto);
	}

	public void setNombreEjercicio(String nombre) {
		String texto = "";
		if (nombre != null) {
			texto = nombre;
		}
		this.lblNombreEjercicio.setText("Ejercicio: " + texto);
	}

	public void setDescripcionEjercicio(String desc) {
		String texto = "";
		if (desc != null) {
			texto = desc;
		}
		this.lblDescripcionEjercicio.setText("Descripción: " + texto);
	}

	public void setImagenEjercicio(javax.swing.ImageIcon icon) {
		if (icon != null) {
			lblImagenEjercicio.setIcon(icon);
			lblImagenEjercicio.setText("");
		} else {
			lblImagenEjercicio.setIcon(null);
			lblImagenEjercicio.setText("Imagen no disponible");
		}
	}

	public void setSeriesRestantes(int n) {
		this.lblNumSeriesRestantes.setText("Series restantes: " + n);
	}

	public void setDescansosRestantes(int n) {
		this.lblNumDescansoRestantes.setText("Descansos restantes: " + n);
	}

	public void setTextoTiempoSerie(String t) {
		this.lblTSerie.setText(t);
	}

	public void setTextoTiempoEjercicio(String t) {
		this.lblTEjercicio.setText(t);
	}

}