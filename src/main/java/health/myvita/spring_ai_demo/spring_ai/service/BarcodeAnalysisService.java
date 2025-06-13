package health.myvita.spring_ai_demo.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import health.myvita.spring_ai_demo.spring_ai.dto.BarcodeAnalysisResponse;
import health.myvita.spring_ai_demo.spring_ai.dto.UserProfileDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Service for analyzing food products by barcode using OpenFoodFacts API and AI analysis.
 * This service fetches product information from OpenFoodFacts and provides personalized
 * nutritional recommendations based on user profile.
 */
@Service
public class BarcodeAnalysisService {
    
    private final ChatClient chatClient;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BarcodeAnalysisService.class);
    
    // OpenFoodFacts API base URL
    private static final String OPENFOODFACTS_API_BASE = "https://world.openfoodfacts.org/api/v2/product/";
    
    // System prompt for barcode analysis
    private static final String SYSTEM_PROMPT = 
        "You are a professional nutritionist specializing in packaged food analysis. " +
        "Analyze the scanned product information and provide personalized recommendations " +
        "based on the user's health profile. Be accurate, helpful, and focus on actionable advice.";
    
    public BarcodeAnalysisService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for nutritional analysis
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_1_MINI.getValue())
                        .temperature(0.1)  // Lower temperature for consistent health advice
                        .maxTokens(1000)
                        .build())
                .build();
        
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Analyzes a product by barcode and provides personalized nutritional recommendations.
     * 
     * @param barcode The product barcode
     * @param userProfile User's health profile for personalization
     * @return BarcodeAnalysisResponse containing product info and analysis
     * @throws Exception if analysis fails
     */
    public BarcodeAnalysisResponse analyzeProductByBarcode(String barcode, UserProfileDto userProfile) throws Exception {
        try {
            // Validate inputs
            if (barcode == null || barcode.trim().isEmpty()) {
                throw new IllegalArgumentException("Barcode cannot be empty");
            }
            
            if (userProfile == null || !userProfile.isValid()) {
                throw new IllegalArgumentException("Valid user profile is required");
            }
            
            // Fetch product data from OpenFoodFacts
            BarcodeAnalysisResponse.ProductInfo productInfo = fetchProductFromOpenFoodFacts(barcode);
            
            if (productInfo == null) {
                return createProductNotFoundResponse(barcode);
            }
            
            // Generate AI analysis based on product and user profile
            String nutritionalAnalysis = generateNutritionalAnalysis(productInfo, userProfile);
            
            // Generate recommendation
            String recommendation = generateRecommendation(productInfo, userProfile);
            
            // Create response
            BarcodeAnalysisResponse response = new BarcodeAnalysisResponse();
            response.setProductInfo(productInfo);
            response.setNutritionalAnalysis(nutritionalAnalysis);
            response.setRecommendation(recommendation);
            response.setSource("OpenFoodFacts + AI Analysis");
            
            return response;
            
        } catch (Exception e) {
            logger.error("Error analyzing product with barcode {}: {}", barcode, e.getMessage());
            throw new Exception("Failed to analyze product: " + e.getMessage(), e);
        }
    }
    
    /**
     * Fetches product information from OpenFoodFacts API.
     * 
     * @param barcode Product barcode
     * @return ProductInfo object or null if not found
     */
    private BarcodeAnalysisResponse.ProductInfo fetchProductFromOpenFoodFacts(String barcode) {
        try {
            String url = OPENFOODFACTS_API_BASE + barcode;
            
            // Set User-Agent header as required by OpenFoodFacts
            restTemplate.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("User-Agent", "HealthAI-Spring-Service/1.0");
                return execution.execute(request, body);
            });
            
            String response = restTemplate.getForObject(url, String.class);
            
            if (response == null) {
                return null;
            }
            
            // Parse JSON response
            JsonNode root = objectMapper.readTree(response);
            
            if (root.get("status").asInt() != 1 || !root.has("product")) {
                logger.warn("Product not found in OpenFoodFacts for barcode: {}", barcode);
                return null;
            }
            
            JsonNode product = root.get("product");
            
            // Transform OpenFoodFacts data to our ProductInfo structure
            return transformToProductInfo(product, barcode);
            
        } catch (RestClientException e) {
            logger.error("Error fetching data from OpenFoodFacts: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            logger.error("Error parsing OpenFoodFacts response: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Transforms OpenFoodFacts JSON data to our ProductInfo DTO.
     */
    private BarcodeAnalysisResponse.ProductInfo transformToProductInfo(JsonNode product, String barcode) {
        BarcodeAnalysisResponse.ProductInfo productInfo = new BarcodeAnalysisResponse.ProductInfo();
        
        // Basic product information
        productInfo.setName(getJsonValueAsString(product, "product_name", "Unknown Product"));
        productInfo.setBrand(getJsonValueAsString(product, "brands", "Unknown Brand"));
        productInfo.setCategories(getJsonValueAsString(product, "categories", "Unknown"));
        productInfo.setIngredientsText(getJsonValueAsString(product, "ingredients_text", "Not available"));
        productInfo.setBarcode(barcode);
        productInfo.setOpenFoodFactsUrl("https://world.openfoodfacts.org/product/" + barcode);
        
        // Nutrition information
        BarcodeAnalysisResponse.NutritionPer100g nutrition = new BarcodeAnalysisResponse.NutritionPer100g();
        JsonNode nutriments = product.get("nutriments");
        if (nutriments != null) {
            nutrition.setEnergyKcal(getJsonValueAsDouble(nutriments, "energy-kcal_100g"));
            nutrition.setProteinsG(getJsonValueAsDouble(nutriments, "proteins_100g"));
            nutrition.setCarbohydratesG(getJsonValueAsDouble(nutriments, "carbohydrates_100g"));
            nutrition.setSugarsG(getJsonValueAsDouble(nutriments, "sugars_100g"));
            nutrition.setFatG(getJsonValueAsDouble(nutriments, "fat_100g"));
            nutrition.setSaturatedFatG(getJsonValueAsDouble(nutriments, "saturated-fat_100g"));
            nutrition.setFiberG(getJsonValueAsDouble(nutriments, "fiber_100g"));
            nutrition.setSaltG(getJsonValueAsDouble(nutriments, "salt_100g"));
            nutrition.setSodiumMg(getJsonValueAsDouble(nutriments, "sodium_100g"));
        }
        productInfo.setNutritionPer100g(nutrition);
        
        // Quality scores
        BarcodeAnalysisResponse.QualityScores qualityScores = new BarcodeAnalysisResponse.QualityScores();
        qualityScores.setNutriscoreGrade(getJsonValueAsString(product, "nutrition_grades", "Not available"));
        qualityScores.setNovaGroup(getJsonValueAsInteger(product, "nova_group"));
        qualityScores.setEcoscoreGrade(getJsonValueAsString(product, "ecoscore_grade", "Not available"));
        productInfo.setQualityScores(qualityScores);
        
        // Additional information
        productInfo.setAdditives(extractListFromJsonArray(product, "additives_tags"));
        productInfo.setAllergens(extractListFromJsonArray(product, "allergens_tags"));
        productInfo.setLabels(extractListFromJsonArray(product, "labels_tags"));
        
        return productInfo;
    }
    
    /**
     * Generates nutritional analysis using AI based on product and user profile.
     */
    private String generateNutritionalAnalysis(BarcodeAnalysisResponse.ProductInfo productInfo, UserProfileDto userProfile) {
        String analysisPrompt = createAnalysisPrompt(productInfo, userProfile);
        
        try {
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(analysisPrompt)
                    .call()
                    .content();
            
            logger.info("Generated nutritional analysis for barcode: {}", productInfo.getBarcode());
            return response;
            
        } catch (Exception e) {
            logger.error("Error generating nutritional analysis: {}", e.getMessage());
            return "Unable to generate detailed analysis at this time. Please consult with a nutritionist for personalized advice.";
        }
    }
    
    /**
     * Generates recommendation based on product and user profile.
     */
    private String generateRecommendation(BarcodeAnalysisResponse.ProductInfo productInfo, UserProfileDto userProfile) {
        // Simple rule-based recommendation logic
        BarcodeAnalysisResponse.QualityScores scores = productInfo.getQualityScores();
        
        if (scores != null && scores.getNutriscoreGrade() != null) {
            String grade = scores.getNutriscoreGrade().toLowerCase();
            switch (grade) {
                case "a":
                case "b":
                    return "RECOMMENDED - This product has good nutritional quality.";
                case "c":
                    return "CAUTION - Moderate nutritional quality. Consider portion sizes.";
                case "d":
                case "e":
                    return "AVOID - Poor nutritional quality. Look for healthier alternatives.";
                default:
                    return "REVIEW - Nutritional information incomplete. Check ingredients carefully.";
            }
        }
        
        return "REVIEW - Unable to determine recommendation. Consult nutritional information.";
    }
    
    /**
     * Creates the analysis prompt for AI.
     */
    private String createAnalysisPrompt(BarcodeAnalysisResponse.ProductInfo productInfo, UserProfileDto userProfile) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Analyze this scanned food product for a ")
              .append(userProfile.getAge()).append(" year old ")
              .append(userProfile.getGender()).append(" with:\n");
        
        if (userProfile.getWeight() != null && userProfile.getHeight() != null) {
            prompt.append("Weight: ").append(userProfile.getWeight()).append("kg\n");
            prompt.append("Height: ").append(userProfile.getHeight()).append("cm\n");
        }
        
        prompt.append("Health conditions: ").append(userProfile.getHealthConditionsSafe()).append("\n");
        prompt.append("Dietary preference: ").append(userProfile.getDietaryPreferenceSafe()).append("\n");
        prompt.append("Allergies: ").append(userProfile.getAllergiesSafe()).append("\n");
        prompt.append("Health goals: ").append(userProfile.getHealthGoalsSafe()).append("\n\n");
        
        prompt.append("Scanned Product Information:\n");
        prompt.append("Name: ").append(productInfo.getName()).append("\n");
        prompt.append("Brand: ").append(productInfo.getBrand()).append("\n");
        prompt.append("Categories: ").append(productInfo.getCategories()).append("\n");
        prompt.append("Ingredients: ").append(productInfo.getIngredientsText()).append("\n\n");
        
        BarcodeAnalysisResponse.NutritionPer100g nutrition = productInfo.getNutritionPer100g();
        if (nutrition != null) {
            prompt.append("Nutrition per 100g:\n");
            if (nutrition.getEnergyKcal() != null) prompt.append("Energy: ").append(nutrition.getEnergyKcal()).append(" kcal\n");
            if (nutrition.getProteinsG() != null) prompt.append("Protein: ").append(nutrition.getProteinsG()).append("g\n");
            if (nutrition.getCarbohydratesG() != null) prompt.append("Carbohydrates: ").append(nutrition.getCarbohydratesG()).append("g\n");
            if (nutrition.getFatG() != null) prompt.append("Fat: ").append(nutrition.getFatG()).append("g\n");
            if (nutrition.getFiberG() != null) prompt.append("Fiber: ").append(nutrition.getFiberG()).append("g\n");
            if (nutrition.getSaltG() != null) prompt.append("Salt: ").append(nutrition.getSaltG()).append("g\n");
        }
        
        BarcodeAnalysisResponse.QualityScores scores = productInfo.getQualityScores();
        if (scores != null) {
            prompt.append("\nQuality Indicators:\n");
            prompt.append("Nutri-score: ").append(scores.getNutriscoreGrade()).append("\n");
            prompt.append("NOVA group: ").append(scores.getNovaGroup()).append("\n");
            prompt.append("Eco-score: ").append(scores.getEcoscoreGrade()).append("\n");
        }
        
        prompt.append("\nPlease provide a comprehensive analysis including:");
        prompt.append("\n1. Nutritional overview and quality assessment");
        prompt.append("\n2. Health considerations for this user's conditions and goals");
        prompt.append("\n3. Portion size recommendations");
        prompt.append("\n4. Key benefits or concerns");
        prompt.append("\nKeep response under 400 words and format in markdown.");
        
        return prompt.toString();
    }
    
    /**
     * Creates response when product is not found.
     */
    private BarcodeAnalysisResponse createProductNotFoundResponse(String barcode) {
        BarcodeAnalysisResponse response = new BarcodeAnalysisResponse();
        response.setNutritionalAnalysis("Product not found in OpenFoodFacts database. This could mean the product is not yet catalogued or the barcode was scanned incorrectly.");
        response.setRecommendation("UNKNOWN - Product information unavailable");
        response.setSource("OpenFoodFacts - Product Not Found");
        
        // Create minimal product info
        BarcodeAnalysisResponse.ProductInfo productInfo = new BarcodeAnalysisResponse.ProductInfo();
        productInfo.setName("Product Not Found");
        productInfo.setBrand("Unknown");
        productInfo.setBarcode(barcode);
        productInfo.setOpenFoodFactsUrl("https://world.openfoodfacts.org/cgi/product_jqm2.pl?code=" + barcode);
        response.setProductInfo(productInfo);
        
        return response;
    }
    
    // Helper methods for JSON parsing
    private String getJsonValueAsString(JsonNode node, String fieldName, String defaultValue) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asText() : defaultValue;
    }
    
    private Double getJsonValueAsDouble(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asDouble() : null;
    }
    
    private Integer getJsonValueAsInteger(JsonNode node, String fieldName) {
        JsonNode field = node.get(fieldName);
        return field != null && !field.isNull() ? field.asInt() : null;
    }
    
    private List<String> extractListFromJsonArray(JsonNode node, String fieldName) {
        JsonNode arrayNode = node.get(fieldName);
        List<String> result = new ArrayList<>();
        
        if (arrayNode != null && arrayNode.isArray()) {
            for (JsonNode item : arrayNode) {
                result.add(item.asText());
            }
        }
        
        return result;
    }
} 