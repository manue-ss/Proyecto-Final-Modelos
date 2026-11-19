// Archivo: GasStationVisitor.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

import java.util.Locale;

/**
 * Visitante concreto (ConcreteVisitor) que representa la Estación de Servicio.
 * Suministra a cada vehículo el combustible específico que le corresponde.
 */
public class GasStationVisitor implements IVehicleVisitor {

    private static final double DEFAULT_DIESEL_PRICE = 11000.0;
    private static final double DEFAULT_GASOLINE_PRICE = 15500.0;
    private static final double DEFAULT_MOTORCYCLE_FUEL_PRICE = 19500.0;

    private final double dieselPricePerGallon;
    private final double gasolinePricePerGallon;
    private final double motorcycleFuelPricePerGallon;

    /**
     * Construye la estación de servicio con tarifas estándar por galón.
     */
    public GasStationVisitor() {
        this(DEFAULT_DIESEL_PRICE, DEFAULT_GASOLINE_PRICE, DEFAULT_MOTORCYCLE_FUEL_PRICE);
    }

    /**
     * Construye la estación de servicio con tarifas personalizadas por galón.
     *
     * @param dieselPricePerGallon         precio por galón de ACPM para Bus (> 0)
     * @param gasolinePricePerGallon       precio por galón de Gasolina para Uber (> 0)
     * @param motorcycleFuelPricePerGallon precio por galón de combustible para Motocicleta (> 0)
     */
    public GasStationVisitor(
            double dieselPricePerGallon,
            double gasolinePricePerGallon,
            double motorcycleFuelPricePerGallon) {

        if (dieselPricePerGallon <= 0 || gasolinePricePerGallon <= 0 || motorcycleFuelPricePerGallon <= 0) {
            throw new IllegalArgumentException("Los precios por galón en la estación deben ser mayores que cero.");
        }
        this.dieselPricePerGallon = dieselPricePerGallon;
        this.gasolinePricePerGallon = gasolinePricePerGallon;
        this.motorcycleFuelPricePerGallon = motorcycleFuelPricePerGallon;
    }

    @Override
    public String visit(Bus bus) {
        if (bus == null) {
            throw new IllegalArgumentException("El bus no puede ser nulo.");
        }
        double missing = bus.getMissingFuel();
        if (missing <= 0) {
            throw new IllegalArgumentException("El tanque del bus " + bus.getLicensePlate() + " ya está lleno.");
        }
        bus.loadFuel(missing);
        double total = missing * dieselPricePerGallon;
        return String.format(
                Locale.US,
                "  Servicio            : Estación de Servicio%n"
                + "  Vehículo atendido   : Bus (%s)%n"
                + "  Combustible         : %s%n"
                + "  Galones suministr.  : %.2f gal%n"
                + "  Precio por galón    : $%.2f%n"
                + "  Total facturado     : $%.2f",
                bus.getLicensePlate(),
                bus.getDieselSpecification(),
                missing,
                dieselPricePerGallon,
                total
        );
    }

    @Override
    public String visit(Uber uber) {
        if (uber == null) {
            throw new IllegalArgumentException("El vehículo Uber no puede ser nulo.");
        }
        double missing = uber.getMissingFuel();
        if (missing <= 0) {
            throw new IllegalArgumentException("El tanque del Uber " + uber.getLicensePlate() + " ya está lleno.");
        }
        uber.loadFuel(missing);
        double total = missing * gasolinePricePerGallon;
        return String.format(
                Locale.US,
                "  Servicio            : Estación de Servicio%n"
                + "  Vehículo atendido   : Uber (%s)%n"
                + "  Combustible         : %s%n"
                + "  Galones suministr.  : %.2f gal%n"
                + "  Precio por galón    : $%.2f%n"
                + "  Total facturado     : $%.2f",
                uber.getLicensePlate(),
                uber.getGasolineSpecification(),
                missing,
                gasolinePricePerGallon,
                total
        );
    }

    @Override
    public String visit(Motorcycle motorcycle) {
        if (motorcycle == null) {
            throw new IllegalArgumentException("La motocicleta no puede ser nula.");
        }
        double missing = motorcycle.getMissingFuel();
        if (missing <= 0) {
            throw new IllegalArgumentException(
                    "El tanque de la motocicleta " + motorcycle.getLicensePlate() + " ya está lleno.");
        }
        motorcycle.loadFuel(missing);
        double total = missing * motorcycleFuelPricePerGallon;
        return String.format(
                Locale.US,
                "  Servicio            : Estación de Servicio%n"
                + "  Vehículo atendido   : Motocicleta (%s)%n"
                + "  Combustible         : %s%n"
                + "  Galones suministr.  : %.2f gal%n"
                + "  Precio por galón    : $%.2f%n"
                + "  Total facturado     : $%.2f",
                motorcycle.getLicensePlate(),
                motorcycle.getMotorcycleFuelSpecification(),
                missing,
                motorcycleFuelPricePerGallon,
                total
        );
    }
}
