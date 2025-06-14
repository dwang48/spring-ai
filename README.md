# Spring AI Health Application

A comprehensive **AI-powered health analytics platform** built with **Spring Boot 3** and **Spring AI 1.0**, featuring four main AI-driven health services with personalized user profile integration and barcode scanning capabilities.

## 🏗️ Architecture Overview

This application implements a modular Spring Boot architecture with Spring AI integration:

```
src/main/java/health/myvita/spring_ai_demo/spring_ai/
├── controller/          # REST API Controllers  
│   ├── FoodAnalysisController.java      # Food image analysis
│   ├── HealthCoachController.java       # Health coaching conversations
│   ├── HealthReportController.java      # Health report generation
│   └── BarcodeController.java           # 🆕 Barcode scanning & analysis
├── service/             # Business Logic Services
│   ├── FoodAnalysisService.java         # Enhanced with user profiles
│   ├── HealthCoachService.java          # Enhanced with personalization
│   ├── HealthReportService.java         # Enhanced with user context
│   └── BarcodeAnalysisService.java      # 🆕 OpenFoodFacts + AI integration
├── dto/                 # Data Transfer Objects
│   ├── FoodItemDto.java
│   ├── NutritionAnalysisResponse.java
│   ├── HealthCoachResponse.java
│   ├── HealthReportRequest.java
│   ├── UserProfileDto.java              # 🆕 User health profile
│   └── BarcodeAnalysisResponse.java     # 🆕 Barcode scan results
└── config/              # Configuration Classes
    └── AiConfiguration.java
```

## 🚀 Features

### 1. **🍎 Food-in-Image Analyzer** (GPT-4 Vision)
- **Endpoint**: `POST /api/v1/food/analyze`
- **Technology**: GPT-4.1 Mini with enhanced prompts
- **🆕 Enhanced Features**:
  - **Personalized Analysis**: Considers user's health conditions, allergies, and goals
  - **USDA Standards**: Uses professional nutritionist-grade analysis
  - **Confidence Scoring**: Provides accuracy confidence for each food identification
  - **User Profile Integration**: Tailored insights based on individual health profile
- **Input**: Multipart form with image file + user profile JSON
- **Output**: Comprehensive nutritional analysis with personalized recommendations

### 2. **🧠 Health Coach** (O4 Mini)
- **Endpoint**: `POST /api/v1/coach/advice`
- **Technology**: O4 Mini with high reasoning effort
- **🆕 Enhanced Features**:
  - **Empathetic AI**: Uses positive psychology principles
  - **Safety Protocols**: Built-in medical boundary awareness
  - **Personalized Coaching**: Adapts advice to user's health profile
  - **Structured Urgency**: Clear urgency levels (none/low/medium/high)
- **Input**: User symptoms + comprehensive health profile
- **Output**: Structured advice with causes, tips, and safety recommendations

### 3. **📊 Health Report Generator** (GPT-4o)
- **Endpoints**: 
  - `POST /api/v1/reports/generate` (single report)
  - `POST /api/v1/reports/batch` (batch processing)
- **Technology**: GPT-4o with comprehensive analysis prompts
- **🆕 Enhanced Features**:
  - **Behavior Change Focus**: Uses SMART goals and positive psychology
  - **Trend Analysis**: Identifies patterns and correlations in health data
  - **Personalized Context**: Considers age, lifestyle, and health goals
  - **Professional Grade**: Evidence-based insights with scientific context
- **Features**: Async processing, batch support, markdown-formatted reports

### 4. **🔍 Barcode Scanner & Analyzer** (NEW!)
- **Endpoints**:
  - `POST /api/v1/barcode/scan` (full analysis with user profile)
  - `GET /api/v1/barcode/lookup/{barcode}` (basic product lookup)
- **Technology**: OpenFoodFacts API + GPT-4.1 Mini analysis
- **Key Features**:
  - **Real-time Product Data**: Integrates with OpenFoodFacts database
  - **Quality Scoring**: Nutri-Score, NOVA, and Eco-Score integration
  - **Personalized Recommendations**: Tailored advice based on health conditions
  - **Allergy Warnings**: Automatic allergen detection and warnings
  - **Portion Recommendations**: Customized serving size suggestions
- **Input**: Barcode + user health profile
- **Output**: Complete product analysis with personalized nutrition insights

## 🧑‍⚕️ User Profile System (NEW!)

All services now support comprehensive user profiling for personalized health insights:

```json
{
  "age": 30,
  "gender": "female",
  "weight": 65.0,
  "height": 165.0,
  "health_conditions": "diabetes, hypertension",
  "dietary_preference": "vegetarian",
  "allergies": "nuts, shellfish",
  "health_goals": "weight loss, better sleep",
  "activity_level": "moderate"
}
```

**Personalization Benefits**:
- **Targeted Advice**: Health recommendations specific to conditions and goals
- **Safety First**: Considers allergies and health conditions in all analyses
- **Lifestyle Context**: Adapts recommendations to activity level and preferences
- **Progress Tracking**: Aligns advice with personal health objectives

## 🛠️ Technology Stack

- **Java**: 21 (LTS)
- **Spring Boot**: 3.x
- **Spring AI**: 1.0.0
- **Maven**: Build management
- **OpenAI**: GPT-4o, GPT-4.1 Mini, O4 Mini
- **🆕 OpenFoodFacts API**: Product database integration
- **Jackson**: JSON processing
- **RestTemplate**: HTTP client for external APIs

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **OpenAI API Key**
- Internet connection (for OpenFoodFacts API)

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

### 🍎 Food Analysis API (Enhanced)

#### Analyze Food Image with User Profile
```http
POST /api/v1/food/analyze
Content-Type: multipart/form-data

Parameters:
- image: Image file (JPG/PNG, max 5MB)
- userProfile: JSON string with user health profile
```

**Example with cURL:**
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@food_image.jpg" \
  -F 'userProfile={
    "age": 30,
    "gender": "female",
    "weight": 65.0,
    "height": 165.0,
    "health_conditions": "diabetes",
    "dietary_preference": "vegetarian",
    "allergies": "nuts",
    "health_goals": "weight loss",
    "activity_level": "moderate"
  }'
```

**Enhanced Response:**
```json
{
  "foodItems": [
    {
      "name": "Grilled Chicken Breast",
      "quantity": "150g",
      "calories": 231,
      "confidence": 0.92
    }
  ],
  "totalCalories": 231,
  "macronutrients": {
    "carbohydrates": 0,
    "proteins": 43.5,
    "fats": 5.0,
    "fiber": 0
  },
  "personalizedInsights": {
    "healthAlignment": "Excellent protein source for weight loss goals",
    "allergenWarnings": "No known allergens detected",
    "portionRecommendation": "Appropriate portion size for your goals",
    "nutritionalHighlights": ["High protein", "Low carb", "Suitable for diabetes management"]
  }
}
```

### 🧠 Health Coach API (Enhanced)

#### Get Personalized Health Advice
```http
POST /api/v1/coach/advice
Content-Type: application/json

{
  "message": "I've been feeling tired and having headaches after lunch",
  "userProfile": {
    "age": 35,
    "gender": "male",
    "health_conditions": "diabetes",
    "dietary_preference": "low-carb",
    "health_goals": "better energy levels"
  }
}
```

**Enhanced Response:**
```json
{
  "summary": "Post-meal fatigue and headaches in diabetic individual",
  "possibleCauses": [
    "Blood sugar fluctuations after meals",
    "Insulin response affecting energy levels",
    "Potential dehydration"
  ],
  "tips": [
    "Monitor blood glucose before and after meals",
    "Consider smaller, more frequent low-carb meals",
    "Increase water intake, especially with meals",
    "Take a short walk after eating to aid glucose metabolism"
  ],
  "urgency": "medium"
}
```

### 🔍 Barcode Scanner API (NEW!)

#### Full Product Analysis
```http
POST /api/v1/barcode/scan
Content-Type: application/json

{
  "barcode": "3017620422003",
  "user_profile": {
    "age": 28,
    "gender": "female",
    "health_conditions": "hypertension",
    "dietary_preference": "low-sodium",
    "allergies": "gluten",
    "health_goals": "heart health"
  }
}
```

**Comprehensive Response:**
```json
{
  "productInfo": {
    "name": "Nutella",
    "brand": "Ferrero",
    "categories": "Spreads, Sweet spreads, Hazelnut spreads",
    "barcode": "3017620422003",
    "openFoodFactsUrl": "https://world.openfoodfacts.org/product/3017620422003",
    "nutritionPer100g": {
      "energyKcal": 539,
      "proteinsG": 6.3,
      "carbohydratesG": 57.5,
      "sugarsG": 56.3,
      "fatG": 30.9,
      "saturatedFatG": 10.6,
      "fiberG": null,
      "saltG": 0.107,
      "sodiumMg": 42.8
    },
    "qualityScores": {
      "nutriscoreGrade": "e",
      "novaGroup": 4,
      "ecoscoreGrade": "d"
    },
    "allergens": ["en:nuts", "en:milk"],
    "additives": ["en:e322", "en:e476"]
  },
  "nutritionalAnalysis": "⚠️ **High Sugar & Fat Content**: This product contains 56.3g sugar per 100g, which is extremely high...",
  "recommendation": "AVOID - Poor nutritional quality. Look for healthier alternatives.",
  "source": "OpenFoodFacts + AI Analysis"
}
```

#### Quick Product Lookup
```http
GET /api/v1/barcode/lookup/3017620422003
```

### 📊 Health Reports API (Enhanced)

#### Generate Personalized Report
```http
POST /api/v1/reports/generate
Content-Type: application/json

{
  "userId": "user123",
  "reportType": "weekly",
  "userProfile": {
    "age": 42,
    "gender": "male",
    "health_conditions": "diabetes, high cholesterol",
    "health_goals": "weight loss, better glucose control"
  },
  "metrics": {
    "nutrition": {
      "avg_calories_per_day": 1850,
      "avg_protein": 95,
      "avg_carbs": 180,
      "avg_fiber": 25
    },
    "activity": {
      "total_steps": 45000,
      "workout_sessions": 4,
      "active_minutes": 210
    },
    "sleep": {
      "avg_sleep_hours": 7.2,
      "sleep_quality_score": 8.1
    },
    "glucose": {
      "avg_morning_glucose": 125,
      "glucose_variability": "moderate"
    }
  }
}
```

## 🧪 Testing & Validation

### Comprehensive Test Suite Available:

1. **📝 Test Cases Document**: `TEST_CASES.md`
   - Detailed cURL commands for all endpoints
   - Success and error scenarios
   - Sample data and expected responses

2. **📮 Postman Collection**: `Health_AI_Services.postman_collection.json`
   - Pre-configured requests for all endpoints
   - Environment variables setup
   - Test scripts for validation

3. **🔧 Shell Script**: `test_health_services.sh`
   - Automated testing script
   - All services validation
   - Easy one-command testing

### Running Tests:
```bash
# Run automated test script
chmod +x test_health_services.sh
./test_health_services.sh

# Or run individual service tests
./mvnw test
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

# External API Configuration
openfoodfacts.api.base-url=https://world.openfoodfacts.org/api/v2/product/
openfoodfacts.user-agent=HealthAI-Spring-Service/1.0
```

### AI Model Configuration
- **Food Analysis**: GPT-4.1 Mini (enhanced prompts, cost-effective)
- **Health Coach**: O4 Mini (high reasoning effort, empathetic responses)
- **Report Generation**: GPT-4o (comprehensive analysis)
- **Barcode Analysis**: GPT-4.1 Mini (structured product analysis)

## 🔍 Health Check Endpoints

Each service provides a health check endpoint:

- `GET /api/v1/food/health`
- `GET /api/v1/coach/health` 
- `GET /api/v1/reports/health`
- `GET /api/v1/barcode/health` 🆕

## 🏃‍♂️ Running Tests

```bash
# Run all tests
./mvnw test

# Run with coverage
./mvnw test jacoco:report

# Run integration tests for external APIs
./mvnw test -Dtest=BarcodeAnalysisServiceTest
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
- **User Profile Privacy**: Ensure user health data is handled securely
- **External API Security**: OpenFoodFacts integration uses proper User-Agent headers

## 📊 Cost Optimization

### Model Selection Strategy:
- **GPT-4.1 Mini**: Food analysis and barcode scanning (cost-effective, sufficient accuracy)
- **O4 Mini**: Health coaching (reasoning capabilities, conversation optimization)
- **GPT-4o**: Complex report generation (comprehensive analysis required)
- **Temperature Settings**: Optimized per use case (0.1-0.6)

### Estimated Costs (per 1000 requests):
- **Health Coach**: ~$0.075 (O4 Mini, optimized prompts)
- **Food Analysis**: ~$3.50 (GPT-4.1 Mini, vision + user profile)
- **Barcode Analysis**: ~$1.20 (GPT-4.1 Mini, structured analysis)
- **Report Generation**: ~$25 (GPT-4o, comprehensive reports)

## 🚧 Development Notes

### 🆕 Key Enhancement Areas:

1. **User Profile Integration**: All services now accept and utilize user health profiles
2. **Enhanced AI Prompts**: Professional-grade prompts with safety protocols and evidence-based guidelines
3. **External API Integration**: OpenFoodFacts integration for real-world product data
4. **Comprehensive Testing**: Full test suite with multiple testing approaches
5. **Personalization Engine**: AI-driven personalization based on health conditions and goals

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
| **External APIs** | `httpx/requests` | `RestTemplate/WebClient` |

## 📖 Additional Documentation

- **📋 API Guide**: `BARCODE_API_GUIDE.md` - Comprehensive barcode API documentation
- **🧪 Test Cases**: `TEST_CASES.md` - Detailed testing scenarios and examples
- **📮 Postman**: `Health_AI_Services.postman_collection.json` - Ready-to-use API collection

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

3. **OpenFoodFacts API Issues**: 
   - Check internet connectivity
   - Verify barcode format (8-14 digits)
   - Some products may not be in the database

4. **File Upload Issues**: Check file size limits in application.properties

5. **Port Conflicts**: Change server.port in application.properties if 8080 is in use

6. **User Profile Validation Errors**: Ensure required fields (age, gender) are provided

### Getting Help:
- Check application logs: `./mvnw spring-boot:run`
- Enable debug logging: Set `logging.level.health.myvita=DEBUG`
- Test individual endpoints: Use provided test scripts and Postman collection
- Spring AI Documentation: [docs.spring.io/spring-ai](https://docs.spring.io/spring-ai)
- OpenFoodFacts API: [wiki.openfoodfacts.org/API](https://wiki.openfoodfacts.org/API)

---

**🎯 Built with ❤️ using Spring AI 1.0, OpenAI GPT-4, and OpenFoodFacts API**

**✨ Features:** AI-Powered Health Analysis • Personalized Recommendations • Barcode Scanning • Real-time Coaching • Comprehensive Reporting 