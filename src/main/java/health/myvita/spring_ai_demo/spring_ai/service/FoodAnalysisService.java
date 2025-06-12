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

/**
 * Service for analyzing food images using GPT-4 Vision.
 * This service processes uploaded food images and returns detailed nutritional analysis
 * including identified food items, estimated calories, and macronutrient breakdown.
 */
@Service
public class FoodAnalysisService {
    
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    
    // System prompt for food analysis
    private static final String SYSTEM_PROMPT = 
        "You are a professional nutritionist and food analysis expert. " +
        "Analyze the food image provided and return a detailed nutritional breakdown. " +
        "Be as accurate as possible with portion estimates and nutritional values. " +
        "If you're unsure about specific values, provide reasonable estimates based on " +
        "typical serving sizes and standard nutritional data.";
    
    public FoodAnalysisService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for vision analysis using GPT-4o
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
                        .temperature(0.2)  // Low temperature for consistent analysis
                        .build())
                .build();
        
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Analyzes an uploaded food image and returns nutritional information.
     * 
     * @param imageFile The uploaded food image
     * @return NutritionAnalysisResponse containing food items and nutritional data
     * @throws Exception if analysis fails
     */
    public NutritionAnalysisResponse analyzeFood(MultipartFile imageFile) throws Exception {
        try {
            // Create media object from the uploaded file
            Media imageMedia = new Media(MimeTypeUtils.IMAGE_JPEG, imageFile.getResource());
            
            // Create the analysis prompt
            String analysisPrompt = 
                "Analyze this food image and provide a detailed nutritional breakdown. " +
                "Return your response in the following JSON format:\n" +
                "{\n" +
                "  \"foodItems\": [\n" +
                "    {\n" +
                "      \"name\": \"food name\",\n" +
                "      \"quantity\": \"estimated portion size\",\n" +
                "      \"calories\": estimated_calories,\n" +
                "      \"confidence\": confidence_score_0_to_1\n" +
                "    }\n" +
                "  ],\n" +
                "  \"totalCalories\": total_estimated_calories,\n" +
                "  \"macronutrients\": {\n" +
                "    \"carbohydrates\": grams,\n" +
                "    \"proteins\": grams,\n" +
                "    \"fats\": grams,\n" +
                "    \"fiber\": grams\n" +
                "  },\n" +
                "  \"micronutrients\": {\n" +
                "    \"vitamin_c\": \"amount with unit\",\n" +
                "    \"iron\": \"amount with unit\",\n" +
                "    \"calcium\": \"amount with unit\"\n" +
                "  }\n" +
                "}";
            
            // Call the vision model
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(u -> u.text(analysisPrompt).media(imageMedia))
                    .call()
                    .content();
            
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
     * Creates a fallback response when JSON parsing fails.
     * 
     * @return Basic NutritionAnalysisResponse with default values
     */
    private NutritionAnalysisResponse createFallbackResponse() {
        NutritionAnalysisResponse response = new NutritionAnalysisResponse();
        response.setTotalEstimatedCaloriesKcal(0);
        // Set other default values as needed
        return response;
    }
} 