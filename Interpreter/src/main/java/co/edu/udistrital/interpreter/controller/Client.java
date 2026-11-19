// Archivo: Client.java | Paquete: co.edu.udistrital.interpreter.controller
package co.edu.udistrital.interpreter.controller;

import co.edu.udistrital.interpreter.model.IAccessExpression;
import co.edu.udistrital.interpreter.model.IRuleParser;
import co.edu.udistrital.interpreter.model.IStationValidator;
import co.edu.udistrital.interpreter.model.TravelContext;
import co.edu.udistrital.interpreter.view.IIn;
import co.edu.udistrital.interpreter.view.IOut;

/**
 * Controlador de Caja Negra completamente desacoplado que expone únicamente métodos
 * de delegación pura sobre el validador (IStationValidator), el analizador gramatical (IRuleParser),
 * las expresiones (IAccessExpression) y las abstracciones de Entrada/Salida (IIn, IOut).
 */
public class Client {

    private final IStationValidator validator;
    private final IRuleParser parser;
    private final IIn in;
    private final IOut out;

    /**
     * Construye el cliente inyectando las abstracciones del modelo y de la vista.
     *
     * @param validator servicio validador de estación
     * @param parser    analizador sintáctico de reglas booleanas
     * @param in        abstracción de entrada
     * @param out       abstracción de salida
     */
    public Client(IStationValidator validator, IRuleParser parser, IIn in, IOut out) {
        if (validator == null || parser == null || in == null || out == null) {
            throw new IllegalArgumentException("Las dependencias inyectadas en Client no pueden ser nulas.");
        }
        this.validator = validator;
        this.parser = parser;
        this.in = in;
        this.out = out;
    }

    /**
     * Delega al analizador sintáctico la construcción de un árbol de expresiones (AST)
     * a partir de una cadena de texto con la regla booleana.
     *
     * @param ruleText regla booleana en texto
     * @return expresión raíz del árbol AST construido
     */
    public IAccessExpression parseRule(String ruleText) {
        return parser.parse(ruleText);
    }

    /**
     * Delega la actualización de la política activa en el validador recibiendo un árbol AST.
     *
     * @param policyName     nombre descriptivo de la política
     * @param expressionTree árbol de expresión booleana
     */
    public void updatePolicy(String policyName, IAccessExpression expressionTree) {
        validator.updatePolicy(policyName, expressionTree);
        out.showLine("-------------------------------------------------------");
        out.showLine("Política actualizada exitosamente:");
        out.showLine("  Nombre de Política  : " + validator.getActivePolicyName());
        out.showLine("  Gramática Canónica  : " + validator.getActiveExpression().toGrammarString());
        out.showLine("-------------------------------------------------------");
    }

    /**
     * Delega la consulta de la política y gramática activas en el validador.
     *
     * @return representación textual de la política activa
     */
    public String showActivePolicy() {
        String info = String.format(
                "  Nombre de Política  : %s%n"
                + "  Gramática Canónica  : %s",
                validator.getActivePolicyName(),
                validator.getActiveExpression().toGrammarString()
        );
        out.showLine("-------------------------------------------------------");
        out.showLine("[POLÍTICA TARIFARIA ACTIVA]");
        out.showLine(info);
        out.showLine("-------------------------------------------------------");
        return info;
    }

    /**
     * Delega la validación de un contexto de viaje en el validador activo,
     * mostrando el estado inicial del contexto y el reporte de interpretación.
     *
     * @param context contexto del viaje a evaluar
     * @return resultado booleano de la autorización de paso
     */
    public boolean validateTrip(TravelContext context) {
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a validar no puede ser nulo.");
        }
        out.showLine("-------------------------------------------------------");
        out.showLine("[CONTEXTO DEL VIAJE]");
        out.showLine(context.formatContext());

        String report = validator.evaluateWithReport(context);
        boolean authorized = validator.validateAccess(context);

        out.showLine("\n[DETALLE DE LA INTERPRETACIÓN]");
        out.showLine(report);
        out.showLine("-------------------------------------------------------");
        return authorized;
    }

    /**
     * Delega la interpretación directa de cualquier expresión booleana sobre un contexto de viaje.
     *
     * @param expression árbol de expresión a interpretar
     * @param context    contexto del viaje
     * @return resultado booleano de la evaluación
     */
    public boolean interpretExpression(IAccessExpression expression, TravelContext context) {
        if (expression == null) {
            throw new IllegalArgumentException("La expresión a interpretar no puede ser nula.");
        }
        if (context == null) {
            throw new IllegalArgumentException("El contexto de viaje a interpretar no puede ser nulo.");
        }
        out.showLine("-------------------------------------------------------");
        out.showLine("[CONTEXTO DEL VIAJE]");
        out.showLine(context.formatContext());

        boolean result = expression.interpret(context);
        String decision = result ? "ACCESO AUTORIZADO" : "ACCESO DENEGADO";

        out.showLine("\n[EVALUACIÓN DIRECTA DE EXPRESIÓN]");
        out.showLine("  Árbol AST Evaluado  : " + expression.toGrammarString());
        out.showLine("  Dictamen Final      : " + decision);
        out.showLine("-------------------------------------------------------");
        return result;
    }

    /**
     * Delega la impresión de un mensaje sin salto de línea en la vista.
     *
     * @param message texto a mostrar
     */
    public void show(String message) {
        out.show(message);
    }

    /**
     * Delega la impresión de un mensaje con salto de línea en la vista.
     *
     * @param message texto a mostrar
     */
    public void showLine(String message) {
        out.showLine(message);
    }

    /**
     * Delega la impresión de un mensaje de error en la vista.
     *
     * @param errorMessage texto del error
     */
    public void showError(String errorMessage) {
        out.showError(errorMessage);
    }

    /**
     * Delega la lectura de una línea de texto desde la vista.
     *
     * @return texto ingresado
     */
    public String nextLine() {
        return in.nextLine();
    }

    /**
     * Delega la lectura de un número entero desde la vista.
     *
     * @return entero ingresado
     */
    public int nextInt() {
        return in.nextInt();
    }

    /**
     * Delega la lectura de un número decimal desde la vista.
     *
     * @return decimal ingresado
     */
    public double nextDouble() {
        return in.nextDouble();
    }

    /**
     * Delega el cierre del recurso de entrada.
     */
    public void close() {
        in.close();
    }
}
