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

public class historicowo extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnVolver;
	private JTable tableHistorico;


	private final String IMG_LOGO_PATH = "media/logo1.png";
	private final String BTN_VOLVER_TXT = "Volver";
	private final String[] TABLE_HEADERS = new String[] { "Nombre", "Nivel", "Duración", "Video" };
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					historicowo frame = new historicowo();
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
	public historicowo() {
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
		
		JLabel lblHistorico = new JLabel("Historico");
		lblHistorico.setForeground(Color.WHITE);
		lblHistorico.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblHistorico.setBackground(Color.WHITE);
		lblHistorico.setBounds(349, 67, 179, 35);
		contentPane.add(lblHistorico);
		
		
		btnVolver = new JButton(BTN_VOLVER_TXT);
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(50, 508, 119, 41);
		contentPane.add(btnVolver);
		
		JScrollPane scrollPaneHistorico = new JScrollPane();
		scrollPaneHistorico.setBounds(50, 260, 794, 158);
		contentPane.add(scrollPaneHistorico);
		
		tableHistorico = new JTable();
		tableHistorico.setBorder(new LineBorder(new Color(128, 0, 0), 3, true));
		tableHistorico.setModel(new DefaultTableModel(new Object[][] {
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
		
		tableHistorico.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tableHistorico.getColumnModel().getColumn(0).setPreferredWidth(288);
		tableHistorico.getColumnModel().getColumn(1).setPreferredWidth(46);
		tableHistorico.getColumnModel().getColumn(2).setPreferredWidth(59);
		tableHistorico.getColumnModel().getColumn(3).setPreferredWidth(229);
		scrollPaneHistorico.setViewportView(tableHistorico);

	}
	public JButton getBtnVolver() { return btnVolver; }
	public JTable getTableWorkouts() { return tableHistorico; }
}