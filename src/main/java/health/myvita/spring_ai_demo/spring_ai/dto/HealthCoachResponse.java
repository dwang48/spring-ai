package health.myvita.spring_ai_demo.spring_ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * Response DTO for the health coach conversation endpoint.
 * Provides structured advice based on user symptoms.
 */
public class HealthCoachResponse {
    private String summary;
    
    @JsonProperty("possible_causes")
    private List<String> possibleCauses;
    
    private List<String> tips;
    
    private String urgency;  // none | low | medium | high
    
    // Default constructor
    public HealthCoachResponse() {}
    
    // Constructor
    public HealthCoachResponse(String summary, List<String> possibleCauses, 
                              List<String> tips, String urgency) {
        this.summary = summary;
        this.possibleCauses = possibleCauses;
        this.tips = tips;
        this.urgency = urgency;
    }
    
    // Getters and setters
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    
    public List<String> getPossibleCauses() { return possibleCauses; }
    public void setPossibleCauses(List<String> possibleCauses) { this.possibleCauses = possibleCauses; }
    
    public List<String> getTips() { return tips; }
    public void setTips(List<String> tips) { this.tips = tips; }
    
    public String getUrgency() { return urgency; }
    public void setUrgency(String urgency) { this.urgency = urgency; }
} 