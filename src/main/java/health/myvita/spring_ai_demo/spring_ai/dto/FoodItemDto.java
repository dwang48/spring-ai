package health.myvita.spring_ai_demo.spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object representing a single food item detected in an image.
 * This matches the expected schema from the algo.md specification.
 * 
 * Think of this as similar to a Pydantic model in FastAPI or a Django serializer.
 */
public class FoodItemDto {
    private String name;
    
    @JsonProperty("serving_size_g")
    private double servingSizeG;
    
    @JsonProperty("calories_kcal") 
    private int caloriesKcal;
    
    private Macros macros;
    private Micros micros;
    private double confidence;
    
    // Default constructor for Jackson
    public FoodItemDto() {}
    
    // Constructor
    public FoodItemDto(String name, double servingSizeG, int caloriesKcal, 
                      Macros macros, Micros micros, double confidence) {
        this.name = name;
        this.servingSizeG = servingSizeG;
        this.caloriesKcal = caloriesKcal;
        this.macros = macros;
        this.micros = micros;
        this.confidence = confidence;
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public double getServingSizeG() { return servingSizeG; }
    public void setServingSizeG(double servingSizeG) { this.servingSizeG = servingSizeG; }
    
    public int getCaloriesKcal() { return caloriesKcal; }
    public void setCaloriesKcal(int caloriesKcal) { this.caloriesKcal = caloriesKcal; }
    
    public Macros getMacros() { return macros; }
    public void setMacros(Macros macros) { this.macros = macros; }
    
    public Micros getMicros() { return micros; }
    public void setMicros(Micros micros) { this.micros = micros; }
    
    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }
    
    /**
     * Macronutrients information
     */
    public static class Macros {
        @JsonProperty("protein_g")
        private double proteinG;
        
        @JsonProperty("fat_g") 
        private double fatG;
        
        @JsonProperty("carb_g")
        private double carbG;
        
        public Macros() {}
        
        public Macros(double proteinG, double fatG, double carbG) {
            this.proteinG = proteinG;
            this.fatG = fatG;
            this.carbG = carbG;
        }
        
        public double getProteinG() { return proteinG; }
        public void setProteinG(double proteinG) { this.proteinG = proteinG; }
        
        public double getFatG() { return fatG; }
        public void setFatG(double fatG) { this.fatG = fatG; }
        
        public double getCarbG() { return carbG; }
        public void setCarbG(double carbG) { this.carbG = carbG; }
    }
    
    /**
     * Micronutrients information 
     */
    public static class Micros {
        @JsonProperty("sodium_mg")
        private double sodiumMg;
        
        @JsonProperty("fiber_g")
        private double fiberG;
        
        public Micros() {}
        
        public Micros(double sodiumMg, double fiberG) {
            this.sodiumMg = sodiumMg;
            this.fiberG = fiberG;
        }
        
        public double getSodiumMg() { return sodiumMg; }
        public void setSodiumMg(double sodiumMg) { this.sodiumMg = sodiumMg; }
        
        public double getFiberG() { return fiberG; }
        public void setFiberG(double fiberG) { this.fiberG = fiberG; }
    }
} 