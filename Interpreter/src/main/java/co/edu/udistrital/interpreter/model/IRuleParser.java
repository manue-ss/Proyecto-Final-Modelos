// Archivo: IRuleParser.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Abstracción del analizador sintáctico encargado de construir el árbol de expresiones
 * booleanas (AST) a partir de una regla textual definida por los administradores.
 */
public interface IRuleParser {

    /**
     * Analiza una expresión textual y construye el árbol sintáctico del patrón Interpreter.
     *
     * @param ruleText regla booleana en texto (ej. "(ZONA_NORTE AND HORA_VALLE) OR NOT DIA_FESTIVO")
     * @return raíz del árbol de expresiones {@link IAccessExpression}
     */
    IAccessExpression parse(String ruleText);
}
