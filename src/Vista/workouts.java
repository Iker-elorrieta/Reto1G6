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
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import javax.swing.ListSelectionModel;

public class workouts extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnVolver;
	private JTable tableWorkouts;

	// UI constants
	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String TXT_TITULO = "WORKOUTS";
	private final String BTN_VOLVER_TXT = "Volver";
	private final String[] TABLE_HEADERS = new String[] { "Nombre", "Nivel", "Duración", "Video" };

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					workouts frame = new workouts();
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
	public workouts() {
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
		
		JLabel lblWorkouts = new JLabel(TXT_TITULO);
		lblWorkouts.setForeground(Color.WHITE);
		lblWorkouts.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblWorkouts.setBackground(Color.WHITE);
		lblWorkouts.setBounds(349, 67, 179, 35);
		contentPane.add(lblWorkouts);
		
		
		btnVolver = new JButton(BTN_VOLVER_TXT);
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(50, 508, 119, 41);
		contentPane.add(btnVolver);
		
		JScrollPane scrollPaneWorkouts = new JScrollPane();
		scrollPaneWorkouts.setBounds(50, 260, 794, 158);
		contentPane.add(scrollPaneWorkouts);
		
		tableWorkouts = new JTable();
		tableWorkouts.setBorder(new LineBorder(new Color(128, 0, 0), 3, true));
		tableWorkouts.setModel(new DefaultTableModel(new Object[][] {
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
				{null, null, null, null},
			}, TABLE_HEADERS) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false; 
			}
		});
		
		tableWorkouts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableWorkouts.getColumnModel().getColumn(0).setPreferredWidth(288);
		tableWorkouts.getColumnModel().getColumn(1).setPreferredWidth(46);
		tableWorkouts.getColumnModel().getColumn(2).setPreferredWidth(59);
		tableWorkouts.getColumnModel().getColumn(3).setPreferredWidth(229);
		scrollPaneWorkouts.setViewportView(tableWorkouts);

	}
	public JButton getBtnVolver() { return btnVolver; }
	public JTable getTableWorkouts() { return tableWorkouts; }
}