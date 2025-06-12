package health.myvita.spring_ai_demo.spring_ai.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for Spring AI and application beans.
 * 
 * This is similar to Django settings or FastAPI dependencies setup.
 * It configures the beans that will be injected into our services.
 */
@Configuration
public class AiConfiguration {
    
    /**
     * Creates an ObjectMapper bean for JSON processing.
     * This is used for parsing and serializing JSON data in our services.
     * 
     * @return ObjectMapper instance
     */
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
} 