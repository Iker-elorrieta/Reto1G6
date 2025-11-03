package Modelo;

import javax.swing.JLabel;

public class CronometroThread extends Thread {

    private boolean enEjecucion = false;
    private boolean detenido = false;
    private long tiempoInicio = 0;
    private long tiempoAcumulado = 0;
    private JLabel label; 

    public CronometroThread(JLabel label) {
        this.label = label;
    }

    public void run() {
        while (!detenido) {
            if (enEjecucion) {
                long tiempoActual = System.currentTimeMillis();
                long transcurrido = tiempoAcumulado + (tiempoActual - tiempoInicio);
                actualizarLabel(transcurrido);
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarLabel(long milisegundos) {
        long segundos = (milisegundos / 1000) % 60;
        long minutos = (milisegundos / 1000) / 60;
        String texto = String.format("Tiempo Workout: %02d:%02d mins", minutos, segundos);

       
        label.setText(texto);
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
        enEjecucion = false;
        detenido = true;
    }

    public boolean isEnEjecucion() {
        return enEjecucion;
    }
}
