// Archivo: IServiceCenterFleet.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

import java.util.List;

/**
 * Interfaz ObjectStructure del patrón Visitor que almacena los vehículos registrados
 * y permite aplicarles operaciones visitantes individual o colectivamente.
 */
public interface IServiceCenterFleet {

    /**
     * Registra un vehículo en la estructura de objetos.
     *
     * @param vehicle vehículo a registrar
     */
    void registerVehicle(IVehicle vehicle);

    /**
     * Busca un vehículo registrado por su placa.
     *
     * @param licensePlate placa del vehículo
     * @return vehículo encontrado
     */
    IVehicle findVehicleByPlate(String licensePlate);

    /**
     * Obtiene la lista inmutable de vehículos registrados.
     *
     * @return lista de vehículos
     */
    List<IVehicle> getVehicles();

    /**
     * Aplica un visitante a un vehículo específico registrado en la estructura.
     *
     * @param licensePlate placa del vehículo
     * @param visitor      operación visitante
     * @return reporte multilínea generado por el visitante
     */
    String applyVisitorToVehicle(String licensePlate, IVehicleVisitor visitor);

    /**
     * Aplica un visitante a todos los vehículos registrados en la estructura.
     *
     * @param visitor operación visitante
     * @return lista de reportes multilínea generados
     */
    List<String> applyVisitorToAll(IVehicleVisitor visitor);
}
