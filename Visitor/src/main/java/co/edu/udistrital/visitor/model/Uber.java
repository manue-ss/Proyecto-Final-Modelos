// Archivo: Uber.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

/**
 * Elemento concreto (ConcreteElement) que representa un vehículo de servicio Uber.
 */
public class Uber extends AbstractVehicle {

    private static final int UBER_WHEELS = 4;

    /**
     * Construye un automóvil Uber con 4 ruedas totales.
     *
     * @param licensePlate placa del automóvil
     * @param tankCapacity capacidad del tanque en galones
     * @param currentFuel  combustible actual en galones
     * @param wornWheels   cantidad de ruedas desgastadas (0 a 4)
     */
    public Uber(String licensePlate, double tankCapacity, double currentFuel, int wornWheels) {
        super(licensePlate, tankCapacity, currentFuel, UBER_WHEELS, wornWheels);
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
        return "Uber";
    }

    /**
     * Obtiene el tipo de combustible específico que utiliza el Uber.
     *
     * @return tipo de gasolina para automóvil
     */
    public String getGasolineSpecification() {
        return "Gasolina Corriente";
    }

    /**
     * Obtiene la especificación de rueda urbana que el taller le vende al Uber.
     *
     * @return referencia de rueda para Uber
     */
    public String getUrbanWheelSpecification() {
        return "Rueda Urbana Automóvil (Rin 15)";
    }
}
