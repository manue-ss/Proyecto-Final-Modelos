// Archivo: IIn.java | Paquete: co.edu.udistrital.interpreter.view
package co.edu.udistrital.interpreter.view;

/**
 * Abstracción para la captura de datos de entrada desde la vista.
 */
public interface IIn {

    /**
     * Captura una línea de texto desde el medio de entrada.
     *
     * @return cadena de texto ingresada por el usuario
     */
    String nextLine();

    /**
     * Captura un número entero desde el medio de entrada.
     *
     * @return valor entero ingresado por el usuario
     */
    int nextInt();

    /**
     * Captura un número decimal desde el medio de entrada.
     *
     * @return valor decimal ingresado por el usuario
     */
    double nextDouble();

    /**
     * Cierra y libera el recurso de entrada subyacente.
     */
    void close();
}
