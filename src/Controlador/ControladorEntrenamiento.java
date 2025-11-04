package Controlador;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import com.google.cloud.firestore.DocumentSnapshot;
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
    private boolean detenerSolicitado = false;

    private CronometroThread cronoWorkout = null;
    private CronometroThread cronoActual = null; // cronómetro activo (serie o descanso)
    private CronometroThread cronoPorEjercicio = null; // cronómetro progresivo por ejercicio
    private Thread hiloSecuencia = null;

    // Contador de ejercicios completados accesible desde listener
    private int ejerciciosCompletados = 0;
    // Bandera para evitar guardar histórico más de una vez
    private boolean historicoGuardado = false;

    public ControladorEntrenamiento(entrenamiento vista, List<Ejercicio> ejercicios, String workoutId, Usuario usuario,
            workouts vwPrev, ejercicios vePrev) {
        this.vista = vista;
        if (ejercicios != null) {
            this.ejercicios = ejercicios;
        } else {
            this.ejercicios = new ArrayList<Ejercicio>();
        }
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
                // Deshabilitar botón para evitar doble pulsación
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try { vista.getBtnTerminar().setEnabled(false); } catch (Exception ex) {}
                    }
                });
               
                manejarTerminar();
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
        if (!cronoWorkout.estaEnEjecucion()) {
            // reanudar general y crono actual si existe
            cronoWorkout.iniciar();
            if (cronoActual != null) cronoActual.iniciar();
            if (cronoPorEjercicio != null) cronoPorEjercicio.iniciar();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.getBtnPararEmpezar().setText("Pausar");
                }
            });
        } else {
            // pausar general y crono actual si existe
            cronoWorkout.pausar();
            if (cronoActual != null) cronoActual.pausar();
            if (cronoPorEjercicio != null) cronoPorEjercicio.pausar();
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    vista.getBtnPararEmpezar().setText("Empezar");
                }
            });
        }
    }

  
    private long parsearTiempoLabel(String texto) {
        if (texto == null) return 0L;
        try {
        	// ESTOS JAVA UTIL REGEX SON PARA poner correctante el tiempo (ia)
            java.util.regex.Pattern p3 = java.util.regex.Pattern.compile("(\\d{1,2}:\\d{2}:\\d{2})");
            java.util.regex.Matcher m3 = p3.matcher(texto);
            if (m3.find()) {
                String t = m3.group(1);
                String[] partes = t.split(":");
                int h = Integer.parseInt(partes[0]);
                int m = Integer.parseInt(partes[1]);
                int s = Integer.parseInt(partes[2]);
                return ((h * 3600) + (m * 60) + s) * 1000L;
            }
            java.util.regex.Pattern p2 = java.util.regex.Pattern.compile("(\\d{1,3}:\\d{2})");
            java.util.regex.Matcher m2 = p2.matcher(texto);
            if (m2.find()) {
                String t = m2.group(1);
                String[] partes = t.split(":");
                int m = Integer.parseInt(partes[0]);
                int s = Integer.parseInt(partes[1]);
                return ((m * 60) + s) * 1000L;
            }
        } catch (Exception e) {
          
        }
        return 0L;
    }

    // Al pulsar Terminar: mostrar mensaje inmediatamente y guardar en background
    private void manejarTerminar() {
        detenerSolicitado = true;
        synchronized (ControladorEntrenamiento.this) {
            ControladorEntrenamiento.this.notifyAll();
        }
        if (cronoWorkout != null) cronoWorkout.detener();
        if (cronoActual != null) cronoActual.detener();
        if (cronoPorEjercicio != null) cronoPorEjercicio.detener();

        // Obtener tiempo total (buscar patrón en etiqueta si es necesario)
        long tiempoTotalMillis = 0;
        try {
            if (cronoWorkout != null) {
                tiempoTotalMillis = cronoWorkout.obtenerMilisTranscurridos();
            }
        } catch (Exception ex) {
            tiempoTotalMillis = 0;
        }
        if (tiempoTotalMillis <= 0) {
            try {
                String texto = vista.getLblTWorkout().getText();
                tiempoTotalMillis = parsearTiempoLabel(texto);
            } catch (Exception ex) {
                tiempoTotalMillis = 0;
            }
        }

        // Calcular porcentaje usando suma local de ejercicios
        double totalWorkoutSegsLocal = 0.0;
        if (ejercicios != null) {
            for (Ejercicio e : ejercicios) {
                totalWorkoutSegsLocal += e.getDuracionMinutos() * 60.0;
            }
        }
        double porcentajeLocal = 0.0;
        if (totalWorkoutSegsLocal > 0) {
            porcentajeLocal = (tiempoTotalMillis / 1000.0) / totalWorkoutSegsLocal * 100.0;
        }
        final int porcentajeEnteroLocal = (int) Math.round(porcentajeLocal);

        

      
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JOptionPane.showMessageDialog(null, "Felicidades, haz completado el " + porcentajeEnteroLocal + "% del workout", "Entrenamiento terminado", JOptionPane.INFORMATION_MESSAGE);
            }
        });

      
        final long tiempoParaGuardar = tiempoTotalMillis;
        final int porcentajeMostrado = porcentajeEnteroLocal;
        new Thread(new Runnable() {
            @Override
            public void run() {
                guardarHistorico(ejerciciosCompletados, tiempoParaGuardar);

                double totalWorkoutSegsOficial = 0.0;
                try {
                    if (workoutId != null && !workoutId.isEmpty()) {
                        Firestore co = Conexion.conectar();
                        if (co != null) {
                            try {
                                DocumentSnapshot wd = co.collection("workouts").document(workoutId).get().get();
                                if (wd != null && wd.exists()) {
                                    Object val = wd.get("duracionMinutos");
                                    if (val == null) val = wd.get("Duracion");
                                    if (val == null) val = wd.get("duracion");
                                    if (val instanceof Number) {
                                        totalWorkoutSegsOficial = ((Number) val).doubleValue() * 60.0;
                                    } else if (val instanceof String) {
                                        try {
                                            double dv = Double.parseDouble(((String) val).trim());
                                            totalWorkoutSegsOficial = dv * 60.0;
                                        } catch (Exception e) {
                                            totalWorkoutSegsOficial = 0.0;
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                // ignore
                            } finally {
                                try { co.close(); } catch (Exception ex) {}
                            }
                        }
                    }
                } catch (Exception e) {
                    totalWorkoutSegsOficial = 0.0;
                }

                if (totalWorkoutSegsOficial <= 0.0) {
                    double suma = 0.0;
                    if (ejercicios != null) {
                        for (Ejercicio e : ejercicios) {
                            suma += e.getDuracionMinutos() * 60.0;
                        }
                    }
                    totalWorkoutSegsOficial = suma;
                }

                double porcentajeOficial = 0.0;
                if (totalWorkoutSegsOficial > 0) {
                    porcentajeOficial = (tiempoParaGuardar / 1000.0) / totalWorkoutSegsOficial * 100.0;
                }
                final int porcentajeEnteroOficial = (int) Math.round(porcentajeOficial);


                if (porcentajeEnteroOficial != porcentajeMostrado) {
                    SwingUtilities.invokeLater(new Runnable() {
                       
                        public void run() {
                            JOptionPane.showMessageDialog(null, "Actualización: tu porcentaje real completado es " + porcentajeEnteroOficial + "%", "Porcentaje actualizado", JOptionPane.INFORMATION_MESSAGE);
                        }
                    });
                }
            }
        }, "GuardarHistorico-Background").start();
    }

    private void ejecutarSecuencia() {
        long tiempoTotalMillis = 0;

        try {
            cronoWorkout = new CronometroThread(vista.getLblTWorkout());
            cronoWorkout.start();

            for (Ejercicio ej : ejercicios) {
                if (detenerSolicitado) { return; }

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vista.setNombreEjercicio(ej.getNombre());
                        vista.setDescripcionEjercicio(ej.getDescripcion());
                        vista.setImagenEjercicio(ej.getImagen());
                        vista.setTextoTiempoSerie("00:00");
                        vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins");
                    }
                });

                List<Serie> listaSeries = new Serie().mObtenerSeries(workoutId, ej.getNombre());
                int restantes = 0;
                if (listaSeries != null) {
                    for (Serie ss : listaSeries) {
                        restantes += ss.getCantidad();
                    }
                }
                final int restantesFinal = restantes;
                final int[] descansosRestantes = new int[] { restantesFinal };
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vista.setSeriesRestantes(restantesFinal);
                        vista.setDescansosRestantes(descansosRestantes[0]);
                    }
                });

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
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins");
                    }
                });

                if (cronoPorEjercicio != null) {
                    cronoPorEjercicio.detener();
                    cronoPorEjercicio = null;
                }
                cronoPorEjercicio = new CronometroThread(vista.getLblTEjercicio(), "Tiempo ejercicio:");
                cronoPorEjercicio.start();

                if (listaSeries != null) {
                    for (Serie s : listaSeries) {
                        if (detenerSolicitado) { return; }
                        int cantidad = Math.max(0, s.getCantidad());
                        int tiempoSerieSegs = s.getTiempo_serie();
                        int tiempoDescSegs = s.getTiempo_descanso();

                        for (int rep = 0; rep < cantidad; rep++) {
                            if (detenerSolicitado) { return; }

                            while (!detenerSolicitado && (cronoWorkout == null || !cronoWorkout.estaEnEjecucion())) {
                                try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
                            }
                            if (detenerSolicitado) { return; }

                            if (cronoPorEjercicio != null && !cronoPorEjercicio.estaEnEjecucion() && cronoWorkout.estaEnEjecucion()) {
                                cronoPorEjercicio.iniciar();
                            }

                            restantes--;
                            final int rem = restantes;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    vista.setSeriesRestantes(rem);
                                }
                            });

                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    vista.setTextoTiempoSerie(formatSegundos(tiempoSerieSegs));
                                }
                            });

                            final Object monitor = new Object();
                            final boolean[] terminado = new boolean[] { false };

                            CronometroThread cronoSerie = new CronometroThread(vista.getLblTSerie(), tiempoSerieSegs * 1000L);
                            cronoActual = cronoSerie;
                            cronoSerie.setListener(new CronometroThread.CronometroListener() {
                                @Override
                                public void terminado() {
                                    synchronized (monitor) {
                                        terminado[0] = true;
                                        monitor.notifyAll();
                                    }
                                }
                            });
                            cronoSerie.start();
                            if (cronoWorkout.estaEnEjecucion()) {
                                cronoSerie.iniciar();
                            } else {
                                while (!detenerSolicitado && !cronoWorkout.estaEnEjecucion()) {
                                    try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
                                }
                                if (detenerSolicitado) { return; }
                                cronoSerie.iniciar();
                            }

                            synchronized (monitor) {
                                while (!terminado[0] && !detenerSolicitado) {
                                    try {
                                        monitor.wait(250);
                                    } catch (InterruptedException ie) {
                                        Thread.currentThread().interrupt();
                                        return;
                                    }
                                }
                            }

                            cronoSerie.detener();
                            cronoActual = null;

                            if (detenerSolicitado) { return; }

                            tiempoRestanteEjercicio[0] -= tiempoSerieSegs;
                            SwingUtilities.invokeLater(new Runnable() {
                                @Override
                                public void run() {
                                    vista.setTextoTiempoSerie("00:00");
                                }
                            });
                            if (tiempoDescSegs > 0 && rep < (cantidad - 1)) {
                                final Object monitorDesc = new Object();
                                final boolean[] terminadoDesc = new boolean[] { false };

                                CronometroThread cronoDesc = new CronometroThread(vista.getLblTDescanso(), tiempoDescSegs * 1000L);
                                cronoActual = cronoDesc;
                                cronoDesc.setListener(new CronometroThread.CronometroListener() {
                                    @Override
                                    public void terminado() {
                                        synchronized (monitorDesc) {
                                            terminadoDesc[0] = true;
                                            monitorDesc.notifyAll();
                                        }
                                    }
                                });
                                cronoDesc.start();
                                if (cronoWorkout.estaEnEjecucion()) {
                                    cronoDesc.iniciar();
                                } else {
                                    while (!detenerSolicitado && !cronoWorkout.estaEnEjecucion()) {
                                        try { Thread.sleep(100); } catch (InterruptedException ie) { Thread.currentThread().interrupt(); return; }
                                    }
                                    if (detenerSolicitado) { return; }
                                    cronoDesc.iniciar();
                                }

                                synchronized (monitorDesc) {
                                    while (!terminadoDesc[0] && !detenerSolicitado) {
                                        try {
                                            monitorDesc.wait(250);
                                        } catch (InterruptedException ie) {
                                            Thread.currentThread().interrupt();
                                            return;
                                        }
                                    }
                                }
                                cronoDesc.detener();
                                cronoActual = null;

                                // disminuir contador de descansos restantes y actualizar 
                                descansosRestantes[0] = Math.max(0, descansosRestantes[0] - 1);
                                final int cr = descansosRestantes[0];
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        vista.setDescansosRestantes(cr);
                                    }
                                });
                                tiempoRestanteEjercicio[0] -= tiempoDescSegs;
                            } else {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        vista.setDescansosRestantes(Math.max(0, descansosRestantes[0] - 1));
                                    }
                                });
                            }
                         }
                         if (detenerSolicitado) { return; }
                     }
                 }

                 if (!detenerSolicitado) ejerciciosCompletados++;

                 if (cronoPorEjercicio != null) {
                     cronoPorEjercicio.detener();
                     cronoPorEjercicio = null;
                 }

                 SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        vista.setTextoTiempoSerie("00:00");
                        vista.setTextoTiempoEjercicio("Tiempo ejercicio: 00:00 mins");
                        vista.setDescansosRestantes(0);
                    }
                 });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            // parar cronómetro general
            if (cronoWorkout != null) {
                tiempoTotalMillis = cronoWorkout.obtenerMilisTranscurridos();
                cronoWorkout.detener();
            } else {
                // intentar parsear label si cronoWorkout es null
                try {
                    String texto = vista.getLblTWorkout().getText();
                    if (texto != null && !texto.isEmpty()) {
                        java.util.regex.Pattern p = java.util.regex.Pattern.compile("(\\d{1,2}:\\d{2})");
                        java.util.regex.Matcher m = p.matcher(texto);
                        if (m.find()) {
                            String tiempo = m.group(1);
                            String[] minseg = tiempo.split(":");
                            int min = Integer.parseInt(minseg[0]);
                            int seg = Integer.parseInt(minseg[1]);
                            tiempoTotalMillis = (min * 60 + seg) * 1000L;
                        }
                    }
                } catch (Exception e) {
                    tiempoTotalMillis = 0;
                }
            }

            // guardar histórico
            guardarHistorico(ejerciciosCompletados, tiempoTotalMillis);

            // volver a la vista workouts
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // sincronizado para evitar doble guardado concurrente
    private synchronized void guardarHistorico(int ejerciciosHechos, long tiempoMillis) {
        if (historicoGuardado) return;
        if (usuario == null) return;
        try {
            System.out.println("Guardar historico: ejercicios=" + ejerciciosHechos + " tiempoSegs=" + Math.round(tiempoMillis/1000.0));
            Firestore co = Conexion.conectar();
            if (co == null) return;
            Map<String,Object> doc = new HashMap<>();
            String idWo = "";
            if (workoutId != null) idWo = workoutId;
            doc.put("id_workout", idWo);
            doc.put("ejercicios_hechos", ejerciciosHechos);
            doc.put("tiempo", Math.round(tiempoMillis / 1000.0)); // guardar en segundos
            doc.put("fecha", java.time.Instant.now().toString());

            co.collection("usuarios").document(usuario.getEmail()).collection("historico_workouts").add(doc).get();
            co.close();
            historicoGuardado = true;
            System.out.println("Historico guardado OK");
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
