package cl.municipality.msalerts.glitchtip;

import org.springframework.stereotype.Component;

import jakarta.annotation.PreDestroy;

/**
 * Asegura que los eventos pendientes se envien a GlitchTip antes de que
 * la aplicacion termine.
 */
@Component
public class SentryLifecycle {

	private final GlitchTipErrorReporter errorReporter;

	public SentryLifecycle(GlitchTipErrorReporter errorReporter) {
		this.errorReporter = errorReporter;
	}

	@PreDestroy
	public void onShutdown() {
		errorReporter.flush();
	}

}
