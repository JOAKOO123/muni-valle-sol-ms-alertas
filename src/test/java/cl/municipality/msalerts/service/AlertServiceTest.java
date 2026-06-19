package cl.municipality.msalerts.service;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.factory.AlertFactory;
import cl.municipality.msalerts.mapper.AlertMapper;
import cl.municipality.msalerts.model.Alert;
import cl.municipality.msalerts.repository.AlertRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link AlertService}.
 * Verifica el comportamiento de la logica de negocio de alertas
 * con todas las dependencias mockeadas.
 *
 * <p>Patrones aplicados:</p>
 * <ul>
 *   <li>Arrange-Act-Assert: estructura clara en cada caso de prueba</li>
 *   <li>Dependency Inversion: se inyectan mocks a traves de la implementacion concreta</li>
 * </ul>
 *
 * @author Beltran
 * @version 1.1
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AlertService - pruebas unitarias")
class AlertServiceTest {

    /** Mock del repositorio de alertas. */
    @Mock
    private AlertRepository alertRepository;

    /** Mock de la fabrica de alertas. */
    @Mock
    private AlertFactory alertFactory;

    /** Mock del mapper de alertas. */
    @Mock
    private AlertMapper alertMapper;

    /** Servicio bajo prueba con dependencias mockeadas. */
    @InjectMocks
    private AlertService alertService;

    /** Entidad de alerta reutilizada en las pruebas. */
    private Alert mockAlert;

    /** DTO de respuesta reutilizado en las pruebas. */
    private AlertResponseDTO mockDTO;

    /** DTO de entrada reutilizado en las pruebas. */
    private AlertRequestDTO mockRequest;

    /**
     * Inicializa los datos de prueba antes de cada test.
     */
    @BeforeEach
    void setUp() {
        mockAlert = Alert.builder()
                .id("abc123")
                .title("Incendio sector norte")
                .description("Fuego activo en calle 5")
                .severity(Alert.Severity.HIGH)
                .status(Alert.Status.ACTIVE)
                .date(LocalDateTime.now())
                .reportId(1L)
                .userId(2L)
                .latitude(null)
                .longitude(null)
                .build();

        mockRequest = new AlertRequestDTO(
                "Incendio sector norte",
                "Fuego activo en calle 5",
                "HIGH",
                1L,
                2L,
                null,
                null
        );

        mockDTO = new AlertResponseDTO(
                "abc123", "Incendio sector norte", "Fuego activo en calle 5",
                "HIGH", "ACTIVE", LocalDateTime.now(), 1L, 2L, null, null
        );
    }

    /**
     * Verifica que create() persiste la alerta y retorna su DTO.
     */
    @Test
    @DisplayName("create() debería persistir la alerta y retornar su DTO")
    void create_persisteYRetornaDTO() {
        when(alertFactory.create(mockRequest)).thenReturn(mockAlert);
        when(alertRepository.save(mockAlert)).thenReturn(mockAlert);
        when(alertMapper.toDTO(mockAlert)).thenReturn(mockDTO);

        AlertResponseDTO resultado = alertService.create(mockRequest);

        assertThat(resultado.id()).isEqualTo("abc123");
        assertThat(resultado.severity()).isEqualTo("HIGH");
        assertThat(resultado.status()).isEqualTo("ACTIVE");
        verify(alertRepository).save(mockAlert);
        verify(alertMapper).toDTO(mockAlert);
    }

    /**
     * Verifica que listActive() retorna solo alertas con estado ACTIVE.
     */
    @Test
    @DisplayName("listActive() debería retornar solo alertas con estado ACTIVE")
    void listActive_soloRetornaActivas() {
        when(alertRepository.findByStatus(Alert.Status.ACTIVE)).thenReturn(List.of(mockAlert));
        when(alertMapper.toDTO(mockAlert)).thenReturn(mockDTO);

        List<AlertResponseDTO> resultado = alertService.listActive();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).status()).isEqualTo("ACTIVE");
        verify(alertRepository).findByStatus(Alert.Status.ACTIVE);
    }

    /**
     * Verifica que listActive() retorna lista vacia si no hay alertas activas.
     */
    @Test
    @DisplayName("listActive() debería retornar lista vacía si no hay alertas activas")
    void listActive_retornaVacioSinActivas() {
        when(alertRepository.findByStatus(Alert.Status.ACTIVE)).thenReturn(List.of());

        assertThat(alertService.listActive()).isEmpty();
    }

    /**
     * Verifica que listAll() retorna todas las alertas sin filtrar por estado.
     */
    @Test
    @DisplayName("listAll() debería retornar todas las alertas sin filtrar por estado")
    void listAll_retornaTodasLasAlertas() {
        Alert resuelta = Alert.builder()
                .id("xyz789").title("Humo").description("Desc")
                .severity(Alert.Severity.MEDIUM).status(Alert.Status.RESOLVED)
                .date(LocalDateTime.now())
                .latitude(null)
                .longitude(null)
                .build();

        AlertResponseDTO dtoResuelta = new AlertResponseDTO(
                "xyz789", "Humo", "Desc", "MEDIUM", "RESOLVED",
                LocalDateTime.now(), null, null, null, null
        );

        when(alertRepository.findAll()).thenReturn(List.of(mockAlert, resuelta));
        when(alertMapper.toDTO(mockAlert)).thenReturn(mockDTO);
        when(alertMapper.toDTO(resuelta)).thenReturn(dtoResuelta);

        List<AlertResponseDTO> resultado = alertService.listAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(AlertResponseDTO::status)
                .containsExactly("ACTIVE", "RESOLVED");
    }

    /**
     * Verifica que findById() retorna el DTO cuando la alerta existe.
     */
    @Test
    @DisplayName("findById() debería retornar el DTO cuando la alerta existe")
    void findById_retornaDTOSiExiste() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));
        when(alertMapper.toDTO(mockAlert)).thenReturn(mockDTO);

        AlertResponseDTO resultado = alertService.findById("abc123");

        assertThat(resultado.id()).isEqualTo("abc123");
        assertThat(resultado.title()).isEqualTo("Incendio sector norte");
    }

    /**
     * Verifica que findById() lanza AlertNotFoundException si la alerta no existe.
     */
    @Test
    @DisplayName("findById() debería lanzar AlertNotFoundException si no existe")
    void findById_lanzaExcepcionSiNoExiste() {
        when(alertRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.findById("noexiste"))
                .isInstanceOf(AlertNotFoundException.class)
                .hasMessageContaining("noexiste");
    }

    /**
     * Verifica que changeStatus() actualiza el estado a RESOLVED correctamente.
     */
    @Test
    @DisplayName("changeStatus() debería actualizar el estado a RESOLVED correctamente")
    void changeStatus_actualizaEstado() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));
        when(alertRepository.save(mockAlert)).thenReturn(mockAlert);

        AlertResponseDTO resuelto = new AlertResponseDTO(
                "abc123", "Incendio sector norte", "Fuego activo en calle 5",
                "HIGH", "RESOLVED", LocalDateTime.now(), 1L, 2L, null, null
        );
        when(alertMapper.toDTO(mockAlert)).thenReturn(resuelto);

        AlertResponseDTO resultado = alertService.changeStatus("abc123", "RESOLVED");

        assertThat(resultado.status()).isEqualTo("RESOLVED");
        verify(alertRepository).save(mockAlert);
    }

    /**
     * Verifica que changeStatus() lanza IllegalArgumentException si el status es invalido.
     */
    @Test
    @DisplayName("changeStatus() debería lanzar IllegalArgumentException si el status es inválido")
    void changeStatus_lanzaExcepcionConStatusInvalido() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));

        assertThatThrownBy(() -> alertService.changeStatus("abc123", "CERRADO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CERRADO");
    }

    /**
     * Verifica que changeStatus() lanza AlertNotFoundException si la alerta no existe.
     */
    @Test
    @DisplayName("changeStatus() debería lanzar AlertNotFoundException si la alerta no existe")
    void changeStatus_lanzaExcepcionSiAlertaNoExiste() {
        when(alertRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.changeStatus("noexiste", "RESOLVED"))
                .isInstanceOf(AlertNotFoundException.class);
    }

    /**
     * Verifica que delete() elimina la alerta si existe.
     */
    @Test
    @DisplayName("delete() debería eliminar la alerta si existe")
    void delete_eliminaSiExiste() {
        when(alertRepository.existsById("abc123")).thenReturn(true);

        alertService.delete("abc123");

        verify(alertRepository).deleteById("abc123");
    }

    /**
     * Verifica que delete() lanza AlertNotFoundException si la alerta no existe.
     */
    @Test
    @DisplayName("delete() debería lanzar AlertNotFoundException si no existe")
    void delete_lanzaExcepcionSiNoExiste() {
        when(alertRepository.existsById("noexiste")).thenReturn(false);

        assertThatThrownBy(() -> alertService.delete("noexiste"))
                .isInstanceOf(AlertNotFoundException.class)
                .hasMessageContaining("noexiste");

        verify(alertRepository, never()).deleteById(any());
    }
}