// Archivo: TravelContext.java | Paquete: co.edu.udistrital.interpreter.model
package co.edu.udistrital.interpreter.model;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Contexto del patrón Interpreter que encapsula las condiciones reales de un viaje
 * al momento de presentarse ante un validador de estación.
 */
public class TravelContext {

    private final String cardCode;
    private final String stationName;
    private final String fareZone;
    private final String timeSlot;
    private final String dayType;
    private final Set<String> activeTokens;

    /**
     * Construye el contexto de viaje validando sus invariantes y normalizando los tokens.
     *
     * @param cardCode    identificador de la tarjeta del pasajero
     * @param stationName nombre de la estación donde se valida el acceso
     * @param fareZone    token de zona tarifaria (ej. ZONA_NORTE, ZONA_SUR, ZONA_CENTRO)
     * @param timeSlot    token de franja horaria (ej. HORA_VALLE, HORA_PICO, HORA_NOCTURNA)
     * @param dayType     token de tipo de día (ej. DIA_HABIL, DIA_FESTIVO, FIN_DE_SEMANA)
     */
    public TravelContext(String cardCode, String stationName, String fareZone, String timeSlot, String dayType) {
        if (cardCode == null || cardCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El código de la tarjeta no puede ser nulo ni vacío.");
        }
        if (stationName == null || stationName.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de la estación no puede ser nulo ni vacío.");
        }
        this.cardCode = cardCode.trim();
        this.stationName = stationName.trim();
        this.fareZone = normalizeToken(fareZone, "zona tarifaria");
        this.timeSlot = normalizeToken(timeSlot, "franja horaria");
        this.dayType = normalizeToken(dayType, "tipo de día");

        Set<String> tokens = new LinkedHashSet<>();
        tokens.add(this.fareZone);
        tokens.add(this.timeSlot);
        tokens.add(this.dayType);
        this.activeTokens = Collections.unmodifiableSet(tokens);
    }

    /**
     * Verifica si un token terminal de la gramática se cumple dentro del contexto del viaje.
     *
     * @param token identificador terminal a evaluar
     * @return true si el token está presente en el contexto activo; false en caso contrario
     */
    public boolean hasToken(String token) {
        String normalized = normalizeToken(token, "token de consulta");
        return activeTokens.contains(normalized);
    }

    /**
     * Normaliza y valida un identificador terminal de la gramática.
     *
     * @param rawToken  valor crudo recibido
     * @param fieldName nombre descriptivo del campo para mensajes de error
     * @return token normalizado en mayúsculas
     */
    private String normalizeToken(String rawToken, String fieldName) {
        if (rawToken == null || rawToken.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + fieldName + "' no puede ser nulo ni vacío.");
        }
        return rawToken.trim().toUpperCase(Locale.ROOT).replace(' ', '_');
    }

    public String getCardCode() {
        return cardCode;
    }

    public String getStationName() {
        return stationName;
    }

    public String getFareZone() {
        return fareZone;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public String getDayType() {
        return dayType;
    }

    public Set<String> getActiveTokens() {
        return activeTokens;
    }

    /**
     * Formatea el estado completo del contexto del viaje para su visualización.
     *
     * @return resumen multilínea del contexto
     */
    public String formatContext() {
        return String.format(
                "  Tarjeta             : %s%n"
                + "  Estación            : %s%n"
                + "  Zona Tarifaria      : %s%n"
                + "  Franja Horaria      : %s%n"
                + "  Tipo de Día         : %s%n"
                + "  Tokens Activos      : %s",
                cardCode, stationName, fareZone, timeSlot, dayType, activeTokens
        );
    }
}
