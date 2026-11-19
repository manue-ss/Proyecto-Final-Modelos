// Archivo: Motorcycle.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

/**
 * Elemento concreto (ConcreteElement) que representa una Motocicleta.
 */
public class Motorcycle extends AbstractVehicle {

    private static final int MOTORCYCLE_WHEELS = 2;

    /**
     * Construye una Motocicleta con 2 ruedas totales.
     *
     * @param licensePlate placa de la motocicleta
     * @param tankCapacity capacidad del tanque en galones
     * @param currentFuel  combustible actual en galones
     * @param wornWheels   cantidad de ruedas desgastadas (0 a 2)
     */
    public Motorcycle(String licensePlate, double tankCapacity, double currentFuel, int wornWheels) {
        super(licensePlate, tankCapacity, currentFuel, MOTORCYCLE_WHEELS, wornWheels);
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
        return "Motocicleta";
    }

    /**
     * Obtiene el tipo de combustible específico que utiliza la Motocicleta.
     *
     * @return tipo de combustible para motocicleta
     */
    public String getMotorcycleFuelSpecification() {
        return "Gasolina Extra para Motocicleta";
    }

    /**
     * Obtiene la especificación de rueda que el taller le vende a la Motocicleta.
     *
     * @return referencia de rueda para Motocicleta
     */
    public String getMotorcycleWheelSpecification() {
        return "Rueda Pistera de Motocicleta (Rin 17)";
    }
}
