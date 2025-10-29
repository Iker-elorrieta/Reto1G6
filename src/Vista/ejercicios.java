package Vista;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTable;
import javax.swing.border.LineBorder;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class ejercicios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnVolver;
	private JTable table;
	private JLabel lblDescripcion;
	private JLabel lblImg;

	// UI constants
	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String TXT_TITULO = "EJERCICIOS";
	private final String BTN_VOLVER = "Volver";
	private final String[] TABLE_HEADERS = new String[] {"Nombre", "Duracion", "Series", "T. Descanso", "T. Serie"};

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ejercicios frame = new ejercicios();
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
	public ejercicios() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 934, 643);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(0, 0, 0));
		
		JLabel lblLogo = new JLabel("");
		lblLogo.setBounds(10, 11, 174, 165);

		ImageIcon iconoOriginal = new ImageIcon(IMG_LOGO_PATH);
		Image imagen = iconoOriginal.getImage();

		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));

		contentPane.add(lblLogo);
		
		JLabel lblEjercicios = new JLabel(TXT_TITULO);
		lblEjercicios.setForeground(Color.WHITE);
		lblEjercicios.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblEjercicios.setBackground(Color.WHITE);
		lblEjercicios.setBounds(350, 76, 191, 35);
		contentPane.add(lblEjercicios);
		
		
		btnVolver = new JButton(BTN_VOLVER);
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(72, 538, 119, 41);
		contentPane.add(btnVolver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(73, 187, 753, 75);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setBorder(new LineBorder(new Color(128, 0, 0), 3, true));
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			TABLE_HEADERS
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(220);
		table.getColumnModel().getColumn(1).setPreferredWidth(104);
		table.getColumnModel().getColumn(2).setPreferredWidth(108);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(91);
		scrollPane.setViewportView(table);
		
		lblDescripcion = new JLabel("");
		lblDescripcion.setForeground(new Color(240, 248, 255));
		lblDescripcion.setBounds(74, 292, 753, 75);
		contentPane.add(lblDescripcion);
		
		lblImg = new JLabel("");
		lblImg.setBounds(312, 355, 293, 224);
		contentPane.add(lblImg);

	}
	public JButton getBtnVolver() { return btnVolver; }
	public JTable getTableEjercicios() { return table; }
	public JLabel getLblDescripcion() { return lblDescripcion; }
	public JLabel getLblImg() { return lblImg; }
}