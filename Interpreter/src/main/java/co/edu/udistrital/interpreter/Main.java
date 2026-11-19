// Archivo: Main.java | Paquete: co.edu.udistrital.interpreter
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

/**
 * Punto de entrada interactivo (Composition Root) que ensambla las dependencias,
 * solicita los datos al usuario mediante los métodos delegados del Client y ejecuta el patrón Interpreter.
 */
public class Main {

    /**
     * Método principal netamente interactivo encargado de ensamblar y solicitar datos al usuario.
     *
     * @param args argumentos de línea de comandos
     */
    public static void main(String[] args) {
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();
        IRuleParser parser = new BooleanRuleParser();

        IAccessExpression initialRule = new OrExpression(
                new AndExpression(
                        new TerminalTokenExpression("ZONA_NORTE"),
                        new TerminalTokenExpression("HORA_VALLE")
                ),
                new NotExpression(new TerminalTokenExpression("DIA_FESTIVO"))
        );

        IStationValidator validator = new StationValidator(
                "VAL-EST-01",
                "Política Base Zona Norte / No Festivo",
                initialRule
        );

        Client client = new Client(validator, parser, in, out);

        client.showLine("=======================================================");
        client.showLine("   VALIDADOR DINÁMICO DE ESTACIONES (INTERPRETER)");
        client.showLine("=======================================================");

        boolean running = true;
        while (running) {
            try {
                client.showLine("\nMenú Principal:");
                client.showLine("1. Consultar la política tarifaria activa en el validador");
                client.showLine("2. Actualizar la política del validador ingresando una regla booleana");
                client.showLine("3. Validar el acceso de un viaje con la política activa");
                client.showLine("4. Interpretar una expresión booleana libre sobre un viaje");
                client.showLine("0. Salir");
                client.show("Seleccione una opción: ");
                int option = client.nextInt();

                if (option == 0) {
                    client.showLine("Finalizando programa...");
                    running = false;
                    continue;
                }

                switch (option) {
                    case 1:
                        client.showActivePolicy();
                        break;

                    case 2:
                        client.show("Ingrese el nombre de la nueva política tarifaria: ");
                        String policyName = client.nextLine();

                        client.showLine("Operadores soportados: AND, OR, NOT y paréntesis ( )");
                        client.showLine("Ejemplo: (ZONA_NORTE AND HORA_VALLE) OR NOT DIA_FESTIVO");
                        client.show("Ingrese la regla booleana: ");
                        String ruleText = client.nextLine();

                        IAccessExpression parsedTree = client.parseRule(ruleText);
                        client.updatePolicy(policyName, parsedTree);
                        break;

                    case 3:
                        client.show("Ingrese el código de la tarjeta (ej. TULLAVE-101): ");
                        String cardCode = client.nextLine();

                        client.show("Ingrese el nombre de la estación (ej. Portal Norte): ");
                        String stationName = client.nextLine();

                        client.show("Ingrese la zona tarifaria (ej. ZONA_NORTE, ZONA_SUR, ZONA_CENTRO): ");
                        String fareZone = client.nextLine();

                        client.show("Ingrese la franja horaria (ej. HORA_VALLE, HORA_PICO, HORA_NOCTURNA): ");
                        String timeSlot = client.nextLine();

                        client.show("Ingrese el tipo de día (ej. DIA_HABIL, DIA_FESTIVO, FIN_DE_SEMANA): ");
                        String dayType = client.nextLine();

                        TravelContext context = new TravelContext(
                                cardCode, stationName, fareZone, timeSlot, dayType
                        );
                        client.validateTrip(context);
                        break;

                    case 4:
                        client.show("Ingrese la expresión booleana a evaluar: ");
                        String customRuleText = client.nextLine();
                        IAccessExpression customExpression = client.parseRule(customRuleText);

                        client.show("Ingrese el código de la tarjeta: ");
                        String customCard = client.nextLine();

                        client.show("Ingrese el nombre de la estación: ");
                        String customStation = client.nextLine();

                        client.show("Ingrese la zona tarifaria: ");
                        String customZone = client.nextLine();

                        client.show("Ingrese la franja horaria: ");
                        String customTimeSlot = client.nextLine();

                        client.show("Ingrese el tipo de día: ");
                        String customDayType = client.nextLine();

                        TravelContext customContext = new TravelContext(
                                customCard, customStation, customZone, customTimeSlot, customDayType
                        );
                        client.interpretExpression(customExpression, customContext);
                        break;

                    default:
                        throw new IllegalArgumentException("Opción de menú fuera de rango (0 - 4).");
                }
            } catch (IllegalArgumentException e) {
                client.showError(e.getMessage());
            }
        }

        client.close();
    }
}
