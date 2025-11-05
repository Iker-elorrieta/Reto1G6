package conexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class Conexion {

	private static final String nombreJSON = "GymAppG6.json";
	private static final String projectID = "gymappreto1";

	public static Firestore conectar() throws IOException {
		FileInputStream serviceAccount;
		Firestore fs = null;
		try {
			// Comprobación rápida de red: intentamos conectar al host de Firestore con un
			// timeout corto.
			// Si falla devolvemos null para que la aplicación use los backups locales.
			try (Socket sock = new Socket()) {
				SocketAddress addr = new InetSocketAddress("firestore.googleapis.com", 443);
				sock.connect(addr, 1500); // 1.5s timeout
			} catch (Exception netEx) {
				System.err
						.println("Sin conexión o no se puede alcanzar firestore.googleapis.com: " + netEx.getMessage());
				return null;
			}

			serviceAccount = new FileInputStream(nombreJSON);

			FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
					.setProjectId(projectID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			fs = firestoreOptions.getService();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return fs;
	}

	// Comprueba rápidamente si el host de Firestore es alcanzable.
	public static boolean hostAlcanzable(int timeoutMillis) {
		try (Socket sock = new Socket()) {
			SocketAddress addr = new InetSocketAddress("firestore.googleapis.com", 443);
			sock.connect(addr, timeoutMillis);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
}