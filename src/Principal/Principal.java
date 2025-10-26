package Principal;

import Controlador.ControladorUsuario;
import Vista.login;

public class Principal {

	public static void main(String[] args) {
		try {
			
			login ventanaLogin = new login();
			ventanaLogin.setVisible(true);
			new ControladorUsuario(ventanaLogin);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}