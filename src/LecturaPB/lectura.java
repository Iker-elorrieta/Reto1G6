package LecturaPB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Modelo.Usuario;
import Modelo.Workout;
import Modelo.Ejercicio;
import Modelo.Serie;
import Modelo.WorkoutCompleto;
import Modelo.EjercicioConSeries;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import conexion.Conexion;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class lectura {

	// rutas de backup (constantes de instancia)
	private final String FILE_USERS = "backups/usuario.dat";
	private final String FILE_WORKOUTS = "backups/workouts.dat";

	public lectura() {
	}

	public void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try {
			File dir = new File("backups");
			if (!dir.exists())
				dir.mkdirs();
			File f = new File(FILE_USERS);
			if (!f.exists())
				f.createNewFile();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				ArrayList<Usuario> listaAEscribir;
				if (usuarios != null) {
					listaAEscribir = usuarios;
				} else {
					listaAEscribir = new ArrayList<Usuario>();
				}
				oos.writeObject(listaAEscribir);
				System.out.println("Usuarios escrito");
			}
		} catch (IOException e) {
			System.err.println("Usuarios no escrito");
			e.printStackTrace();
		}
	}

	public void guardarWorkouts(ArrayList<Workout> workouts) {
		try {
			File dir = new File("backups");
			if (!dir.exists())
				dir.mkdirs();
			File f = new File(FILE_WORKOUTS);
			if (!f.exists())
				f.createNewFile();
			ArrayList<WorkoutCompleto> listaWC = new ArrayList<>();
			if (workouts != null) {
				for (Workout wot : workouts) {
					WorkoutCompleto wc = new WorkoutCompleto();
					wc.setWorkout(wot);
					ArrayList<Ejercicio> ejercs = new Ejercicio().mObtenerEjercicios(wot.getNombre());
					ArrayList<EjercicioConSeries> ecsList = new ArrayList<>();
					if (ejercs != null) {
						for (Ejercicio e : ejercs) {
							EjercicioConSeries ecs = new EjercicioConSeries();
							ecs.setNombre(e.getNombre());
							ecs.setDescripcion(e.getDescripcion());
							ecs.setImagen(e.getImagen());
							ArrayList<Serie> series = new Serie().mObtenerSeries(wot.getNombre(), e.getNombre());
							if (series == null)
								series = new ArrayList<>();
							ecs.setSeries(series);
							ecsList.add(ecs);
						}
					}
					wc.setEjercicios(ecsList);
					listaWC.add(wc);
				}
			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				// escribir la lista completa de WorkoutCompleto
				oos.writeObject(listaWC);
				System.out.println("Workouts escrito");
			}
		} catch (IOException e) {
			System.err.println("Workouts no escrito");
			e.printStackTrace();
		}
	}

	public ArrayList<WorkoutCompleto> leerWorkoutsDesdeBackup() {
		ArrayList<WorkoutCompleto> lista = new ArrayList<>();
		File f = new File(FILE_WORKOUTS);
		if (!f.exists())
			return lista;
		try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(f))) {
			Object obj = ois.readObject();
			if (obj instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<WorkoutCompleto> tmp = (ArrayList<WorkoutCompleto>) obj;
				lista.addAll(tmp);
			} else if (obj instanceof WorkoutCompleto) {
				lista.add((WorkoutCompleto) obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public void guardarHistoricoWorkoutsXML() {
		Firestore co = null;
		try {
			co = Conexion.conectar();

			ApiFuture<QuerySnapshot> usuariosQuery = co.collection("usuarios").get();
			QuerySnapshot usuariosSnapshot = usuariosQuery.get();

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			Element rootElement = doc.createElement("historico");
			doc.appendChild(rootElement);

			for (QueryDocumentSnapshot userDoc : usuariosSnapshot.getDocuments()) {
				String userId = userDoc.getId();
				ApiFuture<QuerySnapshot> histQuery = co.collection("usuarios").document(userId)
						.collection("historico_workouts").get();
				QuerySnapshot histSnapshot = histQuery.get();
				for (QueryDocumentSnapshot histDoc : histSnapshot.getDocuments()) {
					Element histElem = doc.createElement("historico_wo");

					Element usuarioElem = doc.createElement("usuario_id");
					final String usuarioIdStr;
					if (userId != null) {
						usuarioIdStr = userId;
					} else {
						usuarioIdStr = "";
					}
					usuarioElem.appendChild(doc.createTextNode(usuarioIdStr));
					histElem.appendChild(usuarioElem);

					final String idWorkout;
					if (histDoc.contains("id_workout")) {
						Object idObj = histDoc.get("id_workout");
						if (idObj != null) {
							idWorkout = String.valueOf(idObj);
						} else {
							idWorkout = "";
						}
					} else {
						idWorkout = histDoc.getId();
					}
					Element idElem = doc.createElement("id_workout");
					final String idWorkoutStr;
					if (idWorkout != null) {
						idWorkoutStr = idWorkout;
					} else {
						idWorkoutStr = "";
					}
					idElem.appendChild(doc.createTextNode(idWorkoutStr));
					histElem.appendChild(idElem);

					Object ejercsObj = histDoc.get("ejercicios_hechos");
					final String ejercsStr;
					if (ejercsObj instanceof List) {
						List<?> list = (List<?>) ejercsObj;
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < list.size(); i++) {
							if (i > 0)
								sb.append(",");
							Object listItem = list.get(i);
							if (listItem != null) {
								sb.append(listItem.toString());
							}
						}
						ejercsStr = sb.toString();
					} else {
						if (ejercsObj != null) {
							ejercsStr = ejercsObj.toString();
						} else {
							ejercsStr = "";
						}
					}
					Element ejercsElem = doc.createElement("ejercicios_hechos");
					ejercsElem.appendChild(doc.createTextNode(ejercsStr));
					histElem.appendChild(ejercsElem);

					Object tiempoObj = histDoc.get("tiempo");
					final String tiempoStr;
					if (tiempoObj != null) {
						tiempoStr = tiempoObj.toString();
					} else {
						tiempoStr = "";
					}
					Element tiempoElem = doc.createElement("tiempo");
					tiempoElem.appendChild(doc.createTextNode(tiempoStr));
					histElem.appendChild(tiempoElem);

					Object fechaObj = histDoc.get("fecha");
					final String fechaStr;
					if (fechaObj != null) {
						fechaStr = fechaObj.toString();
					} else {
						fechaStr = "";
					}
					Element fechaElem = doc.createElement("fecha");
					fechaElem.appendChild(doc.createTextNode(fechaStr));
					histElem.appendChild(fechaElem);

					rootElement.appendChild(histElem);
				}
			}

			co.close();

			File dir = new File("backups");
			if (!dir.exists())
				dir.mkdirs();
			File outFile = new File(dir, "historico_workouts.xml");

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			try (FileOutputStream fos = new FileOutputStream(outFile)) {
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
			}

			System.out.println("escrito XML");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void generarBackupsDesdeServidor() {
		ArrayList<Usuario> usuarios = null;
		ArrayList<Workout> workouts = null;
		int maxLevel = 3;
		try {
			usuarios = new Usuario().mObtenerUsuarios();
			System.out.println("usuarios obtenidos");
		} catch (Exception e) {
			System.err.println("no se pudieron obtener usuarios: " + e.getMessage());
			usuarios = new ArrayList<>();
		}
		try {
			workouts = (ArrayList<Workout>) new Workout().obtenerWorkouts((long) maxLevel);
			System.out.println("workouts obtenidos");
		} catch (Exception e) {
			System.err.println("no se pudieron obtener workouts: " + e.getMessage());
			workouts = new ArrayList<>();
		}
		guardarUsuarios(usuarios);
		guardarWorkouts(workouts);
	}

	// Leer backups/usuario.dat y devolver lista de Usuario
	public ArrayList<Usuario> leerUsuariosDesdeBackup() {
		ArrayList<Usuario> lista = new ArrayList<>();
		File f = new File(FILE_USERS);
		if (!f.exists())
			return lista;
		try (java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.FileInputStream(f))) {
			Object obj = ois.readObject();
			if (obj instanceof ArrayList) {
				@SuppressWarnings("unchecked")
				ArrayList<Usuario> tmp = (ArrayList<Usuario>) obj;
				lista.addAll(tmp);
			} else if (obj instanceof Usuario) {
				lista.add((Usuario) obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return lista;
	}

	public ArrayList<java.util.Map<String, String>> leerHistoricoDesdeXML() {
		ArrayList<java.util.Map<String, String>> lista = new ArrayList<>();
		File f = new File("backups/historico_workouts.xml");
		if (!f.exists())
			return lista;
		try {
			javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory.newInstance();
			javax.xml.parsers.DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(f);
			doc.getDocumentElement().normalize();
			org.w3c.dom.NodeList nodes = doc.getElementsByTagName("historico_wo");
			for (int i = 0; i < nodes.getLength(); i++) {
				org.w3c.dom.Node n = nodes.item(i);
				if (n.getNodeType() == org.w3c.dom.Node.ELEMENT_NODE) {
					org.w3c.dom.Element e = (org.w3c.dom.Element) n;
					java.util.Map<String, String> mapa = new java.util.HashMap<>();
					mapa.put("usuario_id", getTagValue("usuario_id", e));
					mapa.put("id_workout", getTagValue("id_workout", e));
					mapa.put("ejercicios_hechos", getTagValue("ejercicios_hechos", e));
					mapa.put("tiempo", getTagValue("tiempo", e));
					mapa.put("fecha", getTagValue("fecha", e));
					lista.add(mapa);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return lista;
	}

	private String getTagValue(String tag, org.w3c.dom.Element element) {
		try {
			org.w3c.dom.NodeList nlList = element.getElementsByTagName(tag).item(0).getChildNodes();
			org.w3c.dom.Node nValue = (org.w3c.dom.Node) nlList.item(0);
			return nValue.getNodeValue();
		} catch (Exception e) {
			return "";
		}
	}

	public static void main(String[] args) {
		new lectura().generarBackupsDesdeServidor();
		new lectura().guardarHistoricoWorkoutsXML();
	}
}