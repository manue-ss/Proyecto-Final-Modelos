// Archivo: OrExpression.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Expresión no terminal (NonterminalExpression) que representa la disyunción lógica
 * en la gramática: {@code <Expression> OR <Expression>}.
 */
public class OrExpression implements IAccessExpression {

    private final IAccessExpression leftExpression;
    private final IAccessExpression rightExpression;

    /**
     * Construye un nodo de disyunción lógica validando ambos subárboles.
     *
     * @param leftExpression  subexpresión izquierda
     * @param rightExpression subexpresión derecha
     */
    public OrExpression(IAccessExpression leftExpression, IAccessExpression rightExpression) {
        if (leftExpression == null || rightExpression == null) {
            throw new IllegalArgumentException("Los operandos de la expresión OR no pueden ser nulos.");
        }
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public boolean interpret(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a interpretar no puede ser nulo.");
        }
        return leftExpression.interpret(context) || rightExpression.interpret(context);
    }

    @Override
    public String toGrammarString() {
        return "(" + leftExpression.toGrammarString() + " OR " + rightExpression.toGrammarString() + ")";
    }

    public IAccessExpression getLeftExpression() {
        return leftExpression;
    }

    public IAccessExpression getRightExpression() {
        return rightExpression;
    }
}
