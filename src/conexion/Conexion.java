package conexion;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

public class Conexion {
    
    private static String nombreJSON = "GymAppG6.json";
    private static String projectID = "gymappreto1";
    
    // Initialize FirebaseApp once using the local service account JSON if present,
    // otherwise try Application Default Credentials.
    public static void init() throws IOException {
        if (!FirebaseApp.getApps().isEmpty()) return;

        File json = new File(nombreJSON);
        try {
            if (json.exists()) {
                try (FileInputStream serviceAccount = new FileInputStream(json)) {
                    FirebaseOptions options = FirebaseOptions.builder()
                            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                            .setProjectId(projectID)
                            .build();
                    FirebaseApp.initializeApp(options);
                }
            } else {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.getApplicationDefault())
                        .setProjectId(projectID)
                        .build();
                FirebaseApp.initializeApp(options);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Return Firestore instance, ensuring FirebaseApp is initialized first
    public static Firestore conectar() throws IOException {
        init();
        return FirestoreClient.getFirestore();
    }

}