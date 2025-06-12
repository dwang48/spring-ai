package health.myvita.spring_ai_demo.spring_ai.service;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthCoachResponse;
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
        "You are a health coach assistant. You provide helpful, evidence-based guidance " +
        "but you are NOT a doctor and cannot diagnose medical conditions.\n" +
        "\n" +
        "Guidelines:\n" +
        "- Be concise, friendly, and supportive\n" +
        "- Provide evidence-based suggestions\n" +
        "- Always recommend consulting healthcare professionals for serious symptoms\n" +
        "- Focus on lifestyle factors (nutrition, exercise, sleep, stress)\n" +
        "- Be conservative with urgency levels\n" +
        "- Never provide specific medical diagnoses\n" +
        "\n" +
        "Respond with structured advice including:\n" +
        "- Brief summary of the user's situation\n" +
        "- Possible general causes (lifestyle factors)\n" +
        "- Practical tips they can try\n" +
        "- Urgency level: none, low, medium, or high\n" +
        "\n" +
        "If symptoms are concerning (persistent, severe, or unusual), always recommend " +
        "consulting a healthcare professional and set urgency to medium or high.";
    
    public HealthCoachService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for health coaching using GPT-4 mini
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O_MINI.getValue())
                        .temperature(0.7)  // Higher temperature for more conversational responses
                        .build())
                .build();
        
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Provides health coaching advice based on user symptoms or concerns.
     * 
     * @param userMessage The user's description of how they feel or their health concerns
     * @return HealthCoachResponse with structured advice and recommendations
     */
    public HealthCoachResponse provideHealthAdvice(String userMessage) {
        try {
            logger.info("Processing health coaching request");
            logger.debug("User message: {}", userMessage);
            
            // Create chat options for this specific request
            OpenAiChatOptions requestOptions = OpenAiChatOptions.builder()
                    .model(OpenAiApi.ChatModel.GPT_4_O_MINI.getValue())
                    .temperature(0.7)  // Slightly higher temperature for conversational responses
                    .maxTokens(800)
                    .build();
            
            String response = chatClient.prompt()
                    .options(requestOptions)
                    .user(userMessage)
                    .user("Please provide your response in a structured format:\\n" +
                          "\\n" +
                          "Summary: [Brief summary of what the user is experiencing]\\n" +
                          "\\n" +
                          "Possible Causes:\\n" +
                          "- [Cause 1]\\n" +
                          "- [Cause 2]\\n" +
                          "\\n" +
                          "Tips:\\n" +
                          "- [Tip 1]\\n" +
                          "- [Tip 2]\\n" +
                          "- [Tip 3]\\n" +
                          "\\n" +
                          "Urgency: [none/low/medium/high]")
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