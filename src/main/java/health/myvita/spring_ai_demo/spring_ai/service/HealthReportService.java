package health.myvita.spring_ai_demo.spring_ai.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import health.myvita.spring_ai_demo.spring_ai.dto.HealthReportRequest;

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
        "You are a health analytics expert creating personalized health reports. " +
        "Analyze the provided health metrics and create a comprehensive, actionable report. " +
        "Guidelines: " +
        "- Be encouraging and positive while being honest about areas for improvement " +
        "- Provide specific, actionable recommendations " +
        "- Compare current metrics to previous periods when available " +
        "- Highlight both achievements and areas for growth " +
        "- Use clear, non-medical language " +
        "- Include specific goals and targets " +
        "Structure your report with: " +
        "1. Executive Summary " +
        "2. Nutrition Analysis " +
        "3. Activity/Exercise Review " +
        "4. Sleep Quality Assessment " +
        "5. Key Insights and Trends " +
        "6. Recommendations for Next Period " +
        "7. Specific Action Items";
    
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
     * @return Generated health report as a formatted string
     */
    public String generateHealthReport(HealthReportRequest request) {
        try {
            // Convert metrics to JSON string for the prompt
            String metricsJson = objectMapper.writeValueAsString(request.getMetrics());
            
            // Create the user prompt with metrics data
            String userPrompt = String.format(
                "Generate a %s health report for user %s.\\n" +
                "\\n" +
                "Here are the health metrics for this period:\\n" +
                "\\n" +
                "%s\\n" +
                "\\n" +
                "Please create a comprehensive, encouraging, and actionable health report " +
                "in markdown format that the user will find motivating and helpful.",
                request.getReportType(), 
                request.getUserId(),
                metricsJson
            );
            
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
     * @param requests List of HealthReportRequest objects
     * @return CompletableFuture containing list of generated reports
     */
    public CompletableFuture<List<String>> generateBatchReports(List<HealthReportRequest> requests) {
        return CompletableFuture.supplyAsync(() -> {
            return requests.parallelStream()
                    .map(this::generateHealthReport)
                    .toList();  // Collect to List
        });
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