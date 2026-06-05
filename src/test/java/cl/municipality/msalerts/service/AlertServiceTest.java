package cl.municipality.msalerts.service;

import cl.municipality.msalerts.dto.AlertRequestDTO;
import cl.municipality.msalerts.dto.AlertResponseDTO;
import cl.municipality.msalerts.exception.AlertNotFoundException;
import cl.municipality.msalerts.factory.AlertFactory;
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

@ExtendWith(MockitoExtension.class)
@DisplayName("AlertService - pruebas unitarias")
class AlertServiceTest {

    @Mock
    private AlertRepository alertRepository;

    @Mock
    private AlertFactory alertFactory;

    @InjectMocks
    private AlertService alertService;

    private Alert mockAlert;
    private AlertRequestDTO mockRequest;

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
                .build();

        mockRequest = new AlertRequestDTO(
                "Incendio sector norte",
                "Fuego activo en calle 5",
                "HIGH",
                1L,
                2L
        );
    }

    @Test
    @DisplayName("create() debería persistir la alerta y retornar su DTO")
    void create_persisteYRetornaDTO() {
        when(alertFactory.create(mockRequest)).thenReturn(mockAlert);
        when(alertRepository.save(mockAlert)).thenReturn(mockAlert);

        AlertResponseDTO resultado = alertService.create(mockRequest);

        assertThat(resultado.id()).isEqualTo("abc123");
        assertThat(resultado.title()).isEqualTo("Incendio sector norte");
        assertThat(resultado.severity()).isEqualTo("HIGH");
        assertThat(resultado.status()).isEqualTo("ACTIVE");
        verify(alertRepository).save(mockAlert);
    }

    @Test
    @DisplayName("listActive() debería retornar solo alertas con estado ACTIVE")
    void listActive_soloRetornaActivas() {
        when(alertRepository.findByStatus(Alert.Status.ACTIVE)).thenReturn(List.of(mockAlert));

        List<AlertResponseDTO> resultado = alertService.listActive();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).status()).isEqualTo("ACTIVE");
        verify(alertRepository).findByStatus(Alert.Status.ACTIVE);
    }

    @Test
    @DisplayName("listActive() debería retornar lista vacía si no hay alertas activas")
    void listActive_retornaVacioSinActivas() {
        when(alertRepository.findByStatus(Alert.Status.ACTIVE)).thenReturn(List.of());

        assertThat(alertService.listActive()).isEmpty();
    }

    @Test
    @DisplayName("listAll() debería retornar todas las alertas sin filtrar por estado")
    void listAll_retornaTodasLasAlertas() {
        Alert resuelta = Alert.builder()
                .id("xyz789").title("Humo").description("Desc")
                .severity(Alert.Severity.MEDIUM).status(Alert.Status.RESOLVED)
                .date(LocalDateTime.now()).build();

        when(alertRepository.findAll()).thenReturn(List.of(mockAlert, resuelta));

        List<AlertResponseDTO> resultado = alertService.listAll();

        assertThat(resultado).hasSize(2);
        assertThat(resultado).extracting(AlertResponseDTO::status)
                .containsExactly("ACTIVE", "RESOLVED");
    }

    @Test
    @DisplayName("findById() debería retornar el DTO cuando la alerta existe")
    void findById_retornaDTOSiExiste() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));

        AlertResponseDTO resultado = alertService.findById("abc123");

        assertThat(resultado.id()).isEqualTo("abc123");
        assertThat(resultado.title()).isEqualTo("Incendio sector norte");
    }

    @Test
    @DisplayName("findById() debería lanzar AlertNotFoundException si no existe")
    void findById_lanzaExcepcionSiNoExiste() {
        when(alertRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.findById("noexiste"))
                .isInstanceOf(AlertNotFoundException.class)
                .hasMessageContaining("noexiste");
    }

    @Test
    @DisplayName("changeStatus() debería actualizar el estado a RESOLVED correctamente")
    void changeStatus_actualizaEstado() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));
        when(alertRepository.save(mockAlert)).thenReturn(mockAlert);

        AlertResponseDTO resultado = alertService.changeStatus("abc123", "RESOLVED");

        assertThat(resultado.status()).isEqualTo("RESOLVED");
        verify(alertRepository).save(mockAlert);
    }

    @Test
    @DisplayName("changeStatus() debería lanzar IllegalArgumentException si el status es inválido")
    void changeStatus_lanzaExcepcionConStatusInvalido() {
        when(alertRepository.findById("abc123")).thenReturn(Optional.of(mockAlert));

        assertThatThrownBy(() -> alertService.changeStatus("abc123", "CERRADO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("CERRADO");
    }

    @Test
    @DisplayName("changeStatus() debería lanzar AlertNotFoundException si la alerta no existe")
    void changeStatus_lanzaExcepcionSiAlertaNoExiste() {
        when(alertRepository.findById("noexiste")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.changeStatus("noexiste", "RESOLVED"))
                .isInstanceOf(AlertNotFoundException.class);
    }

    @Test
    @DisplayName("delete() debería eliminar la alerta si existe")
    void delete_eliminaSiExiste() {
        when(alertRepository.existsById("abc123")).thenReturn(true);

        alertService.delete("abc123");

        verify(alertRepository).deleteById("abc123");
    }

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