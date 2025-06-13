package health.myvita.spring_ai_demo.spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Data Transfer Object for barcode scan analysis response.
 * Contains product information fetched from OpenFoodFacts and AI nutritional analysis.
 */
public class BarcodeAnalysisResponse {
    
    @JsonProperty("product_info")
    private ProductInfo productInfo;
    
    @JsonProperty("nutritional_analysis")
    private String nutritionalAnalysis;
    
    @JsonProperty("recommendation")
    private String recommendation;
    
    @JsonProperty("source")
    private String source;
    
    // Default constructor
    public BarcodeAnalysisResponse() {}
    
    // Constructor
    public BarcodeAnalysisResponse(ProductInfo productInfo, String nutritionalAnalysis, 
                                  String recommendation, String source) {
        this.productInfo = productInfo;
        this.nutritionalAnalysis = nutritionalAnalysis;
        this.recommendation = recommendation;
        this.source = source;
    }
    
    // Getters and setters
    public ProductInfo getProductInfo() { return productInfo; }
    public void setProductInfo(ProductInfo productInfo) { this.productInfo = productInfo; }
    
    public String getNutritionalAnalysis() { return nutritionalAnalysis; }
    public void setNutritionalAnalysis(String nutritionalAnalysis) { this.nutritionalAnalysis = nutritionalAnalysis; }
    
    public String getRecommendation() { return recommendation; }
    public void setRecommendation(String recommendation) { this.recommendation = recommendation; }
    
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    
    /**
     * Product information from OpenFoodFacts
     */
    public static class ProductInfo {
        private String name;
        private String brand;
        private String categories;
        
        @JsonProperty("ingredients_text")
        private String ingredientsText;
        
        private String barcode;
        
        @JsonProperty("nutrition_per_100g")
        private NutritionPer100g nutritionPer100g;
        
        @JsonProperty("quality_scores")
        private QualityScores qualityScores;
        
        @JsonProperty("additives")
        private List<String> additives;
        
        @JsonProperty("allergens")
        private List<String> allergens;
        
        @JsonProperty("labels")
        private List<String> labels;
        
        @JsonProperty("openfoodfacts_url")
        private String openFoodFactsUrl;
        
        // Default constructor
        public ProductInfo() {}
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getBrand() { return brand; }
        public void setBrand(String brand) { this.brand = brand; }
        
        public String getCategories() { return categories; }
        public void setCategories(String categories) { this.categories = categories; }
        
        public String getIngredientsText() { return ingredientsText; }
        public void setIngredientsText(String ingredientsText) { this.ingredientsText = ingredientsText; }
        
        public String getBarcode() { return barcode; }
        public void setBarcode(String barcode) { this.barcode = barcode; }
        
        public NutritionPer100g getNutritionPer100g() { return nutritionPer100g; }
        public void setNutritionPer100g(NutritionPer100g nutritionPer100g) { this.nutritionPer100g = nutritionPer100g; }
        
        public QualityScores getQualityScores() { return qualityScores; }
        public void setQualityScores(QualityScores qualityScores) { this.qualityScores = qualityScores; }
        
        public List<String> getAdditives() { return additives; }
        public void setAdditives(List<String> additives) { this.additives = additives; }
        
        public List<String> getAllergens() { return allergens; }
        public void setAllergens(List<String> allergens) { this.allergens = allergens; }
        
        public List<String> getLabels() { return labels; }
        public void setLabels(List<String> labels) { this.labels = labels; }
        
        public String getOpenFoodFactsUrl() { return openFoodFactsUrl; }
        public void setOpenFoodFactsUrl(String openFoodFactsUrl) { this.openFoodFactsUrl = openFoodFactsUrl; }
    }
    
    /**
     * Nutrition information per 100g
     */
    public static class NutritionPer100g {
        @JsonProperty("energy_kcal")
        private Double energyKcal;
        
        @JsonProperty("proteins_g")
        private Double proteinsG;
        
        @JsonProperty("carbohydrates_g")
        private Double carbohydratesG;
        
        @JsonProperty("sugars_g")
        private Double sugarsG;
        
        @JsonProperty("fat_g")
        private Double fatG;
        
        @JsonProperty("saturated_fat_g")
        private Double saturatedFatG;
        
        @JsonProperty("fiber_g")
        private Double fiberG;
        
        @JsonProperty("salt_g")
        private Double saltG;
        
        @JsonProperty("sodium_mg")
        private Double sodiumMg;
        
        // Default constructor
        public NutritionPer100g() {}
        
        // Getters and setters
        public Double getEnergyKcal() { return energyKcal; }
        public void setEnergyKcal(Double energyKcal) { this.energyKcal = energyKcal; }
        
        public Double getProteinsG() { return proteinsG; }
        public void setProteinsG(Double proteinsG) { this.proteinsG = proteinsG; }
        
        public Double getCarbohydratesG() { return carbohydratesG; }
        public void setCarbohydratesG(Double carbohydratesG) { this.carbohydratesG = carbohydratesG; }
        
        public Double getSugarsG() { return sugarsG; }
        public void setSugarsG(Double sugarsG) { this.sugarsG = sugarsG; }
        
        public Double getFatG() { return fatG; }
        public void setFatG(Double fatG) { this.fatG = fatG; }
        
        public Double getSaturatedFatG() { return saturatedFatG; }
        public void setSaturatedFatG(Double saturatedFatG) { this.saturatedFatG = saturatedFatG; }
        
        public Double getFiberG() { return fiberG; }
        public void setFiberG(Double fiberG) { this.fiberG = fiberG; }
        
        public Double getSaltG() { return saltG; }
        public void setSaltG(Double saltG) { this.saltG = saltG; }
        
        public Double getSodiumMg() { return sodiumMg; }
        public void setSodiumMg(Double sodiumMg) { this.sodiumMg = sodiumMg; }
    }
    
    /**
     * Quality scores and ratings
     */
    public static class QualityScores {
        @JsonProperty("nutriscore_grade")
        private String nutriscoreGrade;
        
        @JsonProperty("nova_group")
        private Integer novaGroup;
        
        @JsonProperty("ecoscore_grade")
        private String ecoscoreGrade;
        
        // Default constructor
        public QualityScores() {}
        
        // Getters and setters
        public String getNutriscoreGrade() { return nutriscoreGrade; }
        public void setNutriscoreGrade(String nutriscoreGrade) { this.nutriscoreGrade = nutriscoreGrade; }
        
        public Integer getNovaGroup() { return novaGroup; }
        public void setNovaGroup(Integer novaGroup) { this.novaGroup = novaGroup; }
        
        public String getEcoscoreGrade() { return ecoscoreGrade; }
        public void setEcoscoreGrade(String ecoscoreGrade) { this.ecoscoreGrade = ecoscoreGrade; }
    }
} 