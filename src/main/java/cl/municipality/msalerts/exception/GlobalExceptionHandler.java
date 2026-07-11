package cl.municipality.msalerts.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import cl.municipality.msalerts.glitchtip.GlitchTipErrorReporter;
import cl.municipality.msalerts.glitchtip.GlitchTipLogger;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Manejador global de excepciones del microservicio de alertas.
 * Intercepta excepciones y retorna respuestas HTTP con formato estandarizado.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Chain of Responsibility: intercepta excepciones en cascada</li>
 *   <li>Single Responsibility: centraliza el manejo de errores</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final GlitchTipErrorReporter errorReporter;
    private final GlitchTipLogger glitchTipLogger;

    public GlobalExceptionHandler(GlitchTipErrorReporter errorReporter, GlitchTipLogger glitchTipLogger) {
        this.errorReporter = errorReporter;
        this.glitchTipLogger = glitchTipLogger;
    }

    /**
     * Maneja el caso en que no se encuentra una alerta por su id.
     *
     * @param ex excepcion con el id que no fue encontrado
     * @return ResponseEntity con status 404 y detalle del error
     */
    @ExceptionHandler(AlertNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(AlertNotFoundException ex) {
        glitchTipLogger.warn(logger, "Alerta no encontrada en ms-alertas: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 404
        ));
    }

    /**
     * Maneja errores de validacion de @Valid en el body del request.
     *
     * @param ex excepcion con los campos que fallaron la validacion
     * @return ResponseEntity con status 400 y detalle del primer error
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .findFirst()
                .orElse("Datos de entrada invalidos");
        glitchTipLogger.warn(logger, "Validacion fallida en ms-alertas: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", message,
                "timestamp", LocalDateTime.now().toString(),
                "status", 400
        ));
    }

    /**
     * Maneja valores invalidos en parametros de negocio (ej: status desconocido).
     *
     * @param ex excepcion con el detalle del valor invalido
     * @return ResponseEntity con status 400 y detalle del error
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        glitchTipLogger.warn(logger, "Valor invalido recibido en ms-alertas: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 400
        ));
    }

    /**
     * Maneja excepciones genericas de runtime no controladas.
     *
     * @param ex excepcion capturada
     * @return ResponseEntity con status 500 y detalle del error
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntime(RuntimeException ex) {
        errorReporter.captureException(ex, "Excepcion no controlada en ms-alertas");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
                "error", ex.getMessage(),
                "timestamp", LocalDateTime.now().toString(),
                "status", 500
        ));
    }
}