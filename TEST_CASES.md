# Health AI Services - Test Cases

This document provides comprehensive test cases using cURL and Postman for all health AI services.

## Table of Contents
1. [Prerequisites](#prerequisites)
2. [Food Analysis Service](#food-analysis-service)
3. [Health Coach Service](#health-coach-service)
4. [Health Report Service](#health-report-service)
5. [Barcode Analysis Service](#barcode-analysis-service)
6. [Sample User Profiles](#sample-user-profiles)

## Prerequisites

**Base URL:** `http://localhost:8080`

**Sample User Profile JSON:**
```json
{
  "age": 28,
  "gender": "female",
  "weight": 65.0,
  "height": 165.0,
  "health_conditions": "Pre-diabetes, high blood pressure",
  "dietary_preference": "Mediterranean diet",
  "allergies": "Shellfish, tree nuts",
  "health_goals": "Weight loss, blood sugar control",
  "activity_level": "moderate"
}
```

---

## Food Analysis Service

### Endpoint: POST `/api/v1/food/analyze`

Analyzes food images with personalized nutritional recommendations.

#### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -H "Content-Type: multipart/form-data" \
  -F "image=@/path/to/your/food_image.jpg" \
  -F 'userProfile={
    "age": 28,
    "gender": "female", 
    "weight": 65.0,
    "height": 165.0,
    "health_conditions": "Pre-diabetes",
    "dietary_preference": "Low-carb",
    "allergies": "None",
    "health_goals": "Blood sugar control",
    "activity_level": "moderate"
  }'
```

#### Postman Setup

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/v1/food/analyze`
3. **Headers:** None (auto-set for multipart)
4. **Body:** Select "form-data"
   - Key: `image`, Type: File, Value: Upload image file
   - Key: `userProfile`, Type: Text, Value:
     ```json
     {
       "age": 28,
       "gender": "female",
       "weight": 65.0,
       "height": 165.0,
       "health_conditions": "Pre-diabetes",
       "dietary_preference": "Low-carb",
       "allergies": "None",
       "health_goals": "Blood sugar control",
       "activity_level": "moderate"
     }
     ```

#### Test Cases

**Test Case 1: Pizza Analysis for Diabetic User**
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@pizza.jpg" \
  -F 'userProfile={"age": 45, "gender": "male", "health_conditions": "Type 2 diabetes", "health_goals": "Blood sugar control"}'
```

**Test Case 2: Salad Analysis for Weight Loss**
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@salad.jpg" \
  -F 'userProfile={"age": 32, "gender": "female", "weight": 75.0, "health_goals": "Weight loss", "dietary_preference": "Vegetarian"}'
```

**Test Case 3: Invalid Image Format**
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@document.pdf" \
  -F 'userProfile={"age": 25, "gender": "male"}'
# Expected: 400 Bad Request
```

#### Expected Response Format
```json
{
  "foodItems": [
    {
      "name": "Margherita Pizza",
      "quantity": "2 slices (250g)",
      "calories": 520,
      "confidence": 0.89
    }
  ],
  "totalCalories": 520,
  "macronutrients": {
    "carbohydrates": 58.5,
    "proteins": 22.3,
    "fats": 18.7,
    "fiber": 3.2
  },
  "personalizedInsights": {
    "healthAlignment": "Moderate carbohydrate content may impact blood sugar levels",
    "allergenWarnings": "No known allergens detected",
    "portionRecommendation": "Consider 1 slice with side salad",
    "nutritionalHighlights": "Good protein content from cheese"
  }
}
```

---

## Health Coach Service

### Endpoint: POST `/api/v1/coach/advice`

Provides personalized health coaching based on user symptoms and profile.

#### cURL Examples

**Test Case 1: Digestive Issues**
```bash
curl -X POST http://localhost:8080/api/v1/coach/advice \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Today I felt bloated and had stomach discomfort after lunch. This has been happening for the past 3 days.",
    "user_profile": {
      "age": 30,
      "gender": "female",
      "health_conditions": "IBS",
      "dietary_preference": "Gluten-free",
      "health_goals": "Digestive health"
    }
  }'
```

**Test Case 2: Energy Levels**
```bash
curl -X POST http://localhost:8080/api/v1/coach/advice \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I have been feeling very tired lately, especially in the afternoons. I sleep 7 hours but still feel exhausted.",
    "user_profile": {
      "age": 35,
      "gender": "male",
      "weight": 80.0,
      "height": 175.0,
      "health_conditions": "None",
      "activity_level": "sedentary",
      "health_goals": "Increase energy levels"
    }
  }'
```

**Test Case 3: Pre-workout Nutrition**
```bash
curl -X POST http://localhost:8080/api/v1/coach/advice \
  -H "Content-Type: application/json" \
  -d '{
    "message": "What should I eat before my morning workout? I usually exercise at 7 AM.",
    "user_profile": {
      "age": 26,
      "gender": "female",
      "activity_level": "very active",
      "health_goals": "Muscle building",
      "dietary_preference": "High-protein"
    }
  }'
```

#### Postman Setup

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/v1/coach/advice`
3. **Headers:** 
   - Content-Type: `application/json`
4. **Body:** Select "raw" → "JSON"
   ```json
   {
     "message": "I've been having headaches after meals",
     "user_profile": {
       "age": 28,
       "gender": "female",
       "health_conditions": "Migraine",
       "dietary_preference": "None",
       "allergies": "Caffeine sensitivity"
     }
   }
   ```

#### Expected Response Format
```json
{
  "summary": "You're experiencing post-meal digestive discomfort, which could be related to your IBS condition and dietary choices.",
  "possibleCauses": [
    "High FODMAP foods triggering IBS symptoms",
    "Eating too quickly or large portion sizes",
    "Stress-related digestive sensitivity"
  ],
  "tips": [
    "Try a low-FODMAP elimination diet for 2-3 weeks",
    "Eat smaller, more frequent meals throughout the day",
    "Practice mindful eating and chew food thoroughly"
  ],
  "urgency": "low"
}
```

---

## Health Report Service

*Note: This service would typically be accessed internally for batch processing, but here's how to test it:*

### Endpoint: POST `/api/v1/reports/generate`

#### cURL Example

```bash
curl -X POST http://localhost:8080/api/v1/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user123",
    "reportType": "weekly",
    "metrics": {
      "nutrition": {
        "avgCaloriesPerDay": 1850,
        "vegetableServings": 4.2,
        "proteinIntake": 85.5,
        "fiberIntake": 28.3
      },
      "activity": {
        "totalSteps": 52000,
        "workoutSessions": 4,
        "activeMinutes": 180
      },
      "sleep": {
        "avgDuration": "7h 20m",
        "sleepQuality": 7.2,
        "bedtimeConsistency": 85
      },
      "vitals": {
        "avgHeartRate": 72,
        "bloodPressure": "118/76",
        "weight": 64.8
      }
    },
    "user_profile": {
      "age": 28,
      "gender": "female",
      "weight": 65.0,
      "height": 165.0,
      "health_conditions": "None",
      "health_goals": "Weight maintenance, fitness improvement",
      "activity_level": "active"
    }
  }'
```

#### Test Cases

**Test Case 1: Weight Loss Progress Report**
```bash
curl -X POST http://localhost:8080/api/v1/reports/generate \
  -H "Content-Type: application/json" \
  -d '{
    "userId": "user456",
    "reportType": "monthly",
    "metrics": {
      "nutrition": {
        "avgCaloriesPerDay": 1650,
        "calorieDeficit": 350,
        "macroBalance": {"carbs": 40, "protein": 30, "fat": 30}
      },
      "weight": {
        "startWeight": 75.0,
        "currentWeight": 72.5,
        "weightLoss": 2.5,
        "trend": "decreasing"
      }
    },
    "user_profile": {
      "age": 34,
      "gender": "female",
      "health_goals": "Weight loss - 10kg",
      "dietary_preference": "Mediterranean"
    }
  }'
```

---

## Barcode Analysis Service

### Endpoint: POST `/api/v1/barcode/scan`

Analyzes packaged food products by barcode with personalized recommendations.

#### cURL Examples

**Test Case 1: Coca-Cola for Diabetic User**
```bash
curl -X POST http://localhost:8080/api/v1/barcode/scan \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "5449000000996",
    "user_profile": {
      "age": 50,
      "gender": "male",
      "health_conditions": "Type 2 diabetes, hypertension",
      "health_goals": "Blood sugar control",
      "dietary_preference": "Low-sugar"
    }
  }'
```

**Test Case 2: Organic Almond Milk for Vegan**
```bash
curl -X POST http://localhost:8080/api/v1/barcode/scan \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "7613269471004",
    "user_profile": {
      "age": 29,
      "gender": "female",
      "allergies": "Dairy, soy",
      "dietary_preference": "Vegan",
      "health_goals": "Plant-based nutrition"
    }
  }'
```

**Test Case 3: Protein Bar for Athlete**
```bash
curl -X POST http://localhost:8080/api/v1/barcode/scan \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "722252010926",
    "user_profile": {
      "age": 25,
      "gender": "male",
      "weight": 75.0,
      "height": 180.0,
      "activity_level": "very active",
      "health_goals": "Muscle building",
      "dietary_preference": "High-protein"
    }
  }'
```

**Test Case 4: Invalid Barcode**
```bash
curl -X POST http://localhost:8080/api/v1/barcode/scan \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "123",
    "user_profile": {
      "age": 30,
      "gender": "female"
    }
  }'
# Expected: 400 Bad Request
```

#### Postman Setup

1. **Method:** POST
2. **URL:** `http://localhost:8080/api/v1/barcode/scan`
3. **Headers:** 
   - Content-Type: `application/json`
4. **Body:** Select "raw" → "JSON"
   ```json
   {
     "barcode": "5449000000996",
     "user_profile": {
       "age": 35,
       "gender": "male",
       "health_conditions": "Diabetes",
       "health_goals": "Blood sugar control"
     }
   }
   ```

#### Expected Response Format
```json
{
  "productInfo": {
    "name": "Coca-Cola Classic",
    "brand": "Coca-Cola",
    "barcode": "5449000000996",
    "categories": "Carbonated drinks, Beverages",
    "nutritionPer100g": {
      "energyKcal": 42.0,
      "proteinsG": 0.0,
      "carbohydratesG": 10.6,
      "sugarsG": 10.6,
      "fatG": 0.0,
      "saltG": 0.01
    },
    "qualityScores": {
      "nutriscoreGrade": "e",
      "novaGroup": 4,
      "ecoscoreGrade": "e"
    }
  },
  "nutritionalAnalysis": "This product is high in added sugars and not recommended for diabetes management...",
  "recommendation": "AVOID - Poor nutritional quality for your health goals",
  "source": "OpenFoodFacts + AI Analysis"
}
```

### Endpoint: GET `/api/v1/barcode/lookup/{barcode}`

Quick barcode lookup without user profile.

```bash
curl -X GET http://localhost:8080/api/v1/barcode/lookup/5449000000996
```

---

## Sample User Profiles

### Diabetic User
```json
{
  "age": 55,
  "gender": "male",
  "weight": 85.0,
  "height": 175.0,
  "health_conditions": "Type 2 diabetes, high cholesterol",
  "dietary_preference": "Low-carb",
  "allergies": "None",
  "health_goals": "Blood sugar control, weight loss",
  "activity_level": "light"
}
```

### Athletic Female
```json
{
  "age": 26,
  "gender": "female",
  "weight": 60.0,
  "height": 168.0,
  "health_conditions": "None",
  "dietary_preference": "High-protein",
  "allergies": "Lactose intolerant",
  "health_goals": "Muscle building, performance optimization",
  "activity_level": "very active"
}
```

### Senior with Heart Condition
```json
{
  "age": 68,
  "gender": "female",
  "weight": 70.0,
  "height": 160.0,
  "health_conditions": "Hypertension, heart disease",
  "dietary_preference": "Mediterranean",
  "allergies": "Shellfish",
  "health_goals": "Heart health, blood pressure control",
  "activity_level": "light"
}
```

### Young Professional
```json
{
  "age": 28,
  "gender": "male",
  "weight": 75.0,
  "height": 180.0,
  "health_conditions": "None",
  "dietary_preference": "Balanced",
  "allergies": "Tree nuts",
  "health_goals": "General health, stress management",
  "activity_level": "moderate"
}
```

---

## Health Check Endpoints

Test all services are running:

```bash
# Food Analysis Service
curl -X GET http://localhost:8080/api/v1/food/health

# Health Coach Service  
curl -X GET http://localhost:8080/api/v1/coach/health

# Barcode Analysis Service
curl -X GET http://localhost:8080/api/v1/barcode/health
```

---

## Error Testing

### Invalid User Profile
```bash
curl -X POST http://localhost:8080/api/v1/coach/advice \
  -H "Content-Type: application/json" \
  -d '{
    "message": "I feel tired",
    "user_profile": {
      "age": -5,
      "gender": ""
    }
  }'
# Expected: 400 Bad Request
```

### Missing Required Fields
```bash
curl -X POST http://localhost:8080/api/v1/barcode/scan \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "",
    "user_profile": null
  }'
# Expected: 400 Bad Request
```

### Large File Upload (>5MB)
```bash
curl -X POST http://localhost:8080/api/v1/food/analyze \
  -F "image=@large_image_6mb.jpg" \
  -F 'userProfile={"age": 25, "gender": "male"}'
# Expected: 400 Bad Request
```

---

## Performance Testing

### Load Testing with Apache Bench
```bash
# Test barcode lookup performance
ab -n 100 -c 10 -H "Content-Type: application/json" \
   -p barcode_request.json \
   http://localhost:8080/api/v1/barcode/scan
```

### Concurrent Requests
```bash
# Run multiple requests simultaneously
for i in {1..10}; do
  curl -X GET http://localhost:8080/api/v1/coach/health &
done
wait
```

---

This comprehensive test suite covers all major functionality, edge cases, and error conditions for the health AI services. 