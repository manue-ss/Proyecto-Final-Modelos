// Archivo: MechanicWorkshopVisitor.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

import java.util.Locale;

/**
 * Visitante concreto (ConcreteVisitor) que representa el Taller Mecánico.
 * Vende e instala las piezas específicas (ruedas) que corresponden a cada vehículo.
 */
public class MechanicWorkshopVisitor implements IVehicleVisitor {

    private static final double DEFAULT_BUS_WHEEL_PRICE = 1400000.0;
    private static final double DEFAULT_UBER_WHEEL_PRICE = 350000.0;
    private static final double DEFAULT_MOTORCYCLE_WHEEL_PRICE = 220000.0;

    private final double busWheelPrice;
    private final double uberWheelPrice;
    private final double motorcycleWheelPrice;

    /**
     * Construye el taller mecánico con precios estándar por rueda para cada tipo de vehículo.
     */
    public MechanicWorkshopVisitor() {
        this(DEFAULT_BUS_WHEEL_PRICE, DEFAULT_UBER_WHEEL_PRICE, DEFAULT_MOTORCYCLE_WHEEL_PRICE);
    }

    /**
     * Construye el taller mecánico con precios personalizados por rueda.
     *
     * @param busWheelPrice        precio unitario de rueda para Bus (> 0)
     * @param uberWheelPrice       precio unitario de rueda para Uber (> 0)
     * @param motorcycleWheelPrice precio unitario de rueda para Motocicleta (> 0)
     */
    public MechanicWorkshopVisitor(double busWheelPrice, double uberWheelPrice, double motorcycleWheelPrice) {
        if (busWheelPrice <= 0 || uberWheelPrice <= 0 || motorcycleWheelPrice <= 0) {
            throw new IllegalArgumentException("Los precios de las ruedas en el taller deben ser mayores que cero.");
        }
        this.busWheelPrice = busWheelPrice;
        this.uberWheelPrice = uberWheelPrice;
        this.motorcycleWheelPrice = motorcycleWheelPrice;
    }

    @Override
    public String visit(Bus bus) {
        if (bus == null) {
            throw new IllegalArgumentException("El bus no puede ser nulo.");
        }
        int worn = bus.getWornWheels();
        if (worn <= 0) {
            throw new IllegalArgumentException("El bus " + bus.getLicensePlate() + " no requiere cambio de ruedas.");
        }
        bus.replaceWornWheels(worn);
        double total = worn * busWheelPrice;
        return String.format(
                Locale.US,
                "  Servicio            : Taller Mecánico%n"
                + "  Vehículo atendido   : Bus (%s)%n"
                + "  Pieza vendida       : %s%n"
                + "  Cantidad instalada  : %d unidad(es)%n"
                + "  Precio unitario     : $%.2f%n"
                + "  Total facturado     : $%.2f",
                bus.getLicensePlate(),
                bus.getHeavyWheelSpecification(),
                worn,
                busWheelPrice,
                total
        );
    }

    @Override
    public String visit(Uber uber) {
        if (uber == null) {
            throw new IllegalArgumentException("El vehículo Uber no puede ser nulo.");
        }
        int worn = uber.getWornWheels();
        if (worn <= 0) {
            throw new IllegalArgumentException("El Uber " + uber.getLicensePlate() + " no requiere cambio de ruedas.");
        }
        uber.replaceWornWheels(worn);
        double total = worn * uberWheelPrice;
        return String.format(
                Locale.US,
                "  Servicio            : Taller Mecánico%n"
                + "  Vehículo atendido   : Uber (%s)%n"
                + "  Pieza vendida       : %s%n"
                + "  Cantidad instalada  : %d unidad(es)%n"
                + "  Precio unitario     : $%.2f%n"
                + "  Total facturado     : $%.2f",
                uber.getLicensePlate(),
                uber.getUrbanWheelSpecification(),
                worn,
                uberWheelPrice,
                total
        );
    }

    @Override
    public String visit(Motorcycle motorcycle) {
        if (motorcycle == null) {
            throw new IllegalArgumentException("La motocicleta no puede ser nula.");
        }
        int worn = motorcycle.getWornWheels();
        if (worn <= 0) {
            throw new IllegalArgumentException(
                    "La motocicleta " + motorcycle.getLicensePlate() + " no requiere cambio de ruedas.");
        }
        motorcycle.replaceWornWheels(worn);
        double total = worn * motorcycleWheelPrice;
        return String.format(
                Locale.US,
                "  Servicio            : Taller Mecánico%n"
                + "  Vehículo atendido   : Motocicleta (%s)%n"
                + "  Pieza vendida       : %s%n"
                + "  Cantidad instalada  : %d unidad(es)%n"
                + "  Precio unitario     : $%.2f%n"
                + "  Total facturado     : $%.2f",
                motorcycle.getLicensePlate(),
                motorcycle.getMotorcycleWheelSpecification(),
                worn,
                motorcycleWheelPrice,
                total
        );
    }
}
