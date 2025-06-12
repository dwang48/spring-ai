package health.myvita.spring_ai_demo.spring_ai.controller;

import health.myvita.spring_ai_demo.spring_ai.dto.NutritionAnalysisResponse;
import health.myvita.spring_ai_demo.spring_ai.service.FoodAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST Controller for food image analysis using GPT-4 Vision.
 * This controller handles image uploads and returns nutritional analysis.
 * 
 * Similar to FastAPI endpoints - provides clear API structure.
 */
@RestController
@RequestMapping("/api/v1/food")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class FoodAnalysisController {
    
    private final FoodAnalysisService foodAnalysisService;
    
    @Autowired
    public FoodAnalysisController(FoodAnalysisService foodAnalysisService) {
        this.foodAnalysisService = foodAnalysisService;
    }
    
    /**
     * Analyzes uploaded food images and returns nutritional information.
     * 
     * POST /api/v1/food/analyze
     * Content-Type: multipart/form-data
     * 
     * @param image The uploaded food image file
     * @return JSON response with food items, calories, and nutritional breakdown
     */
    @PostMapping(value = "/analyze", consumes = "multipart/form-data")
    public ResponseEntity<NutritionAnalysisResponse> analyzeFoodImage(
            @RequestParam("image") MultipartFile image) {
        
        try {
            // Validate file upload
            if (image.isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Check if file is an image
            String contentType = image.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return ResponseEntity.badRequest().build();
            }
            
            // File size limit (5MB)
            if (image.getSize() > 5 * 1024 * 1024) {
                return ResponseEntity.badRequest().build();
            }
            
            // Call the service to analyze the food image
            NutritionAnalysisResponse response = foodAnalysisService.analyzeFood(image);
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            // Log the error and return server error
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint for the food analysis service
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Food Analysis Service is running");
    }
} 