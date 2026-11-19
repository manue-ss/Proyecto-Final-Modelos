// Archivo: Main.java | Paquete: co.edu.udistrital.visitor
package co.edu.udistrital.visitor;

import co.edu.udistrital.visitor.controller.Client;
import co.edu.udistrital.visitor.model.Bus;
import co.edu.udistrital.visitor.model.GasStationVisitor;
import co.edu.udistrital.visitor.model.IServiceCenterFleet;
import co.edu.udistrital.visitor.model.IVehicle;
import co.edu.udistrital.visitor.model.IVehicleVisitor;
import co.edu.udistrital.visitor.model.MechanicWorkshopVisitor;
import co.edu.udistrital.visitor.model.Motorcycle;
import co.edu.udistrital.visitor.model.ServiceCenterFleet;
import co.edu.udistrital.visitor.model.Uber;
import co.edu.udistrital.visitor.view.ConsoleIn;
import co.edu.udistrital.visitor.view.ConsoleOut;
import co.edu.udistrital.visitor.view.IIn;
import co.edu.udistrital.visitor.view.IOut;

/**
 * Punto de entrada interactivo (Composition Root) que ensambla los elementos,
 * solicita los datos mediante los métodos delegados del Client y ejecuta el
 * patrón Visitor.
 */
public class Main {

    public static void main(String[] args) {
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();
        IServiceCenterFleet fleet = new ServiceCenterFleet();
        Client client = new Client(fleet, in, out);

        IVehicleVisitor gasStation = new GasStationVisitor();
        IVehicleVisitor workshop = new MechanicWorkshopVisitor();

        client.showLine("=======================================================");
        client.showLine("   ESTACIÓN DE SERVICIO Y TALLER MECÁNICO (VISITOR)");
        client.showLine("=======================================================");

        boolean running = true;
        while (running) {
            try {
                client.showLine("\nMenú Principal:");
                client.showLine(
                        "1. Registrar un nuevo vehículo (Bus, Uber o Motocicleta)");
                client.showLine(
                        "2. Atender vehículo en Estación de Servicio (por placa)");
                client.showLine(
                        "3. Atender vehículo en Taller Mecánico (por placa)");
                client.showLine(
                        "4. Atender toda la flota registrada en Estación de Servicio");
                client.showLine(
                        "5. Atender toda la flota registrada en Taller Mecánico");
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
                        client.showLine("\nTipo de vehículo:");
                        client.showLine("1. Bus (6 ruedas)");
                        client.showLine("2. Uber (4 ruedas)");
                        client.showLine("3. Motocicleta (2 ruedas)");
                        client.show("Seleccione tipo: ");
                        int typeOption = client.nextInt();

                        client.show("Ingrese la placa: ");
                        String plate = client.nextLine();

                        client.show("Ingrese la capacidad del tanque (galones): ");
                        double tankCapacity = client.nextDouble();

                        client.show("Ingrese el combustible actual (galones): ");
                        double currentFuel = client.nextDouble();

                        client.show("Ingrese la cantidad de ruedas desgastadas: ");
                        int wornWheels = client.nextInt();

                        IVehicle vehicle;
                        switch (typeOption) {
                            case 1:
                                vehicle = new Bus(plate, tankCapacity,
                                        currentFuel, wornWheels);
                                break;
                            case 2:
                                vehicle = new Uber(plate, tankCapacity,
                                        currentFuel, wornWheels);
                                break;
                            case 3:
                                vehicle = new Motorcycle(plate, tankCapacity,
                                        currentFuel, wornWheels);
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Tipo de vehículo inválido. Seleccione 1, 2 o 3.");
                        }
                        client.registerVehicle(vehicle);
                        break;

                    case 2:
                        client.show("Ingrese la placa del vehículo a tanquear: ");
                        String fuelPlate = client.nextLine();
                        client.executeVisitor(fuelPlate, gasStation);
                        break;

                    case 3:
                        client.show(
                                "Ingrese la placa del vehículo para cambio de ruedas: ");
                        String workshopPlate = client.nextLine();
                        client.executeVisitor(workshopPlate, workshop);
                        break;

                    case 4:
                        client.executeVisitorOnAll(gasStation);
                        break;

                    case 5:
                        client.executeVisitorOnAll(workshop);
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "Opción de menú fuera de rango (0 - 5).");
                }
            }
            catch (IllegalArgumentException e) {
                client.showError(e.getMessage());
            }
        }

        client.close();
    }
}
