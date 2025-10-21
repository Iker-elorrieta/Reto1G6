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

public class ejercicios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JButton btnVolver;
	private JTable table;
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

		ImageIcon iconoOriginal = new ImageIcon("media/logo1.png");
		Image imagen = iconoOriginal.getImage();

		Image imagenEscalada = imagen.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
		lblLogo.setIcon(new ImageIcon(imagenEscalada));

		contentPane.add(lblLogo);
		
		JLabel lblEjercicios = new JLabel("EJERCICIOS");
		lblEjercicios.setForeground(Color.WHITE);
		lblEjercicios.setFont(new Font("Tahoma", Font.BOLD, 30));
		lblEjercicios.setBackground(Color.WHITE);
		lblEjercicios.setBounds(350, 87, 191, 35);
		contentPane.add(lblEjercicios);
		
		
		btnVolver = new JButton("Volver");
		btnVolver.setForeground(new Color(240, 248, 255));
		btnVolver.setFont(new Font("Tahoma", Font.BOLD, 16));
		btnVolver.setBackground(new Color(139, 0, 0));
		btnVolver.setBounds(72, 538, 119, 41);
		contentPane.add(btnVolver);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(72, 187, 753, 316);
		contentPane.add(scrollPane);
		
		table = new JTable();
		scrollPane.setColumnHeaderView(table);
		table.setBorder(new LineBorder(new Color(128, 0, 0), 4));
		table.setBackground(new Color(255, 204, 204));

	}
	public JButton getBtnVolver() { return btnVolver; }
}
