package LecturaPB;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import Modelo.Usuario;
import Modelo.Workout;

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

	private static final String FILE_USERS = "backups/usuario.dat";
	private static final String FILE_WORKOUTS = "backups/workouts.dat";

	public static void guardarUsuarios(ArrayList<Usuario> usuarios) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			File f = new File(FILE_USERS);
			if (!f.exists()) f.createNewFile();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				if (usuarios != null) {
					for (Usuario usu : usuarios) {
						oos.writeObject(usu);
					}
				}
				System.out.println("Usuarios escrito");
			}
		} catch (IOException e) {
			System.err.println("Usuarios no escrito");
			e.printStackTrace();
		}
	}

	public static void guardarWorkouts(ArrayList<Workout> workouts) {
		try {
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			File f = new File(FILE_WORKOUTS);
			if (!f.exists()) f.createNewFile();
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f))) {
				if (workouts != null) {
					for (Workout wot : workouts) {
						oos.writeObject(wot);
					}
				}
				System.out.println("Workouts escrito");
			}
		} catch (IOException e) {
			System.err.println("Workouts no escrito");
			e.printStackTrace();
		}
	}

	public static void guardarHistoricoWorkoutsXML() {
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
				ApiFuture<QuerySnapshot> histQuery = co.collection("usuarios").document(userId).collection("historico_workouts").get();
				QuerySnapshot histSnapshot = histQuery.get();
				for (QueryDocumentSnapshot histDoc : histSnapshot.getDocuments()) {
					Element histElem = doc.createElement("historico_wo");

					Element usuarioElem = doc.createElement("usuario_id");
					String usuarioIdStr = "";
					if (userId != null) {
						usuarioIdStr = userId;
					}
					usuarioElem.appendChild(doc.createTextNode(usuarioIdStr));
					histElem.appendChild(usuarioElem);

					// id_workout (si existe el campo, si no usar id del documento)
					String idWorkout;
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
					String idWorkoutStr = "";
					if (idWorkout != null) {
						idWorkoutStr = idWorkout;
					}
					idElem.appendChild(doc.createTextNode(idWorkoutStr));
					histElem.appendChild(idElem);

					// ejercicios_hechos: si es una lista, unimos por comas, si no, tostring
					Object ejercsObj = histDoc.get("ejercicios_hechos");
					String ejercsStr = "";
					if (ejercsObj instanceof List) {
						List<?> list = (List<?>) ejercsObj;
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < list.size(); i++) {
							if (i > 0) sb.append(",");
							Object listItem = list.get(i);
							if (listItem != null) {
								sb.append(listItem.toString());
							}
						}
						ejercsStr = sb.toString();
					} else if (ejercsObj != null) {
						ejercsStr = ejercsObj.toString();
					}
					Element ejercsElem = doc.createElement("ejercicios_hechos");
					ejercsElem.appendChild(doc.createTextNode(ejercsStr));
					histElem.appendChild(ejercsElem);

					// tiempo
					Object tiempoObj = histDoc.get("tiempo");
					String tiempoStr = "";
					if (tiempoObj != null) {
						tiempoStr = tiempoObj.toString();
					}
					Element tiempoElem = doc.createElement("tiempo");
					tiempoElem.appendChild(doc.createTextNode(tiempoStr));
					histElem.appendChild(tiempoElem);

					// fecha
					Object fechaObj = histDoc.get("fecha");
					String fechaStr = "";
					if (fechaObj != null) {
						fechaStr = fechaObj.toString();
					}
					Element fechaElem = doc.createElement("fecha");
					fechaElem.appendChild(doc.createTextNode(fechaStr));
					histElem.appendChild(fechaElem);

					rootElement.appendChild(histElem);
				}
			}

			co.close();

			// Guardar documento XML
			File dir = new File("backups");
			if (!dir.exists()) dir.mkdirs();
			File outFile = new File(dir, "historico_workouts.xml");

			TransformerFactory tf = TransformerFactory.newInstance();
			Transformer transformer = tf.newTransformer();
			DOMSource source = new DOMSource(doc);
			try (FileOutputStream fos = new FileOutputStream(outFile)) {
				StreamResult result = new StreamResult(fos);
				transformer.transform(source, result);
			}

			System.out.println("escrito");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	private static void generarBackupsDesdeServidor() {
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

	
	public static void main(String[] args) {
		generarBackupsDesdeServidor();
		guardarHistoricoWorkoutsXML();
	}
}