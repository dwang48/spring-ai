package health.myvita.spring_ai_demo.spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response DTO for the food-in-image analysis endpoint.
 * Contains the complete nutrition analysis results.
 */
public class NutritionAnalysisResponse {
    @JsonProperty("food_items")
    private List<FoodItemDto> foodItems;
    
    @JsonProperty("total_estimated_calories_kcal")
    private int totalEstimatedCaloriesKcal;
    
    private List<String> sources;
    
    // Default constructor
    public NutritionAnalysisResponse() {}
    
    // Constructor
    public NutritionAnalysisResponse(List<FoodItemDto> foodItems, 
                                   int totalEstimatedCaloriesKcal, 
                                   List<String> sources) {
        this.foodItems = foodItems;
        this.totalEstimatedCaloriesKcal = totalEstimatedCaloriesKcal;
        this.sources = sources;
    }
    
    // Getters and setters
    public List<FoodItemDto> getFoodItems() { return foodItems; }
    public void setFoodItems(List<FoodItemDto> foodItems) { this.foodItems = foodItems; }
    
    public int getTotalEstimatedCaloriesKcal() { return totalEstimatedCaloriesKcal; }
    public void setTotalEstimatedCaloriesKcal(int totalEstimatedCaloriesKcal) { 
        this.totalEstimatedCaloriesKcal = totalEstimatedCaloriesKcal; 
    }
    
    public List<String> getSources() { return sources; }
    public void setSources(List<String> sources) { this.sources = sources; }
}

