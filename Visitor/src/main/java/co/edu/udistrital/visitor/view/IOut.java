// Archivo: IOut.java | Paquete: co.edu.udistrital.visitor.view
package co.edu.udistrital.visitor.view;

/**
 * Abstracción para la visualización de mensajes estándar y de error.
 */
public interface IOut {

    /**
     * Muestra un mensaje sin salto de línea al final.
     *
     * @param message texto a mostrar
     */
    void show(String message);

    /**
     * Muestra un mensaje con salto de línea al final.
     *
     * @param message texto a mostrar
     */
    void showLine(String message);

    /**
     * Muestra un mensaje de error en la salida correspondiente.
     *
     * @param errorMessage descripción del error
     */
    void showError(String errorMessage);
}
