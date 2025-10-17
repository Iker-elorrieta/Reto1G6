package Principal;

import Controlador.ControladorUsuario;

public class Principal {

	public static void main(String[] args) {
		try {
			
			Vista.login ventanaLogin = new Vista.login();
			ventanaLogin.setVisible(true);
			
			
			new ControladorUsuario(ventanaLogin);
				
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}