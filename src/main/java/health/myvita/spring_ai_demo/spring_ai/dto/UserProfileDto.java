package health.myvita.spring_ai_demo.spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for user profile information.
 * Used to provide personalized nutritional analysis based on user's health profile.
 */
public class UserProfileDto {
    
    private Integer age;
    private String gender;
    private Double weight; // in kg
    private Double height; // in cm
    
    @JsonProperty("health_conditions")
    private String healthConditions;
    
    @JsonProperty("dietary_preference")
    private String dietaryPreference;
    
    private String allergies;
    
    @JsonProperty("health_goals")
    private String healthGoals;
    
    @JsonProperty("activity_level")
    private String activityLevel;
    
    // Default constructor
    public UserProfileDto() {}
    
    // Constructor
    public UserProfileDto(Integer age, String gender, Double weight, Double height,
                         String healthConditions, String dietaryPreference, String allergies,
                         String healthGoals, String activityLevel) {
        this.age = age;
        this.gender = gender;
        this.weight = weight;
        this.height = height;
        this.healthConditions = healthConditions;
        this.dietaryPreference = dietaryPreference;
        this.allergies = allergies;
        this.healthGoals = healthGoals;
        this.activityLevel = activityLevel;
    }
    
    // Getters and setters
    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
    
    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }
    
    public Double getHeight() { return height; }
    public void setHeight(Double height) { this.height = height; }
    
    public String getHealthConditions() { return healthConditions; }
    public void setHealthConditions(String healthConditions) { this.healthConditions = healthConditions; }
    
    public String getDietaryPreference() { return dietaryPreference; }
    public void setDietaryPreference(String dietaryPreference) { this.dietaryPreference = dietaryPreference; }
    
    public String getAllergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    
    public String getHealthGoals() { return healthGoals; }
    public void setHealthGoals(String healthGoals) { this.healthGoals = healthGoals; }
    
    public String getActivityLevel() { return activityLevel; }
    public void setActivityLevel(String activityLevel) { this.activityLevel = activityLevel; }
    
    /**
     * Validation method to check if required fields are present
     */
    public boolean isValid() {
        return age != null && age > 0 && 
               gender != null && !gender.trim().isEmpty();
    }
    
    /**
     * Returns a safe string representation avoiding null values
     */
    public String getHealthConditionsSafe() {
        return healthConditions != null ? healthConditions : "None";
    }
    
    public String getDietaryPreferenceSafe() {
        return dietaryPreference != null ? dietaryPreference : "No specific preference";
    }
    
    public String getAllergiesSafe() {
        return allergies != null ? allergies : "None";
    }
    
    public String getHealthGoalsSafe() {
        return healthGoals != null ? healthGoals : "maintain health";
    }
    
    public String getActivityLevelSafe() {
        return activityLevel != null ? activityLevel : "moderate";
    }
} 