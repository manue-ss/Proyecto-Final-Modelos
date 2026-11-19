// Archivo: IVehicle.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

/**
 * Interfaz Element del patrón Visitor que define el contrato de un vehículo
 * capaz de aceptar operaciones externas mediante doble despacho (Double Dispatch).
 */
public interface IVehicle {

    /**
     * Acepta un visitante y le redirige la llamada según el tipo concreto del vehículo.
     *
     * @param visitor operación visitante a ejecutar
     * @return resultado textual de la operación aplicada
     */
    String accept(IVehicleVisitor visitor);

    /**
     * Obtiene la placa del vehículo.
     *
     * @return placa del vehículo
     */
    String getLicensePlate();

    /**
     * Obtiene el nombre del tipo de vehículo.
     *
     * @return tipo de vehículo
     */
    String getVehicleType();

    /**
     * Obtiene la capacidad total del tanque en galones.
     *
     * @return capacidad máxima del tanque
     */
    double getTankCapacity();

    /**
     * Obtiene el nivel actual de combustible en galones.
     *
     * @return combustible actual
     */
    double getCurrentFuel();

    /**
     * Calcula los galones faltantes para llenar el tanque.
     *
     * @return galones faltantes
     */
    double getMissingFuel();

    /**
     * Obtiene el número total de ruedas del vehículo.
     *
     * @return cantidad total de ruedas
     */
    int getTotalWheels();

    /**
     * Obtiene la cantidad de ruedas desgastadas pendientes de cambio.
     *
     * @return cantidad de ruedas desgastadas
     */
    int getWornWheels();

    /**
     * Devuelve el resumen formateado del estado actual del vehículo.
     *
     * @return representación textual del estado
     */
    String formatState();
}
