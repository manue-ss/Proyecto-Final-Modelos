// Archivo: ConsoleOut.java | Paquete: co.edu.udistrital.interpreter.view
package co.edu.udistrital.interpreter.view;

/**
 * Implementación concreta de salida de datos por consola estándar.
 */
public class ConsoleOut implements IOut {

    @Override
    public void show(String message) {
        System.out.print(message);
    }

    @Override
    public void showLine(String message) {
        System.out.println(message);
    }

    @Override
    public void showError(String errorMessage) {
        System.err.println("[ERROR] " + errorMessage);
    }
}
