// Archivo: BooleanRuleParser.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Analizador sintáctico por descenso recursivo que transforma reglas textuales
 * en un árbol de expresiones booleanas (AST) respetando precedencia (NOT > AND > OR) y paréntesis.
 */
public class BooleanRuleParser implements IRuleParser {

    @Override
    public IAccessExpression parse(String ruleText) {
        if (ruleText == null || ruleText.trim().isEmpty()) {
            throw new IllegalArgumentException("La regla tarifaria no puede ser nula ni vacía.");
        }
        List<String> tokens = tokenize(ruleText);
        if (tokens.isEmpty()) {
            throw new IllegalArgumentException("La regla tarifaria no contiene tokens válidos.");
        }

        int[] cursor = {0};
        IAccessExpression root = parseOrExpression(tokens, cursor);

        if (cursor[0] < tokens.size()) {
            throw new IllegalArgumentException(
                    "Error de sintaxis en la regla: token inesperado '" + tokens.get(cursor[0]) + "'."
            );
        }
        return root;
    }

    /**
     * Convierte la cadena de entrada en una secuencia de tokens léxicos normalizados.
     *
     * @param ruleText regla booleana en texto
     * @return lista de tokens léxicos
     */
    private List<String> tokenize(String ruleText) {
        String padded = ruleText.replace("(", " ( ").replace(")", " ) ");
        String[] rawParts = padded.trim().split("\\s+");
        List<String> tokens = new ArrayList<>();
        for (String part : rawParts) {
            if (!part.isEmpty()) {
                tokens.add(part.toUpperCase(Locale.ROOT));
            }
        }
        return tokens;
    }

    /**
     * Regla gramatical: {@code OrExpr := AndExpr ( 'OR' AndExpr )*}.
     */
    private IAccessExpression parseOrExpression(List<String> tokens, int[] cursor) {
        IAccessExpression left = parseAndExpression(tokens, cursor);
        while (cursor[0] < tokens.size() && "OR".equals(tokens.get(cursor[0]))) {
            cursor[0]++;
            if (cursor[0] >= tokens.size()) {
                throw new IllegalArgumentException("Error de sintaxis: falta operando derecho después del operador 'OR'.");
            }
            IAccessExpression right = parseAndExpression(tokens, cursor);
            left = new OrExpression(left, right);
        }
        return left;
    }

    /**
     * Regla gramatical: {@code AndExpr := NotExpr ( 'AND' NotExpr )*}.
     */
    private IAccessExpression parseAndExpression(List<String> tokens, int[] cursor) {
        IAccessExpression left = parseNotExpression(tokens, cursor);
        while (cursor[0] < tokens.size() && "AND".equals(tokens.get(cursor[0]))) {
            cursor[0]++;
            if (cursor[0] >= tokens.size()) {
                throw new IllegalArgumentException("Error de sintaxis: falta operando derecho después del operador 'AND'.");
            }
            IAccessExpression right = parseNotExpression(tokens, cursor);
            left = new AndExpression(left, right);
        }
        return left;
    }

    /**
     * Regla gramatical: {@code NotExpr := 'NOT' NotExpr | Primary}.
     */
    private IAccessExpression parseNotExpression(List<String> tokens, int[] cursor) {
        if (cursor[0] < tokens.size() && "NOT".equals(tokens.get(cursor[0]))) {
            cursor[0]++;
            if (cursor[0] >= tokens.size()) {
                throw new IllegalArgumentException("Error de sintaxis: falta operando después del operador 'NOT'.");
            }
            return new NotExpression(parseNotExpression(tokens, cursor));
        }
        return parsePrimaryExpression(tokens, cursor);
    }

    /**
     * Regla gramatical: {@code Primary := '(' OrExpr ')' | TERMINAL_TOKEN}.
     */
    private IAccessExpression parsePrimaryExpression(List<String> tokens, int[] cursor) {
        if (cursor[0] >= tokens.size()) {
            throw new IllegalArgumentException("Error de sintaxis: se esperaba un token o subexpresión.");
        }
        String token = tokens.get(cursor[0]);
        if ("(".equals(token)) {
            cursor[0]++;
            if (cursor[0] < tokens.size() && ")".equals(tokens.get(cursor[0]))) {
                throw new IllegalArgumentException("Error de sintaxis: los paréntesis vacíos '()' no son válidos.");
            }
            IAccessExpression inside = parseOrExpression(tokens, cursor);
            if (cursor[0] >= tokens.size() || !")".equals(tokens.get(cursor[0]))) {
                throw new IllegalArgumentException("Error de sintaxis: falta paréntesis de cierre ')'.");
            }
            cursor[0]++;
            return inside;
        }
        if (")".equals(token)) {
            throw new IllegalArgumentException("Error de sintaxis: paréntesis de cierre ')' inesperado.");
        }
        if ("AND".equals(token) || "OR".equals(token)) {
            throw new IllegalArgumentException(
                    "Error de sintaxis: el operador binario '" + token + "' requiere un operando izquierdo."
            );
        }
        cursor[0]++;
        return new TerminalTokenExpression(token);
    }
}
