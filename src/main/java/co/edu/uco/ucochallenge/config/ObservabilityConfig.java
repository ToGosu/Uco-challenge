package co.edu.uco.ucochallenge.config;

import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Configuración de observabilidad para OpenTelemetry y métricas.
 * 
 * Esta configuración es completamente no invasiva:
 * - Solo se activa si las dependencias están disponibles
 * - No afecta el funcionamiento si las dependencias no están presentes
 * - Habilita anotaciones @Timed para instrumentar métodos automáticamente
 * - Personaliza el registro de métricas con tags comunes
 */
@Configuration
@EnableAspectJAutoProxy
@ConditionalOnClass(name = "io.micrometer.core.aop.TimedAspect")
public class ObservabilityConfig {
    
    /**
     * Habilita la anotación @Timed para instrumentar métodos automáticamente.
     * Solo se activa si Micrometer está disponible.
     */
    @Bean
    @ConditionalOnClass(name = "io.micrometer.core.aop.TimedAspect")
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
    
    /**
     * Personaliza el registro de métricas con tags comunes.
     * Solo se activa si MeterRegistry está disponible.
     */
    @Bean
    @ConditionalOnClass(name = "io.micrometer.core.instrument.MeterRegistry")
    public MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
        return registry -> registry.config().commonTags(
            "application", "uco-challenge",
            "environment", System.getProperty("spring.profiles.active", "default")
        );
    }
}

