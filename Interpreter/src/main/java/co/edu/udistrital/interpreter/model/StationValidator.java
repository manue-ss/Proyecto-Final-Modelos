// Archivo: StationValidator.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Entidad de dominio rica que representa el validador de acceso de una estación,
 * capaz de cambiar su regla tarifaria en tiempo de ejecución sin modificar el código fuente.
 */
public class StationValidator implements IStationValidator {

    private final String validatorId;
    private String activePolicyName;
    private IAccessExpression activeExpression;

    /**
     * Construye un validador de estación con su identificador, política inicial y árbol de expresión.
     *
     * @param validatorId      código identificador del torniquete o validador
     * @param initialPolicy    nombre de la política tarifaria inicial
     * @param initialRuleTree  árbol de expresión booleana inicial
     */
    public StationValidator(String validatorId, String initialPolicy, IAccessExpression initialRuleTree) {
        if (validatorId == null || validatorId.trim().isEmpty()) {
            throw new IllegalArgumentException("El identificador del validador no puede ser nulo ni vacío.");
        }
        this.validatorId = validatorId.trim();
        updatePolicy(initialPolicy, initialRuleTree);
    }

    @Override
    public final void updatePolicy(String policyName, IAccessExpression expressionTree) {
        if (policyName == null || policyName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la política tarifaria no puede ser nulo ni vacío.");
        }
        if (expressionTree == null) {
            throw new IllegalArgumentException("El árbol de expresión de la regla no puede ser nulo.");
        }
        this.activePolicyName = policyName.trim();
        this.activeExpression = expressionTree;
    }

    @Override
    public boolean validateAccess(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a validar no puede ser nulo.");
        }
        return activeExpression.interpret(context);
    }

    @Override
    public String evaluateWithReport(TravelContext context) {
        boolean authorized = validateAccess(context);
        String decision = authorized ? "ACCESO AUTORIZADO (Torniquete Liberado)" : "ACCESO DENEGADO (Torniquete Bloqueado)";
        return String.format(
                "  Validador           : %s%n"
                + "  Política Activa     : %s%n"
                + "  Árbol AST Evaluado  : %s%n"
                + "  Dictamen Final      : %s",
                validatorId,
                activePolicyName,
                activeExpression.toGrammarString(),
                decision
        );
    }

    public String getValidatorId() {
        return validatorId;
    }

    @Override
    public String getActivePolicyName() {
        return activePolicyName;
    }

    @Override
    public IAccessExpression getActiveExpression() {
        return activeExpression;
    }
}
