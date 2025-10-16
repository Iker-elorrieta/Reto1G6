package Controlador;

import conexion.Conexion;
import Modelo.Usuario;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.QueryDocumentSnapshot;

// Firebase imports
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.Collections;
import java.text.SimpleDateFormat;

public class controlador {

    public static boolean guardarUsuario(Usuario u) throws IOException {
        // Initialize Firebase via Conexion (centralized)
        try {
            Conexion.init();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        // Validar email localmente antes de enviar a Firebase
        String email = u.getEmail();
        String emailRegex = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";
        if (email == null || !email.matches(emailRegex)) {
            throw new IOException("Email inválido: " + email);
        }

        Firestore db = Conexion.conectar();

        // Comprobar si ya existe un usuario con ese email en Firestore
        try {
            ApiFuture<QuerySnapshot> queryFuture = db.collection("usuarios").whereEqualTo("email", u.getEmail()).get();
            QuerySnapshot querySnapshot = queryFuture.get();
            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                throw new IOException("El correo ya está registrado.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IOException("Error comprobando existencia de email: " + e.getMessage(), e);
        }

        // Crear usuario en Firebase Authentication (no guardamos el uid en Firestore)
        try {
            UserRecord.CreateRequest createReq = new UserRecord.CreateRequest()
                    .setEmail(u.getEmail())
                    .setEmailVerified(false)
                    .setPassword(u.getPass())
                    .setDisplayName(u.getNombre() + " " + u.getApellidos())
                    .setDisabled(false);
            FirebaseAuth.getInstance().createUser(createReq);
        } catch (IllegalArgumentException iae) {
            iae.printStackTrace();
            throw new IOException("Validación inválida: " + iae.getMessage(), iae);
        } catch (FirebaseAuthException fae) {
            fae.printStackTrace();
            String msg = fae.getMessage();
            if (msg != null && msg.contains("CONFIGURATION_NOT_FOUND")) {
                throw new IOException("CONFIGURATION_NOT_FOUND: habilita Email/Password en Firebase Console -> Authentication -> Sign-in method, o activa Identity Platform / Identity Toolkit API en Google Cloud para el proyecto 'gymappreto1'.");
            }
            throw new IOException("Error al crear usuario en Firebase Auth: " + fae.getMessage(), fae);
        }

        // Guardar datos en Firestore (según requisitos del usuario)

        try {
            // Documento contador para asignar usuarioId autoincremental
            DocumentReference counterRef = db.collection("counters").document("usuarios");

            // Ejecutar transacción para incrementar y obtener el siguiente id que no exista en la colección usuarios
            ApiFuture<Long> txFuture = db.runTransaction(transaction -> {
                // Obtener el documento contador
                DocumentSnapshot snap = transaction.get(counterRef).get();
                long seq = 0L;
                if (snap.exists() && snap.contains("seq")) {
                    Number n = snap.getLong("seq");
                    if (n != null) seq = n.longValue();
                }

                long candidate = seq;
                while (true) {
                    candidate = candidate + 1L; // siguiente candidato
                    DocumentReference userDocRef = db.collection("usuarios").document(String.valueOf(candidate));
                    DocumentSnapshot userSnap = transaction.get(userDocRef).get();
                    if (!userSnap.exists()) {
                        // este id está libre; actualizar contador y reservarlo
                        Map<String, Object> update = Collections.singletonMap("seq", candidate);
                        transaction.set(counterRef, update);
                        return candidate;
                    }
                    // si existe, continuar al siguiente candidato (y el bucle seguirá)
                }
            });

            long usuarioId = txFuture.get();

            // Formatear fecha a dd-MM-yyyy
            String fnacimientoStr = null;
            if (u.getFechaNacimiento() != null) {
                SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                fnacimientoStr = df.format(u.getFechaNacimiento());
            }

            Map<String, Object> data = new HashMap<>();
            data.put("nombre", u.getNombre());
            data.put("apellidos", u.getApellidos());
            data.put("email", u.getEmail());
            data.put("pass", u.getPass()); // según petición: guardar contraseña (inseguro)
            // ya no guardamos uid; usaremos usuarioId como identificador
            data.put("fnacimiento", fnacimientoStr);
            data.put("nivel", u.getNivel());
            data.put("tipoUsuario", "CLIENTE");
            data.put("usuarioId", usuarioId);

            // Guardar documento con id autoincremental (string del numero)
            ApiFuture<WriteResult> writeResult = db.collection("usuarios").document(String.valueOf(usuarioId)).set(data);
            writeResult.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IOException("Error al escribir en Firestore: " + e.getMessage(), e);
        }
    }

    // Método para autenticar usando email y pass (comprueba Firestore)
    public static boolean autenticar(String email, String pass) throws IOException {
        try {
            Conexion.init();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        Firestore db = Conexion.conectar();
        try {
            ApiFuture<QuerySnapshot> queryFuture = db.collection("usuarios").whereEqualTo("email", email).get();
            QuerySnapshot querySnapshot = queryFuture.get();
            if (querySnapshot == null || querySnapshot.isEmpty()) {
                return false;
            }
            // Tomamos el primer documento (debería ser único por la verificación al crear)
            for (QueryDocumentSnapshot doc : querySnapshot.getDocuments()) {
                Object storedPass = doc.get("pass");
                if (storedPass != null && storedPass.toString().equals(pass)) {
                    return true;
                }
            }
            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new IOException("Error durante autenticación: " + e.getMessage(), e);
        }
    }

}