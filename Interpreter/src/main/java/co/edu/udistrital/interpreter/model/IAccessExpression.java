// Archivo: IAccessExpression.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Expresión abstracta (AbstractExpression) del patrón Interpreter para evaluar
 * reglas booleanas de autorización de acceso sobre el contexto de un viaje.
 */
public interface IAccessExpression {

    /**
     * Interpreta y evalúa la expresión booleana frente al contexto de viaje suministrado.
     *
     * @param context contexto del viaje con los tokens activos
     * @return true si la regla autoriza el paso; false en caso contrario
     */
    boolean interpret(TravelContext context);

    /**
     * Representa la estructura canónica del árbol de sintaxis abstracta (AST) en texto.
     *
     * @return representación gramatical de la expresión
     */
    String toGrammarString();
}
