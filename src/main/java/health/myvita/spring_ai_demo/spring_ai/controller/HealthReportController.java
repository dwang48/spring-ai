package health.myvita.spring_ai_demo.spring_ai.controller;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthReportRequest;
import health.myvita.spring_ai_demo.spring_ai.service.HealthReportService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * REST Controller for health report generation using GPT-4.
 * This controller handles both individual and batch report generation.
 */
@RestController
@RequestMapping("/api/v1/reports")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class HealthReportController {
    
    private final HealthReportService healthReportService;
    
    @Autowired
    public HealthReportController(HealthReportService healthReportService) {
        this.healthReportService = healthReportService;
    }
    
    /**
     * Generates a single health report for a user.
     * 
     * POST /api/v1/reports/generate
     * Content-Type: application/json
     * 
     * @param request HealthReportRequest containing user data and metrics
     * @return String containing the generated health report
     */
    @PostMapping("/generate")
    public ResponseEntity<String> generateHealthReport(@RequestBody HealthReportRequest request) {
        try {
            // Validate the request
            if (request.getUserId() == null || request.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            
            if (request.getReportType() == null || request.getReportType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Report type is required");
            }
            
            // Generate the health report
            String report = healthReportService.generateHealthReport(request);
            
            return ResponseEntity.ok(report);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Failed to generate health report: " + e.getMessage());
        }
    }
    
    /**
     * Generates health reports for multiple users asynchronously.
     * 
     * POST /api/v1/reports/batch
     * Content-Type: application/json
     * 
     * @param requests List of HealthReportRequest objects
     * @return CompletableFuture containing list of generated reports
     */
    @PostMapping("/batch")
    public CompletableFuture<ResponseEntity<List<String>>> generateBatchHealthReports(
            @RequestBody List<HealthReportRequest> requests) {
        
        try {
            // Validate the request
            if (requests == null || requests.isEmpty()) {
                return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body(List.of("Request list cannot be empty"))
                );
            }
            
            // Limit batch size to prevent resource exhaustion
            if (requests.size() > 50) {
                return CompletableFuture.completedFuture(
                    ResponseEntity.badRequest().body(List.of("Batch size cannot exceed 50 requests"))
                );
            }
            
            // Generate reports asynchronously
            return healthReportService.generateBatchReports(requests)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(throwable -> 
                        ResponseEntity.internalServerError().body(
                            List.of("Failed to generate batch reports: " + throwable.getMessage())
                        )
                    );
            
        } catch (Exception e) {
            return CompletableFuture.completedFuture(
                ResponseEntity.internalServerError().body(
                    List.of("Failed to process batch request: " + e.getMessage())
                )
            );
        }
    }
    
    /**
     * Health check endpoint for the report generation service
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Health Report Service is running");
    }
} 