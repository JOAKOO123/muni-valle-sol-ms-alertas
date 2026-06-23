package cl.municipality.msalerts.service;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;

import java.util.List;

/**
 * Puerto (interfaz) que define el contrato del servicio de alertas municipales.
 * Permite desacoplar el controlador de la implementacion concreta,
 * facilitando pruebas unitarias y cumpliendo con el principio de inversion de dependencias.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Port and Adapter (Hexagonal): define el contrato sin exponer detalles internos</li>
 *   <li>Dependency Inversion Principle: el controlador depende de la abstraccion, no de la implementacion</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.0
 * @since 1.0
 */
public interface AlertServicePort {

    /**
     * Crea y persiste una nueva alerta en estado ACTIVE.
     *
     * @param request DTO con los datos de la alerta. No debe ser null.
     * @return DTO con la alerta creada, incluyendo el id generado por MongoDB.
     */
    AlertResponseDTO create(AlertRequestDTO request);

    /**
     * Retorna todas las alertas con estado ACTIVE.
     *
     * @return Lista de DTOs de alertas activas. Vacia si no hay ninguna.
     */
    List<AlertResponseDTO> listActive();

    /**
     * Retorna el historial completo de alertas sin importar su estado.
     *
     * @return Lista de DTOs con todas las alertas registradas. Vacia si no hay ninguna.
     */
    List<AlertResponseDTO> listAll();

    /**
     * Busca y retorna una alerta por su identificador.
     *
     * @param id Identificador unico de la alerta.
     * @return DTO con los datos de la alerta encontrada.
     * @throws cl.municipality.msalerts.exception.AlertNotFoundException si no existe la alerta.
     */
    AlertResponseDTO findById(String id);

    /**
     * Cambia el estado de una alerta existente.
     *
     * @param id     Identificador de la alerta a actualizar.
     * @param status Nuevo estado: "ACTIVE" o "RESOLVED".
     * @return DTO con la alerta actualizada.
     * @throws cl.municipality.msalerts.exception.AlertNotFoundException si no existe la alerta.
     * @throws IllegalArgumentException si el valor de status no es valido.
     */
    AlertResponseDTO changeStatus(String id, String status);

    /**
     * Elimina permanentemente una alerta de la base de datos.
     *
     * @param id Identificador de la alerta a eliminar.
     * @throws cl.municipality.msalerts.exception.AlertNotFoundException si no existe la alerta.
     */
    void delete(String id);
}