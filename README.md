# Spring AI Health Application

**Language / 语言:** [English](#english) | [中文](#中文)

---

## English

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

---

## 中文

# Spring AI 健康应用

一个基于 **Spring Boot 3** 和 **Spring AI 1.0** 构建的综合性 **AI 驱动健康分析平台**，提供四个主要的 AI 健康服务，支持个性化用户档案集成和条形码扫描功能。

## 🏗️ 架构概览

本应用采用模块化的 Spring Boot 架构，集成 Spring AI：

```
src/main/java/health/myvita/spring_ai_demo/spring_ai/
├── controller/          # REST API 控制器  
│   ├── FoodAnalysisController.java      # 食物图像分析
│   ├── HealthCoachController.java       # 健康指导对话
│   ├── HealthReportController.java      # 健康报告生成
│   └── BarcodeController.java           # 🆕 条形码扫描与分析
├── service/             # 业务逻辑服务
│   ├── FoodAnalysisService.java         # 增强用户档案支持
│   ├── HealthCoachService.java          # 增强个性化功能
│   ├── HealthReportService.java         # 增强用户上下文
│   └── BarcodeAnalysisService.java      # 🆕 OpenFoodFacts + AI 集成
├── dto/                 # 数据传输对象
│   ├── FoodItemDto.java
│   ├── NutritionAnalysisResponse.java
│   ├── HealthCoachResponse.java
│   ├── HealthReportRequest.java
│   ├── UserProfileDto.java              # 🆕 用户健康档案
│   └── BarcodeAnalysisResponse.java     # 🆕 条形码扫描结果
└── config/              # 配置类
    └── AiConfiguration.java
```

## 🚀 功能特性

### 1. **🍎 食物图像分析器** (GPT-4 Vision)
- **接口**: `POST /api/v1/food/analyze`
- **技术**: GPT-4.1 Mini 配合增强提示词
- **🆕 增强功能**:
  - **个性化分析**: 考虑用户的健康状况、过敏史和目标
  - **USDA 标准**: 使用专业营养师级别的分析
  - **置信度评分**: 为每个食物识别提供准确性置信度
  - **用户档案集成**: 基于个人健康档案的定制化洞察
- **输入**: 多部分表单，包含图像文件 + 用户档案 JSON
- **输出**: 全面的营养分析和个性化建议

### 2. **🧠 健康教练** (O4 Mini)
- **接口**: `POST /api/v1/coach/advice`
- **技术**: O4 Mini 配合高推理能力
- **🆕 增强功能**:
  - **共情 AI**: 使用积极心理学原理
  - **安全协议**: 内置医疗边界意识
  - **个性化指导**: 根据用户健康档案调整建议
  - **结构化紧急程度**: 清晰的紧急级别（无/低/中/高）
- **输入**: 用户症状 + 全面健康档案
- **输出**: 结构化建议，包含原因、提示和安全建议

### 3. **📊 健康报告生成器** (GPT-4o)
- **接口**: 
  - `POST /api/v1/reports/generate` (单个报告)
  - `POST /api/v1/reports/batch` (批量处理)
- **技术**: GPT-4o 配合全面分析提示词
- **🆕 增强功能**:
  - **行为改变焦点**: 使用 SMART 目标和积极心理学
  - **趋势分析**: 识别健康数据中的模式和关联
  - **个性化上下文**: 考虑年龄、生活方式和健康目标
  - **专业级别**: 基于证据的洞察和科学背景
- **特性**: 异步处理、批量支持、Markdown 格式报告

### 4. **🔍 条形码扫描与分析器** (全新!)
- **接口**:
  - `POST /api/v1/barcode/scan` (完整分析配合用户档案)
  - `GET /api/v1/barcode/lookup/{barcode}` (基础产品查询)
- **技术**: OpenFoodFacts API + GPT-4.1 Mini 分析
- **核心功能**:
  - **实时产品数据**: 集成 OpenFoodFacts 数据库
  - **质量评分**: Nutri-Score、NOVA 和 Eco-Score 集成
  - **个性化建议**: 基于健康状况的定制化建议
  - **过敏警告**: 自动过敏原检测和警告
  - **分量建议**: 定制化份量建议
- **输入**: 条形码 + 用户健康档案
- **输出**: 完整的产品分析和个性化营养洞察

## 🧑‍⚕️ 用户档案系统 (全新!)

所有服务现在都支持全面的用户档案，提供个性化健康洞察：

```json
{
  "age": 30,
  "gender": "female",
  "weight": 65.0,
  "height": 165.0,
  "health_conditions": "糖尿病, 高血压",
  "dietary_preference": "素食主义者",
  "allergies": "坚果, 贝类",
  "health_goals": "减重, 改善睡眠",
  "activity_level": "中等"
}
```

**个性化优势**:
- **精准建议**: 针对特定疾病和目标的健康建议
- **安全第一**: 在所有分析中考虑过敏史和健康状况
- **生活方式背景**: 根据活动水平和偏好调整建议
- **进度跟踪**: 与个人健康目标保持一致的建议

## 🛠️ 技术栈

- **Java**: 21 (LTS)
- **Spring Boot**: 3.x
- **Spring AI**: 1.0.0
- **Maven**: 构建管理
- **OpenAI**: GPT-4o, GPT-4.1 Mini, O4 Mini
- **🆕 OpenFoodFacts API**: 产品数据库集成
- **Jackson**: JSON 处理
- **RestTemplate**: 外部 API HTTP 客户端

## 📋 前置要求

- **Java 21** 或更高版本
- **Maven 3.6+**
- **OpenAI API 密钥**
- 互联网连接（用于 OpenFoodFacts API）

## 🔧 安装说明

### 1. 克隆并导航
```bash
git clone <repository-url>
cd spring-ai-health-app
```

### 2. 配置环境
设置 OpenAI API 密钥作为环境变量：

```bash
# macOS/Linux
export OPENAI_API_KEY="your-openai-api-key-here"

# Windows
set OPENAI_API_KEY=your-openai-api-key-here
```

### 3. 构建项目
```bash
./mvnw clean compile
```

### 4. 运行应用
```bash
./mvnw spring-boot:run
```

应用将在 `http://localhost:8080` 启动

## 📚 API 文档

### 🍎 食物分析 API (增强版)

#### 使用用户档案分析食物图像
```http
POST /api/v1/food/analyze
Content-Type: multipart/form-data

参数:
- image: 图像文件 (JPG/PNG, 最大 5MB)
- userProfile: 包含用户健康档案的 JSON 字符串
```

**cURL 示例:**
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@food_image.jpg" \
  -F 'userProfile={
    "age": 30,
    "gender": "female",
    "weight": 65.0,
    "height": 165.0,
    "health_conditions": "糖尿病",
    "dietary_preference": "素食主义者",
    "allergies": "坚果",
    "health_goals": "减重",
    "activity_level": "中等"
  }'
```

**增强响应:**
```json
{
  "foodItems": [
    {
      "name": "烤鸡胸肉",
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
    "healthAlignment": "减重目标的优秀蛋白质来源",
    "allergenWarnings": "未检测到已知过敏原",
    "portionRecommendation": "适合您目标的合适分量",
    "nutritionalHighlights": ["高蛋白", "低碳水", "适合糖尿病管理"]
  }
}
```

### 🧠 健康教练 API (增强版)

#### 获取个性化健康建议
```http
POST /api/v1/coach/advice
Content-Type: application/json

{
  "message": "我午饭后一直感到疲倦和头痛",
  "userProfile": {
    "age": 35,
    "gender": "male",
    "health_conditions": "糖尿病",
    "dietary_preference": "低碳水",
    "health_goals": "提高精力水平"
  }
}
```

**增强响应:**
```json
{
  "summary": "糖尿病患者餐后疲劳和头痛",
  "possibleCauses": [
    "餐后血糖波动",
    "胰岛素反应影响能量水平",
    "可能脱水"
  ],
  "tips": [
    "监测餐前餐后血糖",
    "考虑少量多餐的低碳水饮食",
    "增加水分摄入，特别是用餐时",
    "餐后短距离散步有助于葡萄糖代谢"
  ],
  "urgency": "medium"
}
```

### 🔍 条形码扫描 API (全新!)

#### 完整产品分析
```http
POST /api/v1/barcode/scan
Content-Type: application/json

{
  "barcode": "3017620422003",
  "user_profile": {
    "age": 28,
    "gender": "female",
    "health_conditions": "高血压",
    "dietary_preference": "低钠",
    "allergies": "麸质",
    "health_goals": "心脏健康"
  }
}
```

**全面响应:**
```json
{
  "productInfo": {
    "name": "能多益",
    "brand": "费列罗",
    "categories": "涂抹酱, 甜味涂抹酱, 榛子涂抹酱",
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
  "nutritionalAnalysis": "⚠️ **高糖高脂含量**: 该产品每100g含糖56.3g，含量极高...",
  "recommendation": "避免 - 营养质量差。寻找更健康的替代品。",
  "source": "OpenFoodFacts + AI 分析"
}
```

#### 快速产品查询
```http
GET /api/v1/barcode/lookup/3017620422003
```

### 📊 健康报告 API (增强版)

#### 生成个性化报告
```http
POST /api/v1/reports/generate
Content-Type: application/json

{
  "userId": "user123",
  "reportType": "weekly",
  "userProfile": {
    "age": 42,
    "gender": "male",
    "health_conditions": "糖尿病, 高胆固醇",
    "health_goals": "减重, 更好的血糖控制"
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
      "glucose_variability": "中等"
    }
  }
}
```

## 🧪 测试与验证

### 可用的全面测试套件:

1. **📝 测试用例文档**: `TEST_CASES.md`
   - 所有接口的详细 cURL 命令
   - 成功和错误场景
   - 示例数据和预期响应

2. **📮 Postman 集合**: `Health_AI_Services.postman_collection.json`
   - 所有接口的预配置请求
   - 环境变量设置
   - 验证测试脚本

3. **🔧 Shell 脚本**: `test_health_services.sh`
   - 自动化测试脚本
   - 所有服务验证
   - 一键测试

### 运行测试:
```bash
# 运行自动化测试脚本
chmod +x test_health_services.sh
./test_health_services.sh

# 或运行单独的服务测试
./mvnw test
```

## ⚙️ 配置

### 应用属性
```properties
# OpenAI 配置
spring.ai.openai.api-key=${OPENAI_API_KEY}
spring.ai.openai.chat.options.model=gpt-4o-mini
spring.ai.openai.chat.options.temperature=0.2

# 服务器配置  
server.port=8080

# 文件上传限制
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB

# 外部 API 配置
openfoodfacts.api.base-url=https://world.openfoodfacts.org/api/v2/product/
openfoodfacts.user-agent=HealthAI-Spring-Service/1.0
```

### AI 模型配置
- **食物分析**: GPT-4.1 Mini (增强提示词，成本效益高)
- **健康教练**: O4 Mini (高推理能力，共情响应)
- **报告生成**: GPT-4o (全面分析)
- **条形码分析**: GPT-4.1 Mini (结构化产品分析)

## 🔍 健康检查接口

每个服务都提供健康检查接口：

- `GET /api/v1/food/health`
- `GET /api/v1/coach/health` 
- `GET /api/v1/reports/health`
- `GET /api/v1/barcode/health` 🆕

## 🏃‍♂️ 运行测试

```bash
# 运行所有测试
./mvnw test

# 运行覆盖率测试
./mvnw test jacoco:report

# 运行外部 API 集成测试
./mvnw test -Dtest=BarcodeAnalysisServiceTest
```

## 🐳 Docker 支持

```dockerfile
FROM openjdk:21-jdk-slim
COPY target/spring-ai-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

```bash
# 使用 Docker 构建和运行
./mvnw clean package
docker build -t spring-ai-health .
docker run -p 8080:8080 -e OPENAI_API_KEY=your-key spring-ai-health
```

## 🔐 安全考虑

- **API 密钥管理**: 使用环境变量安全存储 OpenAI API 密钥
- **输入验证**: 所有接口都包含全面的输入验证
- **文件上传安全**: 图像上传受类型和大小限制
- **速率限制**: 生产环境建议实施速率限制
- **用户档案隐私**: 确保用户健康数据的安全处理
- **外部 API 安全**: OpenFoodFacts 集成使用适当的 User-Agent 头

## 📊 成本优化

### 模型选择策略:
- **GPT-4.1 Mini**: 食物分析和条形码扫描（成本效益高，准确性足够）
- **O4 Mini**: 健康指导（推理能力，对话优化）
- **GPT-4o**: 复杂报告生成（需要全面分析）
- **温度设置**: 根据用例优化（0.1-0.6）

### 预估成本（每1000次请求）:
- **健康教练**: ~$0.075 (O4 Mini，优化提示词)
- **食物分析**: ~$3.50 (GPT-4.1 Mini，视觉 + 用户档案)
- **条形码分析**: ~$1.20 (GPT-4.1 Mini，结构化分析)
- **报告生成**: ~$25 (GPT-4o，全面报告)

## 🚧 开发说明

### 🆕 关键增强领域:

1. **用户档案集成**: 所有服务现在都接受并利用用户健康档案
2. **增强 AI 提示词**: 专业级提示词，包含安全协议和循证指南
3. **外部 API 集成**: OpenFoodFacts 集成，提供真实世界产品数据
4. **全面测试**: 多种测试方法的完整测试套件
5. **个性化引擎**: 基于健康状况和目标的 AI 驱动个性化

### 从 Django/FastAPI 到 Spring Boot

熟悉 Python 框架的开发者的关键差异：

| 方面 | Django/FastAPI | Spring Boot |
|------|----------------|-------------|
| **路由** | `@app.post("/api/...")` | `@PostMapping("/api/...")` |
| **依赖注入** | Manual/FastAPI Depends | `@Autowired` / 构造器注入 |
| **配置** | `settings.py` / `.env` | `application.properties` |
| **数据模型** | Pydantic/Django Models | POJOs with getters/setters |
| **JSON 处理** | 内置 | Jackson 注解 |
| **异步支持** | `async def` | `CompletableFuture<T>` |
| **外部 APIs** | `httpx/requests` | `RestTemplate/WebClient` |

## 📖 附加文档

- **📋 API 指南**: `BARCODE_API_GUIDE.md` - 全面的条形码 API 文档
- **🧪 测试用例**: `TEST_CASES.md` - 详细的测试场景和示例
- **📮 Postman**: `Health_AI_Services.postman_collection.json` - 即用型 API 集合

## 🤝 贡献

1. Fork 仓库
2. 创建功能分支 (`git checkout -b feature/amazing-feature`)
3. 提交更改 (`git commit -m 'Add amazing feature'`)
4. 推送到分支 (`git push origin feature/amazing-feature`)
5. 打开 Pull Request

## 📝 许可证

本项目采用 MIT 许可证 - 详见 [LICENSE](LICENSE) 文件。

## 🆘 故障排除

### 常见问题:

1. **编译错误**: 确保使用 Java 21
   ```bash
   java -version  # 应显示 Java 21
   export JAVA_HOME=/path/to/java21
   ```

2. **OpenAI API 错误**: 验证 API 密钥设置正确
   ```bash
   echo $OPENAI_API_KEY  # 应显示您的密钥
   ```

3. **OpenFoodFacts API 问题**: 
   - 检查网络连接
   - 验证条形码格式（8-14位数字）
   - 某些产品可能不在数据库中

4. **文件上传问题**: 检查 application.properties 中的文件大小限制

5. **端口冲突**: 如果 8080 端口被占用，在 application.properties 中更改 server.port

6. **用户档案验证错误**: 确保提供必需字段（年龄、性别）

### 获取帮助:
- 检查应用日志: `./mvnw spring-boot:run`
- 启用调试日志: 设置 `logging.level.health.myvita=DEBUG`
- 测试单独接口: 使用提供的测试脚本和 Postman 集合
- Spring AI 文档: [docs.spring.io/spring-ai](https://docs.spring.io/spring-ai)
- OpenFoodFacts API: [wiki.openfoodfacts.org/API](https://wiki.openfoodfacts.org/API)

---

**🎯 使用 Spring AI 1.0、OpenAI GPT-4 和 OpenFoodFacts API 精心构建 ❤️**

**✨ 特性:** AI 驱动健康分析 • 个性化建议 • 条形码扫描 • 实时指导 • 全面报告
</rewritten_file>