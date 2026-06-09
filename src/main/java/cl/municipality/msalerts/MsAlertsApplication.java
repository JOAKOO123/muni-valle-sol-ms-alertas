package cl.municipality.msalerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal del microservicio de alertas municipales.
 * Punto de entrada de la aplicacion Spring Boot.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Microservice Pattern: servicio autonomo de gestion de alertas</li>
 *   <li>Single Responsibility: solo inicializa el contexto de Spring</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class MsAlertsApplication {

    /**
     * Metodo principal que inicia la aplicacion Spring Boot.
     *
     * @param args Argumentos de linea de comandos.
     */
    public static void main(String[] args) {
        SpringApplication.run(MsAlertsApplication.class, args);
    }
}