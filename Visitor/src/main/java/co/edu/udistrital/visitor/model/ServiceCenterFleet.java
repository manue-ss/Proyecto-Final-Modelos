// Archivo: ServiceCenterFleet.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Implementación concreta del participante ObjectStructure del patrón Visitor.
 * Contiene la colección polimórfica de vehículos (Buses, Ubers y Motocicletas).
 */
public class ServiceCenterFleet implements IServiceCenterFleet {

    private final List<IVehicle> vehicles;

    /**
     * Construye una estructura de flota vacía.
     */
    public ServiceCenterFleet() {
        this.vehicles = new ArrayList<>();
    }

    @Override
    public void registerVehicle(IVehicle vehicle) {
        if (vehicle == null) {
            throw new IllegalArgumentException("No se puede registrar un vehículo nulo.");
        }
        for (IVehicle existing : vehicles) {
            if (existing.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
                throw new IllegalArgumentException(
                        "Ya existe un vehículo registrado con la placa " + vehicle.getLicensePlate() + ".");
            }
        }
        vehicles.add(vehicle);
    }

    @Override
    public IVehicle findVehicleByPlate(String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("La placa de búsqueda no puede ser nula ni vacía.");
        }
        String normalized = licensePlate.trim().toUpperCase();
        for (IVehicle vehicle : vehicles) {
            if (vehicle.getLicensePlate().equalsIgnoreCase(normalized)) {
                return vehicle;
            }
        }
        throw new IllegalArgumentException("No se encontró un vehículo con la placa " + normalized + ".");
    }

    @Override
    public List<IVehicle> getVehicles() {
        return Collections.unmodifiableList(vehicles);
    }

    @Override
    public String applyVisitorToVehicle(String licensePlate, IVehicleVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("El visitante no puede ser nulo.");
        }
        IVehicle vehicle = findVehicleByPlate(licensePlate);
        return vehicle.accept(visitor);
    }

    @Override
    public List<String> applyVisitorToAll(IVehicleVisitor visitor) {
        if (visitor == null) {
            throw new IllegalArgumentException("El visitante no puede ser nulo.");
        }
        if (vehicles.isEmpty()) {
            throw new IllegalArgumentException("No hay vehículos registrados en la estructura.");
        }
        List<String> reports = new ArrayList<>();
        for (IVehicle vehicle : vehicles) {
            reports.add(vehicle.accept(visitor));
        }
        return reports;
    }
}
