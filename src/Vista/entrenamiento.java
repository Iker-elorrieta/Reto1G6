package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Modelo.CronometroThread;

public class entrenamiento extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

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
        panel.setBounds(10, 187, 174, 53);
        contentPane.add(panel);
        panel.setLayout(null);

        JLabel lblTWorkout = new JLabel("Tiempo Workout: 00:00 mins");
        lblTWorkout.setForeground(Color.WHITE);
        lblTWorkout.setBounds(0, 0, 174, 53);
        panel.add(lblTWorkout);

        JButton btnTerminar = new JButton("Terminar");
        btnTerminar.setBackground(new Color(139, 0, 0));
        btnTerminar.setForeground(Color.BLACK);
        btnTerminar.setBounds(529, 466, 90, 53);
        contentPane.add(btnTerminar);

        JPanel panel_1 = new JPanel();
        panel_1.setLayout(null);
        panel_1.setBackground(new Color(139, 0, 0));
        panel_1.setBounds(10, 251, 174, 53);
        contentPane.add(panel_1);

        JLabel lblTEjercicio = new JLabel("Tiempo Ejercicio: 00:00 mins");
        lblTEjercicio.setForeground(Color.WHITE);
        lblTEjercicio.setBounds(0, 0, 174, 53);
        panel_1.add(lblTEjercicio);

        JPanel panel_3 = new JPanel();
        panel_3.setLayout(null);
        panel_3.setBackground(new Color(139, 0, 0));
        panel_3.setBounds(734, 251, 174, 53);
        contentPane.add(panel_3);

        JLabel lblNombreEjercicio = new JLabel("Ejercicio:");
        lblNombreEjercicio.setForeground(Color.WHITE);
        lblNombreEjercicio.setBounds(0, 0, 174, 53);
        panel_3.add(lblNombreEjercicio);

        JPanel panel_2 = new JPanel();
        panel_2.setLayout(null);
        panel_2.setBackground(new Color(139, 0, 0));
        panel_2.setBounds(734, 187, 174, 53);
        contentPane.add(panel_2);

        JLabel lblNombreWorkout = new JLabel("Workout:");
        lblNombreWorkout.setForeground(Color.WHITE);
        lblNombreWorkout.setBounds(0, 0, 174, 53);
        panel_2.add(lblNombreWorkout);

        JPanel panel_5 = new JPanel();
        panel_5.setLayout(null);
        panel_5.setBackground(new Color(139, 0, 0));
        panel_5.setBounds(734, 315, 174, 117);
        contentPane.add(panel_5);

        JLabel lblDescripcionEjercicio = new JLabel("Descripción:");
        lblDescripcionEjercicio.setVerticalAlignment(SwingConstants.TOP);
        lblDescripcionEjercicio.setForeground(Color.WHITE);
        lblDescripcionEjercicio.setBounds(0, 0, 174, 117);
        panel_5.add(lblDescripcionEjercicio);

        JPanel panel_5_1 = new JPanel();
        panel_5_1.setLayout(null);
        panel_5_1.setBackground(new Color(139, 0, 0));
        panel_5_1.setBounds(232, 187, 217, 245);
        contentPane.add(panel_5_1);

        JLabel lbl1 = new JLabel("TIEMPO SERIE");
        lbl1.setFont(new Font("Tahoma", Font.BOLD, 18));
        lbl1.setForeground(Color.WHITE);
        lbl1.setBounds(38, 11, 144, 27);
        panel_5_1.add(lbl1);

        JLabel lblTSerie = new JLabel("00:00");
        lblTSerie.setForeground(Color.WHITE);
        lblTSerie.setFont(new Font("Tahoma", Font.BOLD, 49));
        lblTSerie.setBounds(38, 56, 144, 119);
        panel_5_1.add(lblTSerie);

        JLabel lblNumSeriesRestantes = new JLabel("Series restantes: 0");
        lblNumSeriesRestantes.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNumSeriesRestantes.setBounds(48, 181, 114, 53);
        panel_5_1.add(lblNumSeriesRestantes);
        lblNumSeriesRestantes.setForeground(Color.WHITE);

        JPanel panel_5_1_1 = new JPanel();
        panel_5_1_1.setLayout(null);
        panel_5_1_1.setBackground(new Color(139, 0, 0));
        panel_5_1_1.setBounds(471, 187, 217, 245);
        contentPane.add(panel_5_1_1);

        JLabel lbl2 = new JLabel("  TIEMPO DESCANSO");
        lbl2.setForeground(Color.WHITE);
        lbl2.setFont(new Font("Tahoma", Font.BOLD, 18));
        lbl2.setBounds(10, 11, 197, 27);
        panel_5_1_1.add(lbl2);

        JLabel lblTDescanso = new JLabel("00:00");
        lblTDescanso.setForeground(Color.WHITE);
        lblTDescanso.setFont(new Font("Tahoma", Font.BOLD, 49));
        lblTDescanso.setBounds(35, 56, 144, 119);
        panel_5_1_1.add(lblTDescanso);

        JLabel lblNumDescansoRestantes = new JLabel("Descansos restantes: 0");
        lblNumDescansoRestantes.setForeground(Color.WHITE);
        lblNumDescansoRestantes.setFont(new Font("Tahoma", Font.PLAIN, 13));
        lblNumDescansoRestantes.setBounds(45, 181, 134, 53);
        panel_5_1_1.add(lblNumDescansoRestantes);

        JButton btnPararEmpezar = new JButton("Empezar");
        btnPararEmpezar.setForeground(Color.BLACK);
        btnPararEmpezar.setBackground(new Color(139, 0, 0));
        btnPararEmpezar.setBounds(291, 466, 90, 53);
        contentPane.add(btnPararEmpezar);

        JLabel lblImagenEjercicio = new JLabel("");
        lblImagenEjercicio.setBounds(10, 315, 174, 117);
        contentPane.add(lblImagenEjercicio);

        // 🔹 Aquí empieza la lógica del cronómetro
        final CronometroThread crono = new CronometroThread(lblTWorkout);
        crono.start();

        btnPararEmpezar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (!crono.isEnEjecucion()) {
                    crono.iniciar();
                    btnPararEmpezar.setText("Pausar");
                } else {
                    crono.pausar();
                    btnPararEmpezar.setText("Empezar");
                }
            }
        });

        // Botón "Terminar" para detener el hilo
        btnTerminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent e) {
                crono.detener();
                btnPararEmpezar.setEnabled(false);
            }
        });
    }
}
	