// Archivo: IStationValidator.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Abstracción del servicio de validación de torniquete en estación que aplica
 * dinámicamente el árbol de expresiones del patrón Interpreter sobre cada viaje.
 */
public interface IStationValidator {

    /**
     * Actualiza dinámicamente la política tarifaria activa del validador.
     *
     * @param policyName     nombre descriptivo de la política tarifaria
     * @param expressionTree árbol de expresiones booleanas a evaluar
     */
    void updatePolicy(String policyName, IAccessExpression expressionTree);

    /**
     * Evalúa si el contexto de viaje cumple la política tarifaria activa.
     *
     * @param context contexto del viaje a validar
     * @return true si se autoriza el paso; false si se deniega
     */
    boolean validateAccess(TravelContext context);

    /**
     * Genera un reporte detallado de la interpretación de la regla sobre el contexto del viaje.
     *
     * @param context contexto del viaje evaluado
     * @return reporte multilínea con el resultado de la validación
     */
    String evaluateWithReport(TravelContext context);

    /**
     * Obtiene el nombre de la política tarifaria activa.
     *
     * @return nombre de la política
     */
    String getActivePolicyName();

    /**
     * Obtiene el árbol de expresiones activo en el validador.
     *
     * @return expresión raíz activa
     */
    IAccessExpression getActiveExpression();
}
