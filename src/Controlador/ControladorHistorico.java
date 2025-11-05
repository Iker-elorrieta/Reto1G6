package Controlador;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import LecturaPB.lectura;
import Modelo.Workout;
import conexion.Conexion;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.api.core.ApiFuture;

import Modelo.Usuario;
import Vista.historicowo;

public class ControladorHistorico {

	private final historicowo vista;
	private final Usuario usuario;
	private final lectura lector = new lectura();

	private final DateTimeFormatter fmtFecha = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())
			.withZone(ZoneId.systemDefault());

	public ControladorHistorico(final historicowo vista, final Usuario usuario) {
		this.vista = vista;
		this.usuario = usuario;
		inicializar();
	}

	private void inicializar() {
		this.vista.getBtnVolver().addActionListener(e -> {
			try {
				this.vista.setVisible(false);
				this.vista.dispose();
			} catch (Exception ex) {
			}
		});
		cargarHistorico();
	}

	private void cargarHistorico() {
		try {
			List<Map<String, String>> listaRaw = new ArrayList<>();

			com.google.cloud.firestore.Firestore co = Conexion.conectar();
			if (co != null) {
				try {
					ApiFuture<QuerySnapshot> q = co.collection("usuarios").document(usuario.getEmail())
							.collection("historico_workouts").get();
					QuerySnapshot qs = q.get();
					for (QueryDocumentSnapshot doc : qs.getDocuments()) {
						Map<String, String> m = new HashMap<>();
						Object idw = doc.get("id_workout");
						m.put("id_workout", idw == null ? doc.getId() : String.valueOf(idw));
						Object ejercs = doc.get("ejercicios_hechos");
						m.put("ejercicios_hechos", ejercs == null ? "0" : String.valueOf(ejercs));
						Object tiempo = doc.get("tiempo");
						m.put("tiempo", tiempo == null ? "0" : String.valueOf(tiempo));
						Object fecha = doc.get("fecha");
						m.put("fecha", fecha == null ? "" : String.valueOf(fecha));
						m.put("usuario_id", usuario.getEmail());
						listaRaw.add(m);
					}
				} catch (Exception ex) {
					listaRaw = lector.leerHistoricoDesdeXML();
				} finally {
					try {
						co.close();
					} catch (Exception ex) {
					}
				}
			} else {
				listaRaw = lector.leerHistoricoDesdeXML();
			}

			List<Map<String, String>> listaFiltrada = new ArrayList<>();
			for (Map<String, String> m : listaRaw) {
				String uid = m.getOrDefault("usuario_id", "");
				if (uid == null)
					uid = "";
				if (uid.equalsIgnoreCase(usuario.getEmail()))
					listaFiltrada.add(m);
			}

			List<Workout> listaWorkouts = new Workout().mObtenerWorkouts();
			Map<String, Workout> mapaWork = new HashMap<>();
			if (listaWorkouts != null) {
				for (Workout w : listaWorkouts) {
					if (w != null && w.getNombre() != null)
						mapaWork.put(w.getNombre(), w);
				}
			}

			List<ItemHistorico> items = new ArrayList<>();
			for (Map<String, String> m : listaFiltrada) {
				try {
					String fechaStr = m.getOrDefault("fecha", "");
					Instant inst = null;
					if (!fechaStr.isEmpty()) {
						try {
							inst = Instant.parse(fechaStr);
						} catch (Exception e) {
							try {
								long millis = Long.parseLong(fechaStr);
								inst = Instant.ofEpochMilli(millis);
							} catch (Exception ex) {
								inst = null;
							}
						}
					}
					String idw = m.getOrDefault("id_workout", "");
					int ejercs = 0;
					try {
						ejercs = Integer.parseInt(m.getOrDefault("ejercicios_hechos", "0"));
					} catch (Exception ex) {
						ejercs = 0;
					}
					int tiempoSegs = 0;
					try {
						tiempoSegs = Integer.parseInt(m.getOrDefault("tiempo", "0"));
					} catch (Exception ex) {
						tiempoSegs = 0;
					}

					double durMin = 0.0;
					if (mapaWork.containsKey(idw))
						durMin = mapaWork.get(idw).getDuracionMinutos();

					int porcentaje = 0;
					if (durMin > 0.0)
						porcentaje = (int) Math.round((tiempoSegs / (durMin * 60.0)) * 100.0);

					items.add(new ItemHistorico(inst, tiempoSegs, idw, ejercs, porcentaje));
				} catch (Exception ex) {
					// entrada inválida, ignorar
				}
			}

			items.sort(Comparator.comparing(ItemHistorico::getFechaInstant,
					Comparator.nullsLast(Comparator.reverseOrder())));

			DefaultTableModel model = (DefaultTableModel) this.vista.getTableWorkouts().getModel();
			String[] cabeceras = new String[] { "Fecha", "Tiempo", "Workout", "Ejercicios", "Porcentaje" };
			model.setColumnIdentifiers(cabeceras);
			model.setRowCount(0);
			for (ItemHistorico it : items) {
				String fechaForm = (it.getFechaInstant() == null) ? "" : fmtFecha.format(it.getFechaInstant());
				String tiempoForm = formatTiempo(it.getTiempoSegs());
				String nombreWo = it.getIdWorkout();
				Object[] row = new Object[] { fechaForm, tiempoForm, nombreWo, it.getEjercicios(),
						it.getPorcentaje() + "%" };
				model.addRow(row);
			}

			if (model.getRowCount() == 0)
				JOptionPane.showMessageDialog(null, "No hay histórico para este usuario", "Histórico vacío",
						JOptionPane.INFORMATION_MESSAGE);

		} catch (Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error cargando histórico: " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private String formatTiempo(int segundos) {
		if (segundos <= 0)
			return "00:00";
		int horas = segundos / 3600;
		int minutos = (segundos % 3600) / 60;
		int secs = segundos % 60;
		if (horas > 0)
			return String.format("%02d:%02d:%02d", horas, minutos, secs);
		return String.format("%02d:%02d", minutos, secs);
	}

	private static class ItemHistorico {
		private final Instant fechaInstant;
		private final int tiempoSegs;
		private final String idWorkout;
		private final int ejercicios;
		private final int porcentaje;

		ItemHistorico(Instant fechaInstant, int tiempoSegs, String idWorkout, int ejercicios, int porcentaje) {
			this.fechaInstant = fechaInstant;
			this.tiempoSegs = tiempoSegs;
			this.idWorkout = idWorkout;
			this.ejercicios = ejercicios;
			this.porcentaje = porcentaje;
		}

		public Instant getFechaInstant() {
			return fechaInstant;
		}

		public int getTiempoSegs() {
			return tiempoSegs;
		}

		public String getIdWorkout() {
			return idWorkout;
		}

		public int getEjercicios() {
			return ejercicios;
		}

		public int getPorcentaje() {
			return porcentaje;
		}
	}
}