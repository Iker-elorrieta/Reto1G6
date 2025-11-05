package Modelo;

import javax.swing.JLabel;

public class CronometroThread extends Thread {

	private boolean enEjecucion = false;
	private boolean detenido = false;
	private long tiempoInicio = 0;
	private long tiempoAcumulado = 0;
	private final JLabel label;

	// Si duracionInicialMillis > 0, el cronómetro funciona como cuenta atrás
	private final long duracionInicialMillis;
	private boolean modoCuentaAtras = false;

	// Prefijo para modo progresivo (no cuenta atrás)
	private final String prefijoTexto;

	// Listener para notificar fin de cuenta atrás
	public interface CronometroListener {
		void terminado();
	}

	private CronometroListener listener = null;

	// Constructor por defecto (stopwatch) con prefijo por defecto
	public CronometroThread(JLabel label) {
		this(label, 0, "Tiempo Workout:");
	}

	public CronometroThread(JLabel label, String prefijo) {
		this(label, 0, prefijo);
	}

	// Constructor para cuenta atrás y/o prefijo personalizado
	public CronometroThread(JLabel label, long duracionInicialMillis) {
		this(label, duracionInicialMillis, "Tiempo Workout:");
	}

	public CronometroThread(JLabel label, long duracionInicialMillis, String prefijo) {
		this.label = label;
		this.duracionInicialMillis = duracionInicialMillis;
		this.modoCuentaAtras = duracionInicialMillis > 0;

		if (prefijo == null) {
			this.prefijoTexto = "";
		} else {
			this.prefijoTexto = prefijo;
		}
	}

	public void setListener(CronometroListener l) {
		this.listener = l;
	}

	public void run() {
		while (!detenido) {
			if (enEjecucion) {
				long tiempoActual = System.currentTimeMillis();
				long transcurrido = tiempoAcumulado + (tiempoActual - tiempoInicio);
				if (modoCuentaAtras) {
					long restante = duracionInicialMillis - transcurrido;
					if (restante <= 0) {
						actualizarLabel(0);
						enEjecucion = false;
						if (listener != null) {
							listener.terminado();
						}
					} else {
						actualizarLabel(restante);
					}
				} else {
					actualizarLabel(transcurrido);
				}
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	private void actualizarLabel(long milisegundos) {
		long segundosTot = milisegundos / 1000;
		long segundos = segundosTot % 60;
		long minutos = (segundosTot / 60) % 60;
		long horas = (segundosTot / 3600);
		if (modoCuentaAtras) {
			// formato mm:ss
			String texto = String.format("%02d:%02d", minutos + horas * 60, segundos);
			label.setText(texto);
		} else {
			// usar prefijo personalizado
			if (prefijoTexto == null || prefijoTexto.trim().isEmpty()) {
				String texto = String.format("%02d:%02d", minutos, segundos);
				label.setText(texto);
			} else {
				String texto = String.format("%s %02d:%02d mins", prefijoTexto, minutos, segundos);
				label.setText(texto);
			}
		}
	}

	public void iniciar() {
		if (!enEjecucion) {
			tiempoInicio = System.currentTimeMillis();
			enEjecucion = true;
		}
	}

	public void pausar() {
		if (enEjecucion) {
			long ahora = System.currentTimeMillis();
			tiempoAcumulado += (ahora - tiempoInicio);
			enEjecucion = false;
		}
	}

	public void detener() {
		// Asegurar que acumulamos el tiempo en curso antes de detener
		if (enEjecucion) {
			long ahora = System.currentTimeMillis();
			tiempoAcumulado += (ahora - tiempoInicio);
		}
		enEjecucion = false;
		detenido = true;
	}

	public boolean estaEnEjecucion() {
		return enEjecucion;
	}

	// Permite reiniciar
	public void reiniciarCuentaAtras() {
		tiempoInicio = System.currentTimeMillis();
		tiempoAcumulado = 0;
		enEjecucion = false;
	}

	// Devuelve milisegundos transcurridos
	public synchronized long obtenerMilisTranscurridos() {
		long transcurrido = tiempoAcumulado;
		if (enEjecucion) {
			transcurrido += System.currentTimeMillis() - tiempoInicio;
		}
		if (modoCuentaAtras) {
			long restante = duracionInicialMillis - transcurrido;
			return Math.max(0, restante);
		} else {
			return transcurrido;
		}
	}
}