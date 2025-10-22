package conexion;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;

public class Conexion {
	
	private static String nombreJSON = "GymAppG6.json";
	private static String projectID = "gymappreto1";
	
	public static Firestore conectar() throws IOException {
		FileInputStream serviceAccount;
		Firestore fs = null;
		try {
			serviceAccount = new FileInputStream(nombreJSON);

			FirestoreOptions firestoreOptions = FirestoreOptions.getDefaultInstance().toBuilder()
					.setProjectId(projectID).setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
			fs = firestoreOptions.getService();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return fs;
	}
}