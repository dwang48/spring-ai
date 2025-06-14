package health.myvita.spring_ai_demo.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import health.myvita.spring_ai_demo.spring_ai.dto.NutritionAnalysisResponse;
import health.myvita.spring_ai_demo.spring_ai.dto.UserProfileDto;

/**
 * Service for analyzing food images using GPT-4 Vision.
 * This service processes uploaded food images and returns detailed nutritional analysis
 * including identified food items, estimated calories, and macronutrient breakdown.
 */
@Service
public class FoodAnalysisService {
    
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(FoodAnalysisService.class);
    
    // System prompt for food analysis
    private static final String SYSTEM_PROMPT = 
        "You are a professional nutritionist and food analysis expert specializing in visual food identification and nutritional analysis. " +
        "Your task is to analyze food images with precision and provide comprehensive nutritional data.\n" +
        "\n" +
        "CORE RESPONSIBILITIES:\n" +
        "- Identify each food item in the image with high accuracy\n" +
        "- Estimate portion sizes based on visual cues and standard serving sizes\n" +
        "- Calculate nutritional values using USDA/NCCDB standards when possible\n" +
        "- Provide confidence scores for your identifications\n" +
        "- Return data in strict JSON format as specified\n" +
        "\n" +
        "ANALYSIS GUIDELINES:\n" +
        "- Consider cooking methods that affect nutritional content\n" +
        "- Account for visible ingredients and preparation style\n" +
        "- Use standard portion sizes (e.g., 1 cup rice = ~150g cooked)\n" +
        "- Be conservative with confidence scores if uncertain\n" +
        "- Include major micronutrients when identifiable\n" +
        "- Factor in typical restaurant vs home portions\n" +
        "\n" +
        "ACCURACY REQUIREMENTS:\n" +
        "- Calorie estimates should be within ±20% of actual values\n" +
        "- Macronutrient ratios must reflect food composition\n" +
        "- Confidence scores: 0.9+ for obvious items, 0.7+ for likely matches, 0.5+ for uncertain\n" +
        "- If multiple similar foods possible, choose most common preparation\n" +
        "\n" +
        "Always prioritize accuracy over speed. If uncertain about specific values, use conservative estimates and lower confidence scores.";
    
    public FoodAnalysisService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for vision analysis using GPT-4o
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_1_MINI.getValue())
                        .temperature(0.1)  // Low temperature for consistent analysis
                        
                        //this is for o4 mini, you have to set temperature to 1 and reasoning effort
                        // .model(OpenAiApi.ChatModel.O4_MINI.getValue())
                        // .temperature(1.0)
                        // .reasoningEffort("high")
                        .build())
                .build();
        
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Analyzes an uploaded food image and returns nutritional information.
     * 
     * @param imageFile The uploaded food image
     * @param userProfile User's health profile for personalized analysis
     * @return NutritionAnalysisResponse containing food items and nutritional data
     * @throws Exception if analysis fails
     */
    public NutritionAnalysisResponse analyzeFood(MultipartFile imageFile, UserProfileDto userProfile) throws Exception {
        try {
            // Determine mime type (default to image/jpeg if unknown)
            String mimeString = imageFile.getContentType() != null ? imageFile.getContentType() : MimeTypeUtils.IMAGE_JPEG_VALUE;
            // Create media object from the uploaded file
            Media imageMedia = new Media(MimeTypeUtils.parseMimeType(mimeString), imageFile.getResource());
            
            // Create personalized analysis prompt
            String analysisPrompt = createPersonalizedAnalysisPrompt(userProfile);
            
            // Call the vision model
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(u -> u.text(analysisPrompt).media(imageMedia))
                    .call()
                    .content();
            
            // Log the raw response from the model for debugging
            logger.info("Raw food analysis response:\n{}", response);
            
            // Parse the JSON response into our DTO
            return parseNutritionResponse(response);
            
        } catch (Exception e) {
            throw new Exception("Failed to analyze food image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Parses the AI model's JSON response into a NutritionAnalysisResponse object.
     * 
     * @param jsonResponse The JSON response from the AI model
     * @return Parsed NutritionAnalysisResponse
     * @throws JsonProcessingException if JSON parsing fails
     */
    private NutritionAnalysisResponse parseNutritionResponse(String jsonResponse) throws JsonProcessingException {
        try {
            // Clean up the response (remove markdown formatting if present)
            String cleanJson = jsonResponse.trim();
            if (cleanJson.startsWith("```json")) {
                cleanJson = cleanJson.substring(7);
            }
            if (cleanJson.endsWith("```")) {
                cleanJson = cleanJson.substring(0, cleanJson.length() - 3);
            }
            
            // Parse JSON into our DTO
            return objectMapper.readValue(cleanJson, NutritionAnalysisResponse.class);
            
        } catch (JsonProcessingException e) {
            // If parsing fails, return a fallback response
            return createFallbackResponse();
        }
    }
    
    /**
     * Creates a personalized analysis prompt based on user profile.
     * 
     * @param userProfile User's health profile
     * @return Personalized analysis prompt
     */
    private String createPersonalizedAnalysisPrompt(UserProfileDto userProfile) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Analyze this food image for a ");
        if (userProfile != null) {
            prompt.append(userProfile.getAge() != null ? userProfile.getAge() : "adult")
                  .append(" year old ")
                  .append(userProfile.getGender() != null ? userProfile.getGender() : "person");
            
            if (userProfile.getWeight() != null && userProfile.getHeight() != null) {
                prompt.append(" (").append(userProfile.getWeight()).append("kg, ")
                      .append(userProfile.getHeight()).append("cm)");
            }
            
            prompt.append(" with:\n");
            prompt.append("Health conditions: ").append(userProfile.getHealthConditionsSafe()).append("\n");
            prompt.append("Dietary preference: ").append(userProfile.getDietaryPreferenceSafe()).append("\n");
            prompt.append("Allergies: ").append(userProfile.getAllergiesSafe()).append("\n");
            prompt.append("Health goals: ").append(userProfile.getHealthGoalsSafe()).append("\n\n");
        } else {
            prompt.append("person.\n\n");
        }
        
        prompt.append("Provide a detailed nutritional breakdown with personalized insights. ");
        prompt.append("Return your response in the following JSON format:\n");
        prompt.append("{\n");
        prompt.append("  \"foodItems\": [\n");
        prompt.append("    {\n");
        prompt.append("      \"name\": \"food name\",\n");
        prompt.append("      \"quantity\": \"estimated portion size\",\n");
        prompt.append("      \"calories\": estimated_calories,\n");
        prompt.append("      \"confidence\": confidence_score_0_to_1\n");
        prompt.append("    }\n");
        prompt.append("  ],\n");
        prompt.append("  \"totalCalories\": total_estimated_calories,\n");
        prompt.append("  \"macronutrients\": {\n");
        prompt.append("    \"carbohydrates\": grams,\n");
        prompt.append("    \"proteins\": grams,\n");
        prompt.append("    \"fats\": grams,\n");
        prompt.append("    \"fiber\": grams\n");
        prompt.append("  },\n");
        prompt.append("  \"micronutrients\": {\n");
        prompt.append("    \"vitamin_c\": \"amount with unit\",\n");
        prompt.append("    \"iron\": \"amount with unit\",\n");
        prompt.append("    \"calcium\": \"amount with unit\"\n");
        prompt.append("  }");
        
        if (userProfile != null) {
            prompt.append(",\n");
            prompt.append("  \"personalizedInsights\": {\n");
            prompt.append("    \"healthAlignment\": \"How this meal aligns with user's health goals\",\n");
            prompt.append("    \"allergenWarnings\": \"Any allergen concerns based on user profile\",\n");
            prompt.append("    \"portionRecommendation\": \"Recommended portion size for this user\",\n");
            prompt.append("    \"nutritionalHighlights\": \"Key nutrients beneficial for user's health conditions\"\n");
            prompt.append("  }");
        }
        
        prompt.append("\n}");
        
        return prompt.toString();
    }
    
    /**
     * Creates a fallback response when JSON parsing fails.
     * 
     * @return Basic NutritionAnalysisResponse with default values
     */
    private NutritionAnalysisResponse createFallbackResponse() {
        NutritionAnalysisResponse response = new NutritionAnalysisResponse();
        response.setTotalEstimatedCaloriesKcal(0);
        response.setFoodItems(java.util.Collections.emptyList());
        // Set other default values as needed
        return response;
    }
} 