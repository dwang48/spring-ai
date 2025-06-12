package health.myvita.spring_ai_demo.spring_ai.controller;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthCoachResponse;
import health.myvita.spring_ai_demo.spring_ai.service.HealthCoachService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller for health coaching conversation endpoints.
 * 
 * This handles the "Tell us how you feel" feature where users can
 * describe their symptoms and get health advice.
 */
@RestController
@RequestMapping("/api/v1/coach")
@CrossOrigin(origins = "*")
public class HealthCoachController {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthCoachController.class);
    
    private final HealthCoachService healthCoachService;
    
    public HealthCoachController(HealthCoachService healthCoachService) {
        this.healthCoachService = healthCoachService;
    }
    
    /**
     * POST /api/v1/coach/advice
     * 
     * Provides health coaching advice based on user's description of how they feel.
     * 
     * Example curl request:
     * curl -X POST http://localhost:8080/api/v1/coach/advice \
     *   -H "Content-Type: application/json" \
     *   -d '{"message": "Today I felt bloated and had a mild headache after lunch"}'
     * 
     * @param request Request containing the user's message
     * @return HealthCoachResponse with structured advice
     */
    @PostMapping("/advice")
    public ResponseEntity<HealthCoachResponse> provideAdvice(@RequestBody AdviceRequest request) {
        try {
            logger.info("Received health coaching request");
            logger.debug("User message: {}", request.getMessage());
            
            // Validate the request
            if (request.getMessage() == null || request.getMessage().trim().isEmpty()) {
                logger.warn("Empty message received");
                return ResponseEntity.badRequest().build();
            }
            
            // Limit message length to prevent abuse
            if (request.getMessage().length() > 1000) {
                logger.warn("Message too long: {} characters", request.getMessage().length());
                return ResponseEntity.badRequest().build();
            }
            
            // Get advice from the health coach service
            HealthCoachResponse response = healthCoachService.provideHealthAdvice(request.getMessage());
            
            logger.info("Successfully provided health advice");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error providing health advice", e);
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * GET /api/v1/coach/health
     * 
     * Health check endpoint for the health coach service.
     * 
     * @return Simple status message
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Coach Service is running");
    }
    
    /**
     * Request DTO for the advice endpoint.
     */
    public static class AdviceRequest {
        private String message;
        
        public AdviceRequest() {}
        
        public AdviceRequest(String message) {
            this.message = message;
        }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
} 