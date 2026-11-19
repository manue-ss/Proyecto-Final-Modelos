// Archivo: Bus.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

/**
 * Elemento concreto (ConcreteElement) que representa un Bus de transporte.
 */
public class Bus extends AbstractVehicle {

    private static final int BUS_WHEELS = 6;

    /**
     * Construye un Bus con 6 ruedas totales.
     *
     * @param licensePlate placa del bus
     * @param tankCapacity capacidad del tanque en galones
     * @param currentFuel  combustible actual en galones
     * @param wornWheels   cantidad de ruedas desgastadas (0 a 6)
     */
    public Bus(String licensePlate, double tankCapacity, double currentFuel, int wornWheels) {
        super(licensePlate, tankCapacity, currentFuel, BUS_WHEELS, wornWheels);
    }

    @Override
    public String accept(IVehicleVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("El visitante no puede ser nulo.");
        }
        return visitor.visit(this);
    }

    @Override
    public String getVehicleType() {
        return "Bus";
    }

    /**
     * Obtiene el tipo de combustible específico que utiliza el Bus.
     *
     * @return tipo de combustible diésel
     */
    public String getDieselSpecification() {
        return "ACPM Diésel Pesado";
    }

    /**
     * Obtiene la especificación de rueda de carga para Bus que vende el taller.
     *
     * @return referencia de rueda para Bus
     */
    public String getHeavyWheelSpecification() {
        return "Rueda Radial de Carga (Rin 22.5)";
    }
}
