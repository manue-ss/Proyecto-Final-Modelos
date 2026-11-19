// Archivo: AbstractVehicle.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

import java.util.Locale;

/**
 * Clase abstracta que encapsula el estado común y protege las invariantes
 * de negocio de cualquier vehículo atendido en la estación o en el taller.
 */
public abstract class AbstractVehicle implements IVehicle {

    private static final double EPSILON = 0.0001;

    private final String licensePlate;
    private final double tankCapacity;
    private double currentFuel;
    private final int totalWheels;
    private int wornWheels;

    /**
     * Construye un vehículo validando sus invariantes de dominio.
     *
     * @param licensePlate placa única del vehículo
     * @param tankCapacity capacidad máxima del tanque en galones (> 0)
     * @param currentFuel  combustible actual en galones (0 <= actual <= capacidad)
     * @param totalWheels  cantidad total de ruedas del vehículo (> 0)
     * @param wornWheels   cantidad de ruedas desgastadas (0 <= desgastadas <= totalWheels)
     */
    protected AbstractVehicle(
            String licensePlate,
            double tankCapacity,
            double currentFuel,
            int totalWheels,
            int wornWheels) {

        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa del vehículo no puede ser nula ni vacía.");
        }
        if (tankCapacity <= 0) {
            throw new IllegalArgumentException("La capacidad del tanque debe ser mayor que cero.");
        }
        if (currentFuel < 0 || currentFuel > tankCapacity) {
            throw new IllegalArgumentException(
                    "El combustible actual debe estar entre 0 y la capacidad del tanque ("
                            + tankCapacity + " gal).");
        }
        if (totalWheels <= 0) {
            throw new IllegalArgumentException("La cantidad total de ruedas debe ser mayor que cero.");
        }
        if (wornWheels < 0 || wornWheels > totalWheels) {
            throw new IllegalArgumentException(
                    "Las ruedas desgastadas deben estar entre 0 y " + totalWheels + ".");
        }

        this.licensePlate = licensePlate.trim().toUpperCase();
        this.tankCapacity = tankCapacity;
        this.currentFuel = currentFuel;
        this.totalWheels = totalWheels;
        this.wornWheels = wornWheels;
    }

    /**
     * Suministra combustible al tanque validando que no supere su capacidad máxima.
     *
     * @param gallons cantidad de galones a cargar
     */
    public void loadFuel(double gallons) {
        if (gallons <= 0) {
            throw new IllegalArgumentException("La cantidad de galones a suministrar debe ser mayor que cero.");
        }
        if (currentFuel + gallons > tankCapacity + EPSILON) {
            throw new IllegalArgumentException("El suministro excede la capacidad disponible del tanque.");
        }
        this.currentFuel = Math.min(tankCapacity, this.currentFuel + gallons);
    }

    /**
     * Reemplaza la cantidad indicada de ruedas desgastadas por ruedas nuevas.
     *
     * @param wheelsToReplace cantidad de ruedas a reemplazar
     */
    public void replaceWornWheels(int wheelsToReplace) {
        if (wheelsToReplace <= 0) {
            throw new IllegalArgumentException("La cantidad de ruedas a reemplazar debe ser mayor que cero.");
        }
        if (wheelsToReplace > wornWheels) {
            throw new IllegalArgumentException("No se pueden reemplazar más ruedas de las que están desgastadas.");
        }
        this.wornWheels -= wheelsToReplace;
    }

    @Override
    public String getLicensePlate() {
        return licensePlate;
    }

    @Override
    public double getTankCapacity() {
        return tankCapacity;
    }

    @Override
    public double getCurrentFuel() {
        return currentFuel;
    }

    @Override
    public double getMissingFuel() {
        double missing = tankCapacity - currentFuel;
        return missing < EPSILON ? 0.0 : missing;
    }

    @Override
    public int getTotalWheels() {
        return totalWheels;
    }

    @Override
    public int getWornWheels() {
        return wornWheels;
    }

    @Override
    public String formatState() {
        return String.format(
                Locale.US,
                "  Tipo                : %s%n"
                + "  Placa               : %s%n"
                + "  Combustible         : %.2f / %.2f gal%n"
                + "  Ruedas desgastadas  : %d / %d",
                getVehicleType(),
                licensePlate,
                currentFuel,
                tankCapacity,
                wornWheels,
                totalWheels
        );
    }
}
