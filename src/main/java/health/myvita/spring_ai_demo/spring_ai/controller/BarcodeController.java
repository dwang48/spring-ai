package health.myvita.spring_ai_demo.spring_ai.controller;

import health.myvita.spring_ai_demo.spring_ai.dto.BarcodeAnalysisResponse;
import health.myvita.spring_ai_demo.spring_ai.dto.UserProfileDto;
import health.myvita.spring_ai_demo.spring_ai.service.BarcodeAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * REST Controller for barcode scanning and food product analysis.
 * This controller handles barcode scanning requests and returns personalized nutritional analysis.
 * 
 * Endpoints:
 * - POST /api/v1/barcode/scan - Analyze product by barcode with user profile
 * - GET /api/v1/barcode/health - Health check endpoint
 */
@RestController
@RequestMapping("/api/v1/barcode")
@CrossOrigin(origins = "*") // Allow CORS for frontend integration
public class BarcodeController {
    
    private final BarcodeAnalysisService barcodeAnalysisService;
    
    @Autowired
    public BarcodeController(BarcodeAnalysisService barcodeAnalysisService) {
        this.barcodeAnalysisService = barcodeAnalysisService;
    }
    
    /**
     * Analyzes a product by barcode and provides personalized nutritional recommendations.
     * 
     * POST /api/v1/barcode/scan
     * Content-Type: application/json
     * 
     * Request Body:
     * {
     *   "barcode": "1234567890123",
     *   "user_profile": {
     *     "age": 30,
     *     "gender": "female",
     *     "weight": 65.0,
     *     "height": 165.0,
     *     "health_conditions": "diabetes",
     *     "dietary_preference": "vegetarian",
     *     "allergies": "nuts",
     *     "health_goals": "weight loss",
     *     "activity_level": "moderate"
     *   }
     * }
     * 
     * @param request The barcode scan request containing barcode and user profile
     * @return JSON response with product information and personalized analysis
     */
    @PostMapping("/scan")
    public ResponseEntity<BarcodeAnalysisResponse> scanBarcode(@RequestBody BarcodeScanRequest request) {
        
        try {
            // Validate request
            if (request == null) {
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getBarcode() == null || request.getBarcode().trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            
            if (request.getUserProfile() == null || !request.getUserProfile().isValid()) {
                return ResponseEntity.badRequest().build();
            }
            
            // Validate barcode format (basic check - should be digits and reasonable length)
            String barcode = request.getBarcode().trim();
            if (!barcode.matches("\\d{8,14}")) {
                return ResponseEntity.badRequest().build();
            }
            
            // Call the service to analyze the barcode
            BarcodeAnalysisResponse response = barcodeAnalysisService.analyzeProductByBarcode(
                barcode, 
                request.getUserProfile()
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            // Client error - bad request
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // Server error
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Quick barcode lookup without user profile (basic product info only).
     * 
     * GET /api/v1/barcode/lookup/{barcode}
     * 
     * @param barcode The product barcode
     * @return Basic product information from OpenFoodFacts
     */
    @GetMapping("/lookup/{barcode}")
    public ResponseEntity<BarcodeAnalysisResponse> lookupBarcode(@PathVariable String barcode) {
        
        try {
            // Validate barcode format
            if (barcode == null || !barcode.matches("\\d{8,14}")) {
                return ResponseEntity.badRequest().build();
            }
            
            // Create minimal user profile for basic analysis
            UserProfileDto basicProfile = new UserProfileDto();
            basicProfile.setAge(30);
            basicProfile.setGender("unspecified");
            basicProfile.setHealthConditions("None");
            basicProfile.setDietaryPreference("No specific preference");
            basicProfile.setAllergies("None");
            basicProfile.setHealthGoals("General health");
            
            // Call the service to get basic product info
            BarcodeAnalysisResponse response = barcodeAnalysisService.analyzeProductByBarcode(
                barcode, 
                basicProfile
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    
    /**
     * Health check endpoint for the barcode service
     */
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Barcode Analysis Service is running");
    }
    
    /**
     * Request DTO for barcode scanning
     */
    public static class BarcodeScanRequest {
        private String barcode;
        
        @JsonProperty("user_profile")
        private UserProfileDto userProfile;
        
        // Default constructor
        public BarcodeScanRequest() {}
        
        // Constructor
        public BarcodeScanRequest(String barcode, UserProfileDto userProfile) {
            this.barcode = barcode;
            this.userProfile = userProfile;
        }
        
        // Getters and setters
        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        
        public UserProfileDto getUserProfile() { return userProfile; }
        public void setUserProfile(UserProfileDto userProfile) { this.userProfile = userProfile; }
    }
} 