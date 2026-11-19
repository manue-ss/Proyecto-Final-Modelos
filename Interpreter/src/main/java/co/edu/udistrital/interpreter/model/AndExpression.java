// Archivo: AndExpression.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

/**
 * Expresión no terminal (NonterminalExpression) que representa la conjunción lógica
 * en la gramática: {@code <Expression> AND <Expression>}.
 */
public class AndExpression implements IAccessExpression {

    private final IAccessExpression leftExpression;
    private final IAccessExpression rightExpression;

    /**
     * Construye un nodo de conjunción lógica validando ambos subárboles.
     *
     * @param leftExpression  subexpresión izquierda
     * @param rightExpression subexpresión derecha
     */
    public AndExpression(IAccessExpression leftExpression, IAccessExpression rightExpression) {
        if (leftExpression == null || rightExpression == null) {
            throw new IllegalArgumentException("Los operandos de la expresión AND no pueden ser nulos.");
        }
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
    }

    @Override
    public boolean interpret(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a interpretar no puede ser nulo.");
        }
        return leftExpression.interpret(context) && rightExpression.interpret(context);
    }

    @Override
    public String toGrammarString() {
        return "(" + leftExpression.toGrammarString() + " AND " + rightExpression.toGrammarString() + ")";
    }

    public IAccessExpression getLeftExpression() {
        return leftExpression;
    }

    public IAccessExpression getRightExpression() {
        return rightExpression;
    }
}
