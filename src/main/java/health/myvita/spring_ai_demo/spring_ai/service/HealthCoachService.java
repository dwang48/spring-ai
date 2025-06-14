package health.myvita.spring_ai_demo.spring_ai.service;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthCoachResponse;
import health.myvita.spring_ai_demo.spring_ai.dto.UserProfileDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Service class for health coaching conversations using GPT-4 mini.
 * 
 * This service provides personalized health advice based on user symptoms
 * and follows evidence-based medical guidelines.
 */
@Service
public class HealthCoachService {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthCoachService.class);
    
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    
    // System prompt for health coaching - emphasizes safety and evidence-based advice
    private static final String SYSTEM_PROMPT = 
        "You are an AI health coach assistant with expertise in wellness, nutrition, and lifestyle optimization. " +
        "You provide compassionate, evidence-based guidance while maintaining clear medical boundaries.\n" +
        "\n" +
        "CORE PRINCIPLES:\n" +
        "- You are NOT a medical doctor and cannot diagnose conditions or replace professional medical care\n" +
        "- Focus on evidence-based lifestyle interventions and wellness strategies\n" +
        "- Prioritize user safety - err on side of caution with health concerns\n" +
        "- Be empathetic, encouraging, and non-judgmental\n" +
        "- Provide actionable, practical advice within your scope\n" +
        "\n" +
        "RESPONSE STRUCTURE:\n" +
        "1. Acknowledge their experience with empathy\n" +
        "2. Provide brief, clear summary of their situation\n" +
        "3. Suggest evidence-based lifestyle factors that may contribute\n" +
        "4. Offer 2-3 practical, immediate action steps\n" +
        "5. Assign appropriate urgency level\n" +
        "\n" +
        "URGENCY GUIDELINES:\n" +
        "- NONE: General wellness questions, minor temporary discomfort\n" +
        "- LOW: Mild symptoms, lifestyle optimization questions\n" +
        "- MEDIUM: Persistent symptoms (>1 week), affecting daily life, first-time occurrence\n" +
        "- HIGH: Severe symptoms, sudden changes, potential emergency signs\n" +
        "\n" +
        "SAFETY PROTOCOLS:\n" +
        "- Always recommend professional consultation for persistent or severe symptoms\n" +
        "- Never minimize concerning symptoms\n" +
        "- Provide clear red flags that require immediate medical attention\n" +
        "- Focus on nutrition, exercise, sleep, stress management, and hydration\n" +
        "- Avoid specific supplement recommendations without professional guidance\n" +
        "\n" +
        "Your goal is to empower users with knowledge while ensuring they seek appropriate professional care when needed.";
    
    public HealthCoachService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for health coaching using GPT-4 mini
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        // .model(OpenAiApi.ChatModel.GPT_4_1_MINI.getValue())
                        // .temperature(0.1)  // Low temperature for consistent analysis
                        .model(OpenAiApi.ChatModel.O4_MINI.getValue())
                        .temperature(1.0)
                        .reasoningEffort("high")
                        .build())
                .build();
        
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Provides health coaching advice based on user symptoms or concerns.
     * 
     * @param userMessage The user's description of how they feel or their health concerns
     * @param userProfile User's health profile for personalized advice
     * @return HealthCoachResponse with structured advice and recommendations
     */
    public HealthCoachResponse provideHealthAdvice(String userMessage, UserProfileDto userProfile) {
        try {
            logger.info("Processing health coaching request");
            logger.debug("User message: {}", userMessage);
            
            // Create personalized coaching prompt
            String personalizedPrompt = createPersonalizedCoachingPrompt(userMessage, userProfile);
            
            // Use the default GPT_4_O_MINI model configured in the constructor
            String response = chatClient.prompt()
                    .system(SYSTEM_PROMPT)
                    .user(personalizedPrompt)
                    .call()
                    .content();
            
            logger.debug("Health coach response: {}", response);
            
            // Parse the structured response into our DTO
            return parseHealthCoachResponse(response);
            
        } catch (Exception e) {
            logger.error("Error providing health advice", e);
            throw new RuntimeException("Failed to provide health advice: " + e.getMessage(), e);
        }
    }
    
    /**
     * Creates a personalized coaching prompt based on user message and profile.
     * 
     * @param userMessage User's health concerns or symptoms
     * @param userProfile User's health profile
     * @return Personalized coaching prompt
     */
    private String createPersonalizedCoachingPrompt(String userMessage, UserProfileDto userProfile) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("USER PROFILE:\n");
        if (userProfile != null) {
            prompt.append("Age: ").append(userProfile.getAge() != null ? userProfile.getAge() : "Not specified").append("\n");
            prompt.append("Gender: ").append(userProfile.getGender() != null ? userProfile.getGender() : "Not specified").append("\n");
            
            if (userProfile.getWeight() != null && userProfile.getHeight() != null) {
                prompt.append("Weight: ").append(userProfile.getWeight()).append("kg\n");
                prompt.append("Height: ").append(userProfile.getHeight()).append("cm\n");
            }
            
            prompt.append("Health conditions: ").append(userProfile.getHealthConditionsSafe()).append("\n");
            prompt.append("Dietary preference: ").append(userProfile.getDietaryPreferenceSafe()).append("\n");
            prompt.append("Allergies: ").append(userProfile.getAllergiesSafe()).append("\n");
            prompt.append("Health goals: ").append(userProfile.getHealthGoalsSafe()).append("\n");
            prompt.append("Activity level: ").append(userProfile.getActivityLevelSafe()).append("\n\n");
        } else {
            prompt.append("Profile not available - provide general guidance\n\n");
        }
        
        prompt.append("USER MESSAGE:\n");
        prompt.append(userMessage).append("\n\n");
        
        prompt.append("Please provide personalized health coaching advice considering their profile. ");
        prompt.append("Provide your response in a structured format:\n\n");
        prompt.append("Summary: [Brief summary of what the user is experiencing]\n\n");
        prompt.append("Possible Causes:\n");
        prompt.append("- [Cause 1 - consider their health conditions and lifestyle]\n");
        prompt.append("- [Cause 2 - factor in their age/gender if relevant]\n\n");
        prompt.append("Tips:\n");
        prompt.append("- [Tip 1 - personalized to their health goals]\n");
        prompt.append("- [Tip 2 - consider their dietary preferences/allergies]\n");
        prompt.append("- [Tip 3 - appropriate for their activity level]\n\n");
        prompt.append("Urgency: [none/low/medium/high]");
        
        return prompt.toString();
    }
    
    /**
     * Parses the structured response from GPT-4 mini into our DTO.
     * This is a simplified parser - in production you might want more robust parsing.
     */
    private HealthCoachResponse parseHealthCoachResponse(String response) {
        try {
            // Parse the structured text response
            String[] sections = response.split("\\n\\n");
            
            String summary = "";
            java.util.List<String> causes = new java.util.ArrayList<>();
            java.util.List<String> tips = new java.util.ArrayList<>();
            String urgency = "medium";
            
            for (String section : sections) {
                if (section.startsWith("Summary:")) {
                    summary = section.substring("Summary:".length()).trim();
                } else if (section.startsWith("Possible Causes:")) {
                    String causesText = section.substring("Possible Causes:".length()).trim();
                    String[] causeLines = causesText.split("\\n");
                    for (String cause : causeLines) {
                        if (cause.startsWith("- ")) {
                            causes.add(cause.substring(2).trim());
                        }
                    }
                } else if (section.startsWith("Tips:")) {
                    String tipsText = section.substring("Tips:".length()).trim();
                    String[] tipLines = tipsText.split("\\n");
                    for (String tip : tipLines) {
                        if (tip.startsWith("- ")) {
                            tips.add(tip.substring(2).trim());
                        }
                    }
                } else if (section.startsWith("Urgency:")) {
                    urgency = section.substring("Urgency:".length()).trim().toLowerCase();
                }
            }
            
            // Fallback values if parsing didn't work perfectly
            if (summary.isEmpty()) {
                summary = "Based on your description, here's some general health guidance.";
            }
            if (causes.isEmpty()) {
                causes.add("Various lifestyle factors could contribute to your symptoms");
            }
            if (tips.isEmpty()) {
                tips.add("Consider consulting with a healthcare professional");
                tips.add("Maintain a balanced diet and regular exercise");
                tips.add("Ensure adequate sleep and manage stress");
            }
            
            return new HealthCoachResponse(summary, causes, tips, urgency);
            
        } catch (Exception e) {
            logger.error("Error parsing health coach response", e);
            // Return a safe fallback response
            return new HealthCoachResponse(
                "I understand you have some health concerns. Here's some general guidance.",
                java.util.List.of("Various factors could contribute to how you're feeling"),
                java.util.List.of(
                    "Consider speaking with a healthcare professional",
                    "Maintain a balanced diet and stay hydrated",
                    "Get adequate sleep and manage stress levels"
                ),
                "medium"
            );
        }
    }
} 