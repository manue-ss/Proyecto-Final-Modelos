// Archivo: VisitorTest.java | Paquete: co.edu.udistrital.visitor
package co.edu.udistrital.visitor;

import co.edu.udistrital.visitor.controller.Client;
import co.edu.udistrital.visitor.model.Bus;
import co.edu.udistrital.visitor.model.GasStationVisitor;
import co.edu.udistrital.visitor.model.IServiceCenterFleet;
import co.edu.udistrital.visitor.model.IVehicle;
import co.edu.udistrital.visitor.model.IVehicleVisitor;
import co.edu.udistrital.visitor.model.MechanicWorkshopVisitor;
import co.edu.udistrital.visitor.model.Motorcycle;
import co.edu.udistrital.visitor.model.ServiceCenterFleet;
import co.edu.udistrital.visitor.model.Uber;
import co.edu.udistrital.visitor.view.ConsoleIn;
import co.edu.udistrital.visitor.view.ConsoleOut;
import co.edu.udistrital.visitor.view.IIn;
import co.edu.udistrital.visitor.view.IOut;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias en JUnit 5 para verificar casos base, casos límite (edge cases),
 * protección de invariantes y extensibilidad del patrón Visitor usando los métodos
 * delegados del Client y la estructura contenedora IServiceCenterFleet.
 */
public class VisitorTest {

    private IServiceCenterFleet fleet;
    private Client client;
    private IVehicleVisitor gasStation;
    private IVehicleVisitor workshop;

    @BeforeEach
    public void setUp() {
        IIn in = new ConsoleIn();
        IOut out = new ConsoleOut();
        fleet = new ServiceCenterFleet();
        client = new Client(fleet, in, out);
        gasStation = new GasStationVisitor(10000.0, 15000.0, 20000.0);
        workshop = new MechanicWorkshopVisitor(1200000.0, 300000.0, 200000.0);
    }

    // =========================================================================
    // 1. CASOS BASE (HAPPY PATHS)
    // =========================================================================

    @Test
    public void testGasStationSuppliesDieselToBus() {
        IVehicle bus = new Bus("BUS123", 60.0, 20.0, 2);
        client.registerVehicle(bus);

        String report = client.executeVisitor("BUS123", gasStation);

        assertEquals(60.0, bus.getCurrentFuel(), 0.0001);
        assertEquals(0.0, bus.getMissingFuel(), 0.0001);
        assertTrue(report.contains("ACPM Diésel Pesado"));
        assertTrue(report.contains("$400000.00"));
        assertTrue(report.contains("\n"));
    }

    @Test
    public void testGasStationSuppliesGasolineToUber() {
        IVehicle uber = new Uber("UBR456", 12.0, 4.0, 1);
        client.registerVehicle(uber);

        String report = client.executeVisitor("UBR456", gasStation);

        assertEquals(12.0, uber.getCurrentFuel(), 0.0001);
        assertTrue(report.contains("Gasolina Corriente"));
        assertTrue(report.contains("$120000.00"));
    }

    @Test
    public void testGasStationSuppliesSpecificFuelToMotorcycle() {
        IVehicle moto = new Motorcycle("MOT789", 4.0, 1.5, 1);
        client.registerVehicle(moto);

        String report = client.executeVisitor("MOT789", gasStation);

        assertEquals(4.0, moto.getCurrentFuel(), 0.0001);
        assertTrue(report.contains("Gasolina Extra para Motocicleta"));
        assertTrue(report.contains("$50000.00"));
    }

    @Test
    public void testWorkshopSellsSpecificWheelsToEachVehicleType() {
        IVehicle bus = new Bus("BUS123", 60.0, 30.0, 3);
        IVehicle uber = new Uber("UBR456", 12.0, 6.0, 2);
        IVehicle moto = new Motorcycle("MOT789", 4.0, 2.0, 1);

        client.registerVehicle(bus);
        client.registerVehicle(uber);
        client.registerVehicle(moto);

        String busReport = client.executeVisitor("BUS123", workshop);
        String uberReport = client.executeVisitor("UBR456", workshop);
        String motoReport = client.executeVisitor("MOT789", workshop);

        assertEquals(0, bus.getWornWheels());
        assertEquals(0, uber.getWornWheels());
        assertEquals(0, moto.getWornWheels());

        assertTrue(busReport.contains("Rueda Radial de Carga (Rin 22.5)"));
        assertTrue(busReport.contains("$3600000.00"));

        assertTrue(uberReport.contains("Rueda Urbana Automóvil (Rin 15)"));
        assertTrue(uberReport.contains("$600000.00"));

        assertTrue(motoReport.contains("Rueda Pistera de Motocicleta (Rin 17)"));
        assertTrue(motoReport.contains("$200000.00"));
    }

    @Test
    public void testNewOperationAddedToEntireFleetWithoutModifyingVehicleClasses() {
        IVehicleVisitor technicalInspectionVisitor = new IVehicleVisitor() {
            @Override
            public String visit(Bus bus) {
                return String.format(
                        "  Servicio            : Inspección CDA%n"
                        + "  Vehículo            : Bus (%s)%n"
                        + "  Dictamen            : Frenos neumáticos aprobados",
                        bus.getLicensePlate()
                );
            }

            @Override
            public String visit(Uber uber) {
                return String.format(
                        "  Servicio            : Inspección CDA%n"
                        + "  Vehículo            : Uber (%s)%n"
                        + "  Dictamen            : Emisiones urbanas aprobadas",
                        uber.getLicensePlate()
                );
            }

            @Override
            public String visit(Motorcycle motorcycle) {
                return String.format(
                        "  Servicio            : Inspección CDA%n"
                        + "  Vehículo            : Motocicleta (%s)%n"
                        + "  Dictamen            : Luces y transmisión aprobadas",
                        motorcycle.getLicensePlate()
                );
            }
        };

        client.registerVehicle(new Bus("BUS001", 50.0, 25.0, 0));
        client.registerVehicle(new Uber("UBR001", 10.0, 5.0, 0));
        client.registerVehicle(new Motorcycle("MOT001", 3.0, 1.0, 0));

        List<String> reports = client.executeVisitorOnAll(technicalInspectionVisitor);

        assertEquals(3, reports.size());
        assertTrue(reports.get(0).contains("Frenos neumáticos aprobados"));
        assertTrue(reports.get(1).contains("Emisiones urbanas aprobadas"));
        assertTrue(reports.get(2).contains("Luces y transmisión aprobadas"));
    }

    // =========================================================================
    // 2. CASOS LÍMITE (BOUNDARY / EDGE CASES)
    // =========================================================================

    @Test
    public void testBoundaryCompletelyEmptyTankAndAllWheelsWornAcrossFleet() {
        IVehicle bus = new Bus("BUS000", 80.0, 0.0, 6);
        IVehicle uber = new Uber("UBR000", 14.0, 0.0, 4);
        IVehicle moto = new Motorcycle("MOT000", 3.5, 0.0, 2);

        client.registerVehicle(bus);
        client.registerVehicle(uber);
        client.registerVehicle(moto);

        client.executeVisitorOnAll(gasStation);
        client.executeVisitorOnAll(workshop);

        assertEquals(80.0, bus.getCurrentFuel(), 0.0001);
        assertEquals(0, bus.getWornWheels());

        assertEquals(14.0, uber.getCurrentFuel(), 0.0001);
        assertEquals(0, uber.getWornWheels());

        assertEquals(3.5, moto.getCurrentFuel(), 0.0001);
        assertEquals(0, moto.getWornWheels());
    }

    @Test
    public void testBoundaryMinimalFuelFractionMissing() {
        IVehicle moto = new Motorcycle("MIN001", 3.0, 2.95, 0);
        client.registerVehicle(moto);

        String report = client.executeVisitor("MIN001", gasStation);

        assertEquals(3.0, moto.getCurrentFuel(), 0.0001);
        assertTrue(report.contains("0.05 gal"));
    }

    // =========================================================================
    // 3. VALIDACIÓN DE INVARIANTES Y MANEJO DE ERRORES
    // =========================================================================

    @Test
    public void testRefuelAlreadyFullTankThrowsException() {
        client.registerVehicle(new Bus("FULL01", 50.0, 50.0, 1));

        assertThrows(IllegalArgumentException.class, () -> client.executeVisitor("FULL01", gasStation));
    }

    @Test
    public void testWorkshopOnVehicleWithoutWornWheelsThrowsException() {
        client.registerVehicle(new Uber("NEW001", 12.0, 6.0, 0));

        assertThrows(IllegalArgumentException.class, () -> client.executeVisitor("NEW001", workshop));
    }

    @Test
    public void testDuplicateAndNonExistentLicensePlatesThrowException() {
        client.registerVehicle(new Motorcycle("DUP001", 4.0, 1.0, 1));

        assertThrows(IllegalArgumentException.class,
                () -> client.registerVehicle(new Uber("DUP001", 12.0, 5.0, 1)));
        assertThrows(IllegalArgumentException.class,
                () -> client.executeVisitor("NOEX99", gasStation));
    }

    @Test
    public void testInvalidVehicleInvariantsThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Bus("   ", 50.0, 10.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Uber(null, 12.0, 5.0, 1));

        assertThrows(IllegalArgumentException.class, () -> new Motorcycle("MOT111", 0.0, 0.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Bus("BUS111", -20.0, 0.0, 1));

        assertThrows(IllegalArgumentException.class, () -> new Uber("UBR111", 12.0, -1.0, 1));
        assertThrows(IllegalArgumentException.class, () -> new Uber("UBR111", 12.0, 15.0, 1));

        assertThrows(IllegalArgumentException.class, () -> new Bus("BUS111", 50.0, 10.0, 7));
        assertThrows(IllegalArgumentException.class, () -> new Uber("UBR111", 12.0, 5.0, 5));
        assertThrows(IllegalArgumentException.class, () -> new Motorcycle("MOT111", 4.0, 1.0, 3));
        assertThrows(IllegalArgumentException.class, () -> new Motorcycle("MOT111", 4.0, 1.0, -1));
    }

    @Test
    public void testNullDependenciesAndEmptyFleetThrowException() {
        assertThrows(IllegalArgumentException.class, () -> new Client(null, new ConsoleIn(), new ConsoleOut()));
        assertThrows(IllegalArgumentException.class, () -> new Client(fleet, null, new ConsoleOut()));
        assertThrows(IllegalArgumentException.class, () -> new Client(fleet, new ConsoleIn(), null));
        assertThrows(IllegalArgumentException.class, () -> client.registerVehicle(null));
        assertThrows(IllegalArgumentException.class, () -> client.executeVisitorOnAll(gasStation));

        client.registerVehicle(new Motorcycle("MOT123", 4.0, 2.0, 1));
        assertThrows(IllegalArgumentException.class, () -> client.executeVisitor("MOT123", null));
    }
}
