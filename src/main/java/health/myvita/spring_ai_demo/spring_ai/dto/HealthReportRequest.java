package health.myvita.spring_ai_demo.spring_ai.dto;

import java.util.Map;

/**
 * Request DTO for generating weekly/monthly health reports.
 * Contains user metrics data for the specified period.
 */
public class HealthReportRequest {
    private String userId;
    private String reportType; // "weekly" or "monthly"
    private Map<String, Object> metrics; // Flexible structure for various health metrics
    
    // Default constructor
    public HealthReportRequest() {}
    
    // Constructor
    public HealthReportRequest(String userId, String reportType, Map<String, Object> metrics) {
        this.userId = userId;
        this.reportType = reportType;
        this.metrics = metrics;
    }
    
    // Getters and setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    
    public String getReportType() { return reportType; }
    public void setReportType(String reportType) { this.reportType = reportType; }
    
    public Map<String, Object> getMetrics() { return metrics; }
    public void setMetrics(Map<String, Object> metrics) { this.metrics = metrics; }
} 