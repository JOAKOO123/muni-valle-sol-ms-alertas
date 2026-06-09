package cl.municipality.msalerts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@TestPropertySource(properties = {
    "spring.data.mongodb.uri=mongodb://localhost:27017/ms-alerts-test",
    "spring.data.mongodb.database=ms-alerts-test",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration," +
        "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration"
})
class MsAlertsApplicationTests {

    /**
     * Verifica que el contexto de Spring se inicializa sin errores.
     * Un fallo indica un problema de configuracion o dependencias.
     */
    @Test
    void contextLoads() {
    }
}