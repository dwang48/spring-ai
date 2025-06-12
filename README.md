# Spring AI Health Application

A comprehensive health analytics application built with **Spring Boot 3** and **Spring AI 1.0**, featuring three main AI-powered health services for food analysis, health coaching, and report generation.

## 🏗️ Architecture Overview

This application implements a modular Spring Boot architecture with Spring AI integration:

```
src/main/java/health/myvita/spring_ai_demo/spring_ai/
├── controller/          # REST API Controllers  
│   ├── FoodAnalysisController.java
│   ├── HealthCoachController.java
│   └── HealthReportController.java
├── service/             # Business Logic Services
│   ├── FoodAnalysisService.java
│   ├── HealthCoachService.java
│   └── HealthReportService.java
├── dto/                 # Data Transfer Objects
│   ├── FoodItemDto.java
│   ├── NutritionAnalysisResponse.java
│   ├── HealthCoachResponse.java
│   └── HealthReportRequest.java
└── config/              # Configuration Classes
    └── AiConfiguration.java
```

## 🚀 Features

### 1. **Food-in-Image Analyzer** (GPT-4 Vision)
- **Endpoint**: `POST /api/v1/food/analyze`
- **Technology**: GPT-4o with Vision capabilities
- **Functionality**: Analyzes uploaded food images and returns detailed nutritional breakdown
- **Input**: Multipart image file (JPG/PNG)
- **Output**: JSON with detected food items, calories, macronutrients, and confidence scores

### 2. **Health Coach** (GPT-4 Mini)
- **Endpoint**: `POST /api/v1/coach/advice`
- **Technology**: GPT-4o Mini for cost-effective interactions
- **Functionality**: Provides structured health advice based on symptom descriptions
- **Input**: JSON with user symptoms and health concerns
- **Output**: Structured advice with summary, possible causes, tips, and urgency level

### 3. **Health Report Generator** (GPT-4)
- **Endpoints**: 
  - `POST /api/v1/reports/generate` (single report)
  - `POST /api/v1/reports/batch` (batch processing)
- **Technology**: GPT-4o for comprehensive analysis
- **Functionality**: Generates detailed weekly/monthly health reports from user metrics
- **Features**: Async processing, batch support, markdown-formatted reports

## 🛠️ Technology Stack

- **Java**: 21 (LTS)
- **Spring Boot**: 3.x
- **Spring AI**: 1.0.0
- **Maven**: Build management
- **OpenAI**: GPT-4o, GPT-4o Mini, GPT-4 Vision
- **Jackson**: JSON processing

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **OpenAI API Key**

## 🔧 Setup Instructions

### 1. Clone and Navigate
```bash
git clone <repository-url>
cd spring-ai-health-app
```

### 2. Configure Environment
Set your OpenAI API key as an environment variable:

```bash
# macOS/Linux
export OPENAI_API_KEY="your-openai-api-key-here"

# Windows
set OPENAI_API_KEY=your-openai-api-key-here
```

### 3. Build the Project
```bash
./mvnw clean compile
```

### 4. Run the Application
```bash
./mvnw spring-boot:run
```

The application will start on `http://localhost:8080`

## 📚 API Documentation

### Food Analysis API

#### Analyze Food Image
```http
POST /api/v1/food/analyze
Content-Type: multipart/form-data

Parameters:
- image: Image file (JPG/PNG, max 5MB)
```

**Example Response:**
```json
{
  "food_items": [
    {
      "name": "Grilled Chicken Breast",
      "quantity": "150g",
      "calories": 231,
      "confidence": 0.92
    }
  ],
  "total_estimated_calories_kcal": 231,
  "sources": ["OpenAI GPT-4 Vision Analysis"]
}
```

### Health Coach API

#### Get Health Advice
```http
POST /api/v1/coach/advice
Content-Type: application/json

{
  "symptoms": "I've been feeling tired and having headaches",
  "duration": "3 days",
  "severity": "moderate"
}
```

**Example Response:**
```json
{
  "summary": "Experiencing fatigue and headaches for 3 days",
  "possible_causes": [
    "Dehydration",
    "Stress or lack of sleep",
    "Eye strain"
  ],
  "tips": [
    "Increase water intake",
    "Ensure 7-8 hours of sleep",
    "Take regular breaks from screens"
  ],
  "urgency": "low"
}
```

### Health Reports API

#### Generate Single Report
```http
POST /api/v1/reports/generate
Content-Type: application/json

{
  "userId": "user123",
  "reportType": "weekly",
  "metrics": {
    "nutrition": {
      "avg_calories_per_day": 1850,
      "avg_protein": 95,
      "avg_carbs": 220
    },
    "activity": {
      "total_steps": 45000,
      "workout_sessions": 4
    },
    "sleep": {
      "avg_sleep_hours": 7.2,
      "sleep_quality_score": 8.1
    }
  }
}
```

#### Generate Batch Reports
```http
POST /api/v1/reports/batch
Content-Type: application/json

[
  {
    "userId": "user1",
    "reportType": "weekly",
    "metrics": { ... }
  },
  {
    "userId": "user2", 
    "reportType": "monthly",
    "metrics": { ... }
  }
]
```

## ⚙️ Configuration

### Application Properties
```properties
# OpenAI Configuration
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.2

# Server Configuration  
server.port=8080

# File Upload Limits
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

### AI Model Configuration
- **Food Analysis**: GPT-4o (vision capabilities required)
- **Health Coach**: GPT-4o Mini (cost-effective for conversations)
- **Report Generation**: GPT-4o (comprehensive analysis)

## 🔍 Health Check Endpoints

Each service provides a health check endpoint:

- `GET /api/v1/food/health`
- `GET /api/v1/coach/health` 
- `GET /api/v1/reports/health`

## 🏃‍♂️ Running Tests

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report
```

## 🐳 Docker Support

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/spring-ai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# Build and run with Docker
./mvnw clean package
docker build -t spring-ai-health .
docker run -p 8080:8080 -e OPENAI_API_KEY=your-key spring-ai-health
```

## 🔐 Security Considerations

- **API Key Management**: Store OpenAI API key securely using environment variables
- **Input Validation**: All endpoints include comprehensive input validation
- **File Upload Security**: Image uploads are restricted by type and size
- **Rate Limiting**: Consider implementing rate limiting for production use

## 📊 Cost Optimization

### Model Selection Strategy:
- **GPT-4o Mini**: Used for health coaching (lower cost, suitable for conversations)
- **GPT-4o**: Used for complex analysis (food analysis, report generation)
- **Temperature Settings**: Optimized per use case (0.2-0.7)

### Estimated Costs (per 1000 requests):
- Health Coach: ~$0.15 (GPT-4o Mini)
- Food Analysis: ~$10 (GPT-4o with vision)
- Report Generation: ~$30 (GPT-4o with longer outputs)

## 🚧 Development Notes

### From Django/FastAPI to Spring Boot

Key differences for developers familiar with Python frameworks:

| Aspect | Django/FastAPI | Spring Boot |
|--------|----------------|-------------|
| **Routing** | `@app.post("/api/...")` | `@PostMapping("/api/...")` |
| **Dependency Injection** | Manual/FastAPI Depends | `@Autowired` / Constructor injection |
| **Configuration** | `settings.py` / `.env` | `application.properties` |
| **Data Models** | Pydantic/Django Models | POJOs with getters/setters |
| **JSON Handling** | Built-in | Jackson annotations |
| **Async Support** | `async def` | `CompletableFuture<T>` |

### Code Structure Philosophy
- **Controllers**: Handle HTTP concerns (similar to FastAPI routes)
- **Services**: Business logic (similar to service layers in Django)
- **DTOs**: Data transfer objects (similar to Pydantic models)
- **Configuration**: Spring-managed beans (similar to dependency injection)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues:

1. **Compilation Errors**: Ensure Java 21 is being used
   ```bash
   java -version  # Should show Java 21
   export JAVA_HOME=/path/to/java21
   ```

2. **OpenAI API Errors**: Verify API key is set correctly
   ```bash
   echo $OPENAI_API_KEY  # Should display your key
   ```

3. **File Upload Issues**: Check file size limits in application.properties

4. **Port Conflicts**: Change server.port in application.properties if 8080 is in use

### Getting Help:
- Check application logs: `./mvnw spring-boot:run`
- Enable debug logging: Set `logging.level.health.myvita=DEBUG`
- Spring AI Documentation: [docs.spring.io/spring-ai](https://docs.spring.io/spring-ai)

---

**Built with ❤️ using Spring AI 1.0 and OpenAI GPT-4** 