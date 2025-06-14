package health.myvita.spring_ai_demo.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthReportRequest;
import health.myvita.spring_ai_demo.spring_ai.dto.UserProfileDto;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service for generating comprehensive health reports using GPT-4.
 * This service creates detailed health reports from user metrics and data,
 * supporting both individual and batch processing.
 */
@Service
public class HealthReportService {
    
    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    
    // System prompt for health report generation
    private static final String SYSTEM_PROMPT = 
        "You are a health analytics expert and wellness strategist specializing in comprehensive health report generation. " +
        "Your mission is to transform raw health data into meaningful, motivating insights that drive positive behavior change.\n" +
        "\n" +
        "REPORT PHILOSOPHY:\n" +
        "- Focus on progress over perfection - celebrate every improvement\n" +
        "- Provide clear, evidence-based insights without overwhelming detail\n" +
        "- Balance encouragement with honest assessment of areas needing attention\n" +
        "- Make recommendations specific, measurable, achievable, relevant, and time-bound (SMART)\n" +
        "- Use positive psychology principles to motivate continued engagement\n" +
        "\n" +
        "ANALYSIS APPROACH:\n" +
        "- Identify patterns and trends in the data, not just point-in-time snapshots\n" +
        "- Compare metrics to evidence-based health guidelines and personal baselines\n" +
        "- Look for correlations between different health metrics (sleep vs energy, nutrition vs mood)\n" +
        "- Highlight both quantitative improvements and qualitative lifestyle wins\n" +
        "- Consider individual context (age, lifestyle, health goals) in all assessments\n" +
        "\n" +
        "REPORT STRUCTURE:\n" +
        "1. **Executive Summary** - Key wins, primary focus area, overall trajectory\n" +
        "2. **Nutrition Spotlight** - Eating patterns, nutrient quality, hydration, meal timing\n" +
        "3. **Movement & Energy** - Activity levels, exercise consistency, daily movement patterns\n" +
        "4. **Recovery & Sleep** - Sleep quality, duration, consistency, recovery metrics\n" +
        "5. **Wellness Trends** - Week-over-week changes, emerging patterns, correlations\n" +
        "6. **Action Plan** - 3 specific goals for next period, ordered by impact potential\n" +
        "7. **Quick Wins** - 2-3 easy implementations for immediate benefit\n" +
        "\n" +
        "COMMUNICATION STYLE:\n" +
        "- Use encouraging, coach-like tone that builds confidence\n" +
        "- Include specific numbers and percentages to show concrete progress\n" +
        "- Frame challenges as opportunities for growth\n" +
        "- Provide context for why certain metrics matter for long-term health\n" +
        "- Use metaphors and analogies to make complex concepts accessible\n" +
        "\n" +
        "SUCCESS METRICS:\n" +
        "- User feels motivated and empowered after reading the report\n" +
        "- Clear understanding of what's working well and what needs attention\n" +
        "- Concrete, actionable steps for the next reporting period\n" +
        "- Sense of progress and momentum in their health journey";
    
    public HealthReportService(ChatClient.Builder chatClientBuilder) {
        // Configure the chat client for health report generation using GPT-4
        this.chatClient = chatClientBuilder
                .defaultOptions(OpenAiChatOptions.builder()
                        .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
                        .temperature(0.6)  // Balanced temperature for creative yet accurate reports
                        .build())
                .build();
        
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Generates a comprehensive health report for a single user
     * 
     * @param request HealthReportRequest containing user ID, report type, and metrics
     * @param userProfile User's health profile for personalized analysis
     * @return Generated health report as a formatted string
     */
    public String generateHealthReport(HealthReportRequest request, UserProfileDto userProfile) {
        try {
            // Convert metrics to JSON string for the prompt
            String metricsJson = objectMapper.writeValueAsString(request.getMetrics());
            
            // Create personalized report prompt
            String userPrompt = createPersonalizedReportPrompt(request, userProfile, metricsJson);
            
            // Generate the health report
            String report = chatClient.prompt()
                    .options(OpenAiChatOptions.builder()
                            .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
                            .temperature(0.6)
                            .maxTokens(2000)
                            .build())
                    .user(userPrompt)
                    .call()
                    .content();
            
            return report;
            
        } catch (Exception e) {
            // Return a fallback report if generation fails
            return generateFallbackReport(request);
        }
    }
    
    /**
     * Generates health reports for multiple users asynchronously
     * 
     * @param requestsWithProfiles List of request/profile pairs
     * @return CompletableFuture containing list of generated reports
     */
    public CompletableFuture<List<String>> generateBatchReports(List<RequestProfilePair> requestsWithProfiles) {
        return CompletableFuture.supplyAsync(() -> {
            return requestsWithProfiles.parallelStream()
                    .map(pair -> generateHealthReport(pair.request, pair.userProfile))
                    .toList();  // Collect to List
        });
    }
    
    /**
     * Helper class for batch processing
     */
    public static class RequestProfilePair {
        public final HealthReportRequest request;
        public final UserProfileDto userProfile;
        
        public RequestProfilePair(HealthReportRequest request, UserProfileDto userProfile) {
            this.request = request;
            this.userProfile = userProfile;
        }
    }
    
    /**
     * Creates a personalized report prompt based on user profile and metrics.
     * 
     * @param request HealthReportRequest containing report details
     * @param userProfile User's health profile
     * @param metricsJson JSON string of user metrics
     * @return Personalized report prompt
     */
    private String createPersonalizedReportPrompt(HealthReportRequest request, UserProfileDto userProfile, String metricsJson) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("Generate a ").append(request.getReportType()).append(" health report for user ").append(request.getUserId()).append(".\n\n");
        
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
            prompt.append("Profile not available - provide general health analysis\n\n");
        }
        
        prompt.append("HEALTH METRICS FOR THIS PERIOD:\n");
        prompt.append(metricsJson).append("\n\n");
        
        prompt.append("Please create a comprehensive, personalized, encouraging, and actionable health report ");
        prompt.append("in markdown format that considers their individual profile, health conditions, and goals. ");
        prompt.append("The report should be motivating and helpful, with specific recommendations tailored to their ");
        prompt.append("age, health conditions, and personal objectives. Include comparisons to healthy ranges for ");
        prompt.append("their demographic when relevant.");
        
        return prompt.toString();
    }
    
    /**
     * Generates a fallback report when AI generation fails
     * 
     * @param request The original health report request
     * @return A basic fallback health report
     */
    private String generateFallbackReport(HealthReportRequest request) {
        return String.format(
            "# %s Health Report\\n" +
            "\\n" +
            "## Summary\\n" +
            "Thank you for tracking your health metrics! We're currently processing your data " +
            "and will have a detailed report available soon.\\n" +
            "\\n" +
            "## General Recommendations\\n" +
            "- Continue tracking your daily habits\\n" +
            "- Maintain a balanced diet with plenty of vegetables\\n" +
            "- Aim for regular physical activity\\n" +
            "- Prioritize quality sleep\\n" +
            "- Stay hydrated throughout the day\\n" +
            "\\n" +
            "## Next Steps\\n" +
            "Keep up the great work with your health journey!",
            request.getReportType().substring(0, 1).toUpperCase() + 
            request.getReportType().substring(1)
        );
    }
} 