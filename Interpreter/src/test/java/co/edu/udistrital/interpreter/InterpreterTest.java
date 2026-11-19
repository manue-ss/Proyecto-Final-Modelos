// Archivo: InterpreterTest.java | Paquete: co.edu.udistrital.interpreter
package co.edu.udistrital.interpreter;

import co.edu.udistrital.interpreter.controller.Client;
import co.edu.udistrital.interpreter.model.AndExpression;
import co.edu.udistrital.interpreter.model.BooleanRuleParser;
import co.edu.udistrital.interpreter.model.IAccessExpression;
import co.edu.udistrital.interpreter.model.IRuleParser;
import co.edu.udistrital.interpreter.model.IStationValidator;
import co.edu.udistrital.interpreter.model.NotExpression;
import co.edu.udistrital.interpreter.model.OrExpression;
import co.edu.udistrital.interpreter.model.StationValidator;
import co.edu.udistrital.interpreter.model.TerminalTokenExpression;
import co.edu.udistrital.interpreter.model.TravelContext;
import co.edu.udistrital.interpreter.view.ConsoleIn;
import co.edu.udistrital.interpreter.view.ConsoleOut;
import co.edu.udistrital.interpreter.view.IIn;
import co.edu.udistrital.interpreter.view.IOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias en JUnit 5 para verificar casos base, casos límite (edge cases),
 * precedencia gramatical y protección de invariantes del patrón Interpreter usando
 * exclusivamente los métodos delegados del Client.
 */
public class InterpreterTest {

    private IRuleParser parser;
    private IStationValidator validator;
    private Client client;

    @BeforeEach
    public void setUp() {
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();
        parser = new BooleanRuleParser();

        IAccessExpression initialTree = new OrExpression(
                new AndExpression(
                        new TerminalTokenExpression("ZONA_NORTE"),
                        new TerminalTokenExpression("HORA_VALLE")
                ),
                new NotExpression(new TerminalTokenExpression("DIA_FESTIVO"))
        );

        validator = new StationValidator(
                "VAL-EST-01",
                "Política Base: (ZONA_NORTE AND HORA_VALLE) OR NOT DIA_FESTIVO",
                initialTree
        );
        client = new Client(validator, parser, in, out);
    }

    // =========================================================================
    // 1. CASOS BASE (HAPPY PATHS)
    // =========================================================================

    @Test
    public void testManualAstEvaluatesNorthZoneOffPeakOrNotHolidayRule() {
        TravelContext northOffPeakHoliday = new TravelContext(
                "TULLAVE-101", "Portal Norte", "ZONA_NORTE", "HORA_VALLE", "DIA_FESTIVO"
        );
        TravelContext southPeakWorkday = new TravelContext(
                "TULLAVE-102", "Portal Sur", "ZONA_SUR", "HORA_PICO", "DIA_HABIL"
        );
        TravelContext southPeakHoliday = new TravelContext(
                "TULLAVE-103", "Restrepo", "ZONA_SUR", "HORA_PICO", "DIA_FESTIVO"
        );

        assertTrue(client.validateTrip(northOffPeakHoliday));
        assertTrue(client.validateTrip(southPeakWorkday));
        assertFalse(client.validateTrip(southPeakHoliday));
    }

    @Test
    public void testDynamicPolicyUpdateViaParserEvaluatesTripsCorrectly() {
        IAccessExpression newTree = client.parseRule(
                "(ZONA_SUR OR ZONA_CENTRO) AND HORA_VALLE AND NOT DIA_FESTIVO"
        );
        client.updatePolicy("Corredor Sur-Centro en Hora Valle", newTree);

        TravelContext centerOffPeakWorkday = new TravelContext(
                "TULLAVE-201", "Museo Nacional", "ZONA_CENTRO", "HORA_VALLE", "DIA_HABIL"
        );
        TravelContext northOffPeakWorkday = new TravelContext(
                "TULLAVE-202", "Calle 100", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"
        );
        TravelContext southOffPeakHoliday = new TravelContext(
                "TULLAVE-203", "Venecia", "ZONA_SUR", "HORA_VALLE", "DIA_FESTIVO"
        );

        assertEquals(
                "(((ZONA_SUR OR ZONA_CENTRO) AND HORA_VALLE) AND (NOT DIA_FESTIVO))",
                newTree.toGrammarString()
        );
        assertTrue(client.validateTrip(centerOffPeakWorkday));
        assertFalse(client.validateTrip(northOffPeakWorkday));
        assertFalse(client.validateTrip(southOffPeakHoliday));
    }

    @Test
    public void testOperatorPrecedenceNotOverAndOverOr() {
        IAccessExpression tree = client.parseRule(
                "ZONA_NORTE OR ZONA_SUR AND NOT DIA_FESTIVO"
        );
        client.updatePolicy("Precedencia NOT > AND > OR", tree);

        assertEquals(
                "(ZONA_NORTE OR (ZONA_SUR AND (NOT DIA_FESTIVO)))",
                tree.toGrammarString()
        );

        TravelContext northOnHoliday = new TravelContext(
                "TULLAVE-301", "Toberín", "ZONA_NORTE", "HORA_PICO", "DIA_FESTIVO"
        );
        TravelContext southOnHoliday = new TravelContext(
                "TULLAVE-302", "Olaya", "ZONA_SUR", "HORA_PICO", "DIA_FESTIVO"
        );
        TravelContext southOnWorkday = new TravelContext(
                "TULLAVE-303", "Olaya", "ZONA_SUR", "HORA_PICO", "DIA_HABIL"
        );

        assertTrue(client.validateTrip(northOnHoliday));
        assertFalse(client.validateTrip(southOnHoliday));
        assertTrue(client.validateTrip(southOnWorkday));
    }

    @Test
    public void testDirectExpressionInterpretationWithoutChangingValidatorPolicy() {
        IAccessExpression customTree = client.parseRule("HORA_NOCTURNA AND FIN_DE_SEMANA");
        TravelContext nightWeekend = new TravelContext(
                "TULLAVE-401", "Banderas", "ZONA_OCCIDENTE", "HORA_NOCTURNA", "FIN_DE_SEMANA"
        );

        assertTrue(client.interpretExpression(customTree, nightWeekend));
        assertTrue(client.showActivePolicy().contains("((ZONA_NORTE AND HORA_VALLE) OR (NOT DIA_FESTIVO))"));
    }

    // =========================================================================
    // 2. CASOS LÍMITE (BOUNDARY / EDGE CASES)
    // =========================================================================

    @Test
    public void testSingleTerminalTokenAndMultipleChainedNegations() {
        IAccessExpression singleToken = client.parseRule("ZONA_NORTE");
        IAccessExpression doubleNegation = client.parseRule("NOT NOT ZONA_NORTE");
        IAccessExpression tripleNegation = client.parseRule("NOT (NOT (NOT ZONA_NORTE))");

        TravelContext northTrip = new TravelContext(
                "TULLAVE-501", "Virrey", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"
        );

        assertTrue(client.interpretExpression(singleToken, northTrip));
        assertTrue(client.interpretExpression(doubleNegation, northTrip));
        assertFalse(client.interpretExpression(tripleNegation, northTrip));
    }

    @Test
    public void testDeeplyNestedParenthesesAndCaseInsensitiveNormalization() {
        IAccessExpression nestedTree = client.parseRule(
                "   ((( (zona_norte)   and   (hora_valle) )))   "
        );
        TravelContext lowercaseContext = new TravelContext(
                "TULLAVE-502", "Calle 85", "zona norte", "hora valle", "dia habil"
        );

        assertEquals("(ZONA_NORTE AND HORA_VALLE)", nestedTree.toGrammarString());
        assertTrue(client.interpretExpression(nestedTree, lowercaseContext));
    }

    // =========================================================================
    // 3. VALIDACIÓN DE INVARIANTES Y MANEJO DE ERRORES
    // =========================================================================

    @Test
    public void testMalformedGrammarRulesThrowException() {
        assertThrows(IllegalArgumentException.class, () -> client.parseRule(null));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("   "));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("()"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("(ZONA_NORTE AND HORA_VALLE"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("ZONA_NORTE AND HORA_VALLE)"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("ZONA_NORTE AND"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("ZONA_NORTE OR"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("AND HORA_VALLE"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("OR HORA_VALLE"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("NOT"));
        assertThrows(IllegalArgumentException.class, () -> client.parseRule("ZONA_NORTE HORA_VALLE"));
    }

    @Test
    public void testInvalidTerminalTokensThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression(null));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("   "));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("AND"));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("or"));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("NOT"));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("("));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression(")"));
        assertThrows(IllegalArgumentException.class, () -> new TerminalTokenExpression("ZONA$NORTE"));
    }

    @Test
    public void testInvalidTravelContextInvariantsThrowException() {
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext(null, "Portal Norte", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"));
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext("   ", "Portal Norte", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"));
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext("TULLAVE-01", "  ", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"));
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext("TULLAVE-01", "Portal Norte", null, "HORA_VALLE", "DIA_HABIL"));
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext("TULLAVE-01", "Portal Norte", "ZONA_NORTE", "", "DIA_HABIL"));
        assertThrows(IllegalArgumentException.class,
                () -> new TravelContext("TULLAVE-01", "Portal Norte", "ZONA_NORTE", "HORA_VALLE", "   "));

        TravelContext validContext = new TravelContext(
                "TULLAVE-01", "Portal Norte", "ZONA_NORTE", "HORA_VALLE", "DIA_HABIL"
        );
        assertThrows(IllegalArgumentException.class, () -> validContext.hasToken(null));
        assertThrows(IllegalArgumentException.class, () -> validContext.hasToken("   "));
    }

    @Test
    public void testNullDependenciesAndOperandsThrowException() {
        IAccessExpression terminal = new TerminalTokenExpression("ZONA_NORTE");

        assertThrows(IllegalArgumentException.class, () -> new AndExpression(null, terminal));
        assertThrows(IllegalArgumentException.class, () -> new AndExpression(terminal, null));
        assertThrows(IllegalArgumentException.class, () -> new OrExpression(null, terminal));
        assertThrows(IllegalArgumentException.class, () -> new OrExpression(terminal, null));
        assertThrows(IllegalArgumentException.class, () -> new NotExpression(null));

        assertThrows(IllegalArgumentException.class, () -> terminal.interpret(null));
        assertThrows(IllegalArgumentException.class, () -> new AndExpression(terminal, terminal).interpret(null));
        assertThrows(IllegalArgumentException.class, () -> new OrExpression(terminal, terminal).interpret(null));
        assertThrows(IllegalArgumentException.class, () -> new NotExpression(terminal).interpret(null));

        assertThrows(IllegalArgumentException.class,
                () -> new StationValidator("  ", "Política", terminal));
        assertThrows(IllegalArgumentException.class,
                () -> validator.updatePolicy("  ", terminal));
        assertThrows(IllegalArgumentException.class,
                () -> validator.updatePolicy("Política", null));
        assertThrows(IllegalArgumentException.class,
                () -> validator.validateAccess(null));

        assertThrows(IllegalArgumentException.class,
                () -> new Client(null, parser, new ConsoleIn(), new ConsoleOut()));
        assertThrows(IllegalArgumentException.class,
                () -> new Client(validator, null, new ConsoleIn(), new ConsoleOut()));
        assertThrows(IllegalArgumentException.class,
                () -> new Client(validator, parser, null, new ConsoleOut()));
        assertThrows(IllegalArgumentException.class,
                () -> new Client(validator, parser, new ConsoleIn(), null));

        assertThrows(IllegalArgumentException.class, () -> client.validateTrip(null));
        assertThrows(IllegalArgumentException.class, () -> client.interpretExpression(null, null));
    }
}
