// Archivo: Client.java | Paquete: co.edu.udistrital.visitor.controller
package co.edu.udistrital.visitor.controller;

import co.edu.udistrital.visitor.model.IServiceCenterFleet;
import co.edu.udistrital.visitor.model.IVehicle;
import co.edu.udistrital.visitor.model.IVehicleVisitor;
import co.edu.udistrital.visitor.view.IIn;
import co.edu.udistrital.visitor.view.IOut;

import java.util.List;

/**
 * Controlador de Caja Negra que expone métodos de delegación pura sobre la estructura
 * de vehículos (IServiceCenterFleet) y las abstracciones de Entrada/Salida.
 */
public class Client {

    private final IServiceCenterFleet fleet;
    private final IIn in;
    private final IOut out;

    public Client(IServiceCenterFleet fleet, IIn in, IOut out) {
        if (fleet == null || in == null || out == null) {
            throw new IllegalArgumentException(
                    "Las dependencias inyectadas en Client no pueden ser nulas.");
        }
        this.fleet = fleet;
        this.in = in;
        this.out = out;
    }

    public void registerVehicle(IVehicle vehicle) {
        fleet.registerVehicle(vehicle);
        out.showLine("-------------------------------------------------------");
        out.showLine("Vehículo registrado exitosamente:");
        out.showLine(vehicle.formatState());
        out.showLine("-------------------------------------------------------");
    }
  
    public String executeVisitor(String licensePlate, IVehicleVisitor visitor) {
        IVehicle vehicle = fleet.findVehicleByPlate(licensePlate);
        out.showLine("-------------------------------------------------------");
        out.showLine("[ESTADO INICIAL]");
        out.showLine(vehicle.formatState());

        String result = fleet.applyVisitorToVehicle(licensePlate, visitor);

        out.showLine("\n[DETALLE DE LA OPERACIÓN]");
        out.showLine(result);

        out.showLine("\n[ESTADO FINAL]");
        out.showLine(vehicle.formatState());
        out.showLine("-------------------------------------------------------");
        return result;
    }

    public List<String> executeVisitorOnAll(IVehicleVisitor visitor) {
        List<String> reports = fleet.applyVisitorToAll(visitor);
        out.showLine("-------------------------------------------------------");
        out.showLine("[EJECUCIÓN SOBRE TODA LA ESTRUCTURA DE VEHÍCULOS]");
        for (String report : reports) {
            out.showLine(report);
            out.showLine("-------------------------------------------------------");
        }
        return reports;
    }

    public void show(String message) {
        out.show(message);
    }

    public void showLine(String message) {
        out.showLine(message);
    }

    public void showError(String errorMessage) {
        out.showError(errorMessage);
    }

    public String nextLine() {
        return in.nextLine();
    }

    public int nextInt() {
        return in.nextInt();
    }

    public double nextDouble() {
        return in.nextDouble();
    }

    public void close() {
        in.close();
    }
}
