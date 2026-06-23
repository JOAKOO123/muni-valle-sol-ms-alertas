package cl.municipality.msalerts.repository;

import cl.municipality.msalerts.model.Alert;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Repositorio de acceso a datos para la entidad Alert.
 * Extiende MongoRepository para heredar las operaciones CRUD estandar.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Repository Pattern: abstrae el acceso a la capa de persistencia</li>
 *   <li>Single Responsibility: solo gestiona la persistencia de alertas</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public interface AlertRepository extends MongoRepository<Alert, String> {

    /**
     * Retorna todas las alertas que coincidan con el estado indicado.
     *
     * @param status Estado por el que se desea filtrar.
     * @return Lista de alertas con el estado especificado. Vacia si no hay resultados.
     */
    List<Alert> findByStatus(Alert.Status status);

    /**
     * Retorna todas las alertas asociadas a un reporte especifico.
     *
     * @param reportId Identificador del reporte.
     * @return Lista de alertas vinculadas al reporte. Vacia si no hay resultados.
     */
    List<Alert> findByReportId(Long reportId);
}