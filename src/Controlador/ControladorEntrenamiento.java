package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.swing.SwingUtilities;

import com.google.cloud.firestore.Firestore;

import Modelo.CronometroThread;
import Modelo.Ejercicio;
import Modelo.Serie;
import Modelo.Usuario;
import conexion.Conexion;
import Vista.entrenamiento;
import Vista.workouts;
import Vista.ejercicios;

public class ControladorEntrenamiento {

    private final entrenamiento vista;
    private final List<Ejercicio> ejercicios;
    private final String workoutId;
    private final Usuario usuario;
    private final workouts vistaWorkoutsPrev;
    private final ejercicios vistaEjerciciosPrev;

    private volatile boolean stopRequested = false;
    private volatile CountDownLatch espera = null;

    private CronometroThread cronoWorkout = null;
    private volatile CronometroThread currentCrono = null; // cronómetro activo (serie o descanso)
    private volatile CronometroThread cronoEjercicio = null; // cronómetro progresivo por ejercicio
    private Thread hiloSecuencia = null;

    public ControladorEntrenamiento(entrenamiento vista, List<Ejercicio> ejercicios, String workoutId, Usuario usuario,
            workouts vwPrev, ejercicios vePrev) {
        this.vista = vista;
        this.ejercicios = (ejercicios != null) ? ejercicios : new ArrayList<Ejercicio>();
        this.workoutId = workoutId;
        this.usuario = usuario;
        this.vistaWorkoutsPrev = vwPrev;
        this.vistaEjerciciosPrev = vePrev;

        inicializar();
    }

    private void inicializar() {
        // listeners botones
        this.vista.getBtnPararEmpezar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarBotonEmpezarPausar();
            }
        });

        this.vista.getBtnTerminar().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stopRequested = true;
                // desbloquear espera si existe
                if (espera != null) espera.countDown();
                // detener cronómetro del workout y cronómetro actual
                if (cronoWorkout != null) cronoWorkout.detener();
                if (currentCrono != null) currentCrono.detener();
                if (cronoEjercicio != null) cronoEjercicio.detener();
            }
        });

        // iniciar hilo del entrenamiento
        hiloSecuencia = new Thread(new Runnable() {
            @Override
            public void run() {
                ejecutarSecuencia();
            }
        }, "Hilo-Entrenamiento");
        hiloSecuencia.start();
    }

    private void manejarBotonEmpezarPausar() {
        if (cronoWorkout == null) return;
        if (!cronoWorkout.isEnEjecucion()) {
            // reanudar general y crono actual si existe
            cronoWorkout.iniciar();
            if (currentCrono != null) currentCrono.iniciar();
            if (cronoEjercicio != null) cronoEjercicio.iniciar();
            SwingUtilities.invokeLater(() -> vista.getBtnPararEmpezar().setText("Pausar"));
        } else {
            // pausar general y crono actual si existe
            cronoWorkout.pausar();
            if (currentCrono != null) currentCrono.pausar();
            if (cronoEjercicio != null) cronoEjercicio.pausar();
            SwingUtilities.invokeLater(() -> vista.getBtnPararEmpezar().setText("Empezar"));
        }
    }

    private void ejecutarSecuencia() {
        int ejerciciosCompletados = 0;
        long tiempoTotalMillis = 0;

        try {
            // iniciar cronómetro general (stopwatch) pero no arrancar series hasta que el usuario pulse Empezar
            cronoWorkout = new CronometroThread(vista.getLblTWorkout());
            cronoWorkout.start();
            // dejamos cronoWorkout sin iniciar para esperar al usuario

            // recorrer ejercicios
            for (Ejercicio ej : ejercicios) {
                if (stopRequested) break;

                SwingUtilities.invokeLater(() -> {
                    vista.setNombreEjercicio(ej.getNombre());
                    vista.setDescripcionEjercicio(ej.getDescripcion());
                    vista.setImagenEjercicio(ej.getImagen());
                    // reset labels para nuevo ejercicio
                    vista.setTextoTiempoSerie("00:00");
                    vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins");
                });

                // obtener series reales para este ejercicio
                List<Serie> listaSeries = new Serie().mObtenerSeries(workoutId, ej.getNombre());
                int restantes = 0;
                if (listaSeries != null) {
                    for (Serie ss : listaSeries) {
                        restantes += ss.getCantidad();
                    }
                }
                final int restantesFinal = restantes;
                // Inicializar contador de series y descansos (descanso obligatorio tras cada repetición)
                final int[] descansosRestantes = new int[] { restantesFinal };
                SwingUtilities.invokeLater(() -> {
                    vista.setSeriesRestantes(restantesFinal);
                    vista.setDescansosRestantes(descansosRestantes[0]);
                });

                // calcular tiempo total del ejercicio (segundos)
                int tiempoTotalEjercicioSegs = 0;
                if (listaSeries != null) {
                    for (Serie ss : listaSeries) {
                        int cant = ss.getCantidad();
                        int tserie = ss.getTiempo_serie();
                        int tdesc = ss.getTiempo_descanso();
                        if (cant > 0) {
                            tiempoTotalEjercicioSegs += cant * tserie;
                            if (cant > 1) tiempoTotalEjercicioSegs += (cant - 1) * tdesc;
                        }
                    }
                }
                final int[] tiempoRestanteEjercicio = new int[] { tiempoTotalEjercicioSegs };
                SwingUtilities.invokeLater(() -> vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins"));

                // preparar y arrancar crono por ejercicio (stopwatch) — se inicia cuando el usuario pulse Empezar
                if (cronoEjercicio != null) {
                    cronoEjercicio.detener();
                    cronoEjercicio = null;
                }
                cronoEjercicio = new CronometroThread(vista.getLblTEjercicio(), "Tiempo ejercicio:");
                cronoEjercicio.start();

                // para cada serie: iterar por cantidad (repeticiones) y manejar descanso entre repeticiones
                if (listaSeries != null) {
                    for (Serie s : listaSeries) {
                        if (stopRequested) break;
                        int cantidad = Math.max(0, s.getCantidad());
                        int tiempoSerieSegs = s.getTiempo_serie();
                        int tiempoDescSegs = s.getTiempo_descanso();

                        for (int rep = 0; rep < cantidad; rep++) {
                            if (stopRequested) break;

                            // Antes de arrancar la serie, esperar a que el usuario pulse Empezar (cronoWorkout en ejecución)
                            while (!stopRequested && (cronoWorkout == null || !cronoWorkout.isEnEjecucion())) {
                                try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
                            }
                            if (stopRequested) break;

                            // iniciar crono de ejercicio si no está en ejecución
                            if (cronoEjercicio != null && !cronoEjercicio.isEnEjecucion() && cronoWorkout.isEnEjecucion()) {
                                cronoEjercicio.iniciar();
                            }

                            // actualizar series restantes (una menos por cada repetición)
                            restantes--;
                            final int rem = restantes;
                            SwingUtilities.invokeLater(() -> vista.setSeriesRestantes(rem));

                            // mostrar tiempo por serie en label
                            SwingUtilities.invokeLater(() -> vista.setTextoTiempoSerie(formatSegundos(tiempoSerieSegs)));

                            // crono de serie (cuenta atrás)
                            CountDownLatch latchSerie = new CountDownLatch(1);
                            espera = latchSerie;
                            CronometroThread cronoSerie = new CronometroThread(vista.getLblTSerie(), tiempoSerieSegs * 1000L);
                            currentCrono = cronoSerie;
                            cronoSerie.setListener(new CronometroThread.CronometroListener() {
                                @Override
                                public void terminado() {
                                    latchSerie.countDown();
                                }
                            });
                            cronoSerie.start();
                            if (cronoWorkout.isEnEjecucion()) {
                                cronoSerie.iniciar();
                            } else {
                                while (!stopRequested && !cronoWorkout.isEnEjecucion()) {
                                    try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
                                }
                                if (!stopRequested) cronoSerie.iniciar();
                            }

                            // esperar fin de la repetición
                            while (true) {
                                if (stopRequested) {
                                    cronoSerie.detener();
                                    latchSerie.countDown();
                                    break;
                                }
                                try {
                                    if (latchSerie.await(250, java.util.concurrent.TimeUnit.MILLISECONDS)) break;
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                    break;
                                }
                            }
                            cronoSerie.detener();
                            currentCrono = null;
                            espera = null;

                            if (stopRequested) break;

                            // restar tiempo consumido de tiempoRestanteEjercicio
                            tiempoRestanteEjercicio[0] -= tiempoSerieSegs;
                            // resetear label de serie antes del descanso
                            SwingUtilities.invokeLater(() -> vista.setTextoTiempoSerie("00:00"));
                            if (tiempoDescSegs > 0 && rep < (cantidad - 1)) {
                                // iniciar descanso entre repeticiones
                                CountDownLatch latchDesc = new CountDownLatch(1);
                                espera = latchDesc;
                                CronometroThread cronoDesc = new CronometroThread(vista.getLblTDescanso(), tiempoDescSegs * 1000L);
                                currentCrono = cronoDesc;
                                cronoDesc.setListener(new CronometroThread.CronometroListener() {
                                    @Override
                                    public void terminado() {
                                        latchDesc.countDown();
                                    }
                                });
                                cronoDesc.start();
                                if (cronoWorkout.isEnEjecucion()) {
                                    cronoDesc.iniciar();
                                } else {
                                    while (!stopRequested && !cronoWorkout.isEnEjecucion()) {
                                        try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); break; }
                                    }
                                    if (!stopRequested) cronoDesc.iniciar();
                                }

                                while (true) {
                                    if (stopRequested) {
                                        cronoDesc.detener();
                                        latchDesc.countDown();
                                        break;
                                    }
                                    try {
                                        if (latchDesc.await(250, java.util.concurrent.TimeUnit.MILLISECONDS)) break;
                                    } catch (InterruptedException ie) {
                                        Thread.currentThread().interrupt();
                                        break;
                                    }
                                }
                                cronoDesc.detener();
                                currentCrono = null;
                                espera = null;

                                // disminuir contador de descansos restantes y actualizar UI
                                descansosRestantes[0] = Math.max(0, descansosRestantes[0] - 1);
                                final int cr = descansosRestantes[0];
                                SwingUtilities.invokeLater(() -> vista.setDescansosRestantes(cr));
                                tiempoRestanteEjercicio[0] -= tiempoDescSegs;
                            } else {
                                // si no hay tiempo de descanso definido, aun así consideramos que no queda descanso
                                SwingUtilities.invokeLater(() -> vista.setDescansosRestantes(Math.max(0, descansosRestantes[0] - 1)));
                            }

                            // actualizar label tiempo ejercicio
                            // no sobreescribir el label de tiempo ejercicio: lo gestiona cronoEjercicio (stopwatch)
                            // solo actualizamos series/descanso/serie labels en la UI
                         }
                         if (stopRequested) break;
                     }
                 }

                 // ejercicio completado
                 if (!stopRequested) ejerciciosCompletados++;

                 // detener y limpiar cronoEjercicio
                 if (cronoEjercicio != null) {
                     cronoEjercicio.detener();
                     cronoEjercicio = null;
                 }

                 // limpiar labels de serie/descanso antes del siguiente ejercicio
                 SwingUtilities.invokeLater(() -> {
                     vista.setTextoTiempoSerie("00:00");
                     vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins");
                     vista.setDescansosRestantes(0);
                 });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // parar cronómetro general
            if (cronoWorkout != null) {
                tiempoTotalMillis = cronoWorkout.obtenerMilisTranscurridos();
                cronoWorkout.detener();
            }

            // guardar histórico en Firestore
            guardarHistorico(ejerciciosCompletados, tiempoTotalMillis);

            // volver a la vista workouts
            try {
                SwingUtilities.invokeLater(() -> {
                    try {
                        if (vista != null) {
                            vista.setVisible(false);
                            vista.dispose();
                        }
                        if (vistaWorkoutsPrev != null) vistaWorkoutsPrev.setVisible(true);
                        if (vistaEjerciciosPrev != null) { vistaEjerciciosPrev.setVisible(false); vistaEjerciciosPrev.dispose(); }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void guardarHistorico(int ejerciciosHechos, long tiempoMillis) {
        if (usuario == null) return;
        try {
            Firestore co = Conexion.conectar();
            if (co == null) return;
            Map<String,Object> doc = new HashMap<>();
            doc.put("id_workout", workoutId == null ? "" : workoutId);
            doc.put("ejercicios_hechos", ejerciciosHechos);
            doc.put("tiempo", Math.round(tiempoMillis / 1000.0)); // guardar en segundos
            doc.put("fecha", java.time.Instant.now().toString());

            co.collection("usuarios").document(usuario.getEmail()).collection("historico_workouts").add(doc).get();
            co.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String formatSegundos(int segundos) {
        int min = segundos / 60;
        int sec = segundos % 60;
        return String.format("%02d:%02d", min, sec);
    }

    private String formatTiempoEjercicio(int tiempoSegs) {
        int horas = tiempoSegs / 3600;
        int minutos = (tiempoSegs % 3600) / 60;
        int segundos = tiempoSegs % 60;
        if (horas > 0) {
            return String.format("%02d:%02d:%02d", horas, minutos, segundos);
        } else {
            return String.format("%02d:%02d", minutos, segundos);
        }
    }
}