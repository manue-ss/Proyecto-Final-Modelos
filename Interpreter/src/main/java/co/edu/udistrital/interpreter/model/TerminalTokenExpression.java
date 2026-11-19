// Archivo: TerminalTokenExpression.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

import java.util.Locale;

/**
 * Expresión terminal (TerminalExpression) que evalúa si una condición atómica
 * (zona tarifaria, franja horaria o tipo de día) está activa en el contexto del viaje.
 */
public class TerminalTokenExpression implements IAccessExpression {

    private final String token;

    /**
     * Construye una expresión terminal validando que el identificador sea válido
     * y no corresponda a una palabra reservada de la gramática booleana.
     *
     * @param token identificador terminal (ej. ZONA_NORTE, HORA_VALLE, DIA_FESTIVO)
     */
    public TerminalTokenExpression(String token) {
        if (token == null || token.trim().isEmpty()) {
            throw new IllegalArgumentException("El token de una expresión terminal no puede ser nulo ni vacío.");
        }
        String normalized = token.trim().toUpperCase(Locale.ROOT);
        if ("AND".equals(normalized) || "OR".equals(normalized) || "NOT".equals(normalized)
                || "(".equals(normalized) || ")".equals(normalized)) {
            throw new IllegalArgumentException(
                    "El operador o símbolo reservado '" + normalized + "' no puede usarse como token terminal."
            );
        }
        if (!normalized.matches("[A-Z0-9_]+")) {
            throw new IllegalArgumentException(
                    "El token terminal '" + normalized + "' contiene caracteres inválidos. Use letras, números y '_'."
            );
        }
        this.token = normalized;
    }

    @Override
    public boolean interpret(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a interpretar no puede ser nulo.");
        }
        return context.hasToken(token);
    }

    @Override
    public String toGrammarString() {
        return token;
    }

    public String getToken() {
        return token;
    }
}
