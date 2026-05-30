package cl.municipality.msalerts.exception;

public class AlertNotFoundException extends RuntimeException {
    public AlertNotFoundException(String id) {
        super("Alert not found with id: " + id);
    }
}
