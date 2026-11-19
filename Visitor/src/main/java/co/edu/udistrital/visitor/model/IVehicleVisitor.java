// Archivo: IVehicleVisitor.java | Paquete: co.edu.udistrital.visitor.model
package co.edu.udistrital.visitor.model;

/**
 * Interfaz Visitor del patrón GoF que declara una operación de visita específica
 * para cada tipo concreto de vehículo sin modificar su jerarquía de clases.
 */
public interface IVehicleVisitor {

    /**
     * Ejecuta la operación correspondiente sobre un vehículo de tipo Bus.
     *
     * @param bus instancia concreta de Bus
     * @return detalle de la operación realizada
     */
    String visit(Bus bus);

    /**
     * Ejecuta la operación correspondiente sobre un vehículo de tipo Uber.
     *
     * @param uber instancia concreta de Uber
     * @return detalle de la operación realizada
     */
    String visit(Uber uber);

    /**
     * Ejecuta la operación correspondiente sobre un vehículo de tipo Motocicleta.
     *
     * @param motorcycle instancia concreta de Motorcycle
     * @return detalle de la operación realizada
     */
    String visit(Motorcycle motorcycle);
}
