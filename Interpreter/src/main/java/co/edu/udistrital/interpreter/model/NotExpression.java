// Archivo: NotExpression.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Expresión no terminal (NonterminalExpression) que representa la negación lógica
 * en la gramática: {@code NOT <Expression>}.
 */
public class NotExpression implements IAccessExpression {

    private final IAccessExpression targetExpression;

    /**
     * Construye un nodo de negación lógica validando el subárbol objetivo.
     *
     * @param targetExpression subexpresión a negar
     */
    public NotExpression(IAccessExpression targetExpression) {
        if (targetExpression == null) {
            throw new IllegalArgumentException("El operando de la expresión NOT no puede ser nulo.");
        }
        this.targetExpression = targetExpression;
    }

    @Override
    public boolean interpret(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a interpretar no puede ser nulo.");
        }
        return !targetExpression.interpret(context);
    }

    @Override
    public String toGrammarString() {
        return "(NOT " + targetExpression.toGrammarString() + ")";
    }

    public IAccessExpression getTargetExpression() {
        return targetExpression;
    }
}
