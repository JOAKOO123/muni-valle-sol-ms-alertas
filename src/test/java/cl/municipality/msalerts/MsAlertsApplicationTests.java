package cl.municipality.msalerts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Prueba de integracion para verificar que el contexto de Spring Boot
 * carga correctamente todos los beans del microservicio de alertas.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Single Responsibility: solo verifica el arranque del contexto</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
@SpringBootTest
class MsAlertsApplicationTests {

    /**
     * Verifica que el contexto de Spring se inicializa sin errores.
     * Un fallo indica un problema de configuracion o dependencias.
     */
    @Test
    void contextLoads() {
    }
}