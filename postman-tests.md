# Spring AI Health API - Postman Test Examples

## 🚀 **Setup Instructions**

### 1. **Environment Variables**
Create a Postman environment with these variables:
- `base_url`: `http://localhost:8080`
- `openai_api_key`: `your_openai_api_key_here`

### 2. **Headers for All Requests**
```
Content-Type: application/json
Accept: application/json
```

---

## 📋 **API Endpoint Tests**

### **1. Health Coach Advice API**

**Endpoint:** `POST {{base_url}}/api/v1/coach/advice`

#### **Test Case 1: Basic Symptom Analysis**
```json
{
  "symptoms": "I have been experiencing headaches and fatigue for the past 3 days. I also feel dizzy when I stand up quickly."
}
```

#### **Test Case 2: Digestive Issues**
```json
{
  "symptoms": "I have stomach pain after eating, bloating, and occasional nausea. This has been happening for about a week."
}
```

#### **Test Case 3: Sleep Problems**
```json
{
  "symptoms": "I can't fall asleep at night, I wake up frequently, and I feel tired during the day despite being in bed for 8 hours."
}
```

#### **Test Case 4: Exercise-Related Symptoms**
```json
{
  "symptoms": "After my workout yesterday, I have muscle soreness in my legs and lower back. I also feel more tired than usual."
}
```

#### **Expected Response Format:**
```json
{
  "summary": "Brief summary of symptoms",
  "possibleCauses": [
    "Possible cause 1",
    "Possible cause 2"
  ],
  "tips": [
    "Tip 1",
    "Tip 2",
    "Tip 3"
  ],
  "urgencyLevel": "low|medium|high"
}
```

---

### **2. Food Analysis API**

**Endpoint:** `POST {{base_url}}/api/v1/food/analyze`

#### **Test Case 1: Upload Food Image**
- **Method:** POST
- **Body Type:** form-data
- **Key:** `image` (File)
- **Value:** Upload an image file (JPG, PNG, etc.)

#### **Test Case 2: Multiple Food Items**
Upload an image containing multiple food items like a full meal plate.

#### **Test Case 3: Single Food Item**
Upload an image of a single food item like an apple or sandwich.

#### **Expected Response Format:**
```json
{
  "foodItems": [
    {
      "name": "Apple",
      "quantity": "1 medium",
      "calories": 95,
      "confidence": 0.95
    }
  ],
  "totalEstimatedCaloriesKcal": 95,
  "macronutrients": {
    "protein": "0.5g",
    "carbohydrates": "25g",
    "fat": "0.3g"
  },
  "micronutrients": {
    "vitaminC": "14% DV",
    "fiber": "4g"
  }
}
```

---

### **3. Health Report Generation API**

**Endpoint:** `POST {{base_url}}/api/v1/reports/generate`

#### **Test Case 1: Weekly Health Report**
```json
{
  "userId": "user123",
  "reportType": "weekly",
  "metrics": {
    "averageCaloriesPerDay": 2200,
    "averageStepsPerDay": 8500,
    "averageSleepHours": 7.5,
    "workoutDays": 4,
    "waterIntakeLiters": 2.5,
    "mood": "good",
    "energyLevel": "high",
    "stressLevel": "medium"
  }
}
```

#### **Test Case 2: Monthly Health Report**
```json
{
  "userId": "user456",
  "reportType": "monthly",
  "metrics": {
    "averageCaloriesPerDay": 2100,
    "averageStepsPerDay": 9200,
    "averageSleepHours": 7.8,
    "workoutDays": 16,
    "waterIntakeLiters": 2.8,
    "mood": "excellent",
    "energyLevel": "high",
    "stressLevel": "low",
    "weightChange": "-2.5kg",
    "bloodPressure": "120/80",
    "heartRate": 65
  }
}
```

#### **Test Case 3: Fitness-Focused Report**
```json
{
  "userId": "athlete789",
  "reportType": "weekly",
  "metrics": {
    "averageCaloriesPerDay": 2800,
    "averageStepsPerDay": 12000,
    "averageSleepHours": 8.2,
    "workoutDays": 6,
    "waterIntakeLiters": 3.5,
    "mood": "good",
    "energyLevel": "very high",
    "stressLevel": "low",
    "proteinIntake": "150g",
    "workoutDuration": "90 minutes",
    "recoveryTime": "good"
  }
}
```

---

### **4. Batch Health Reports API**

**Endpoint:** `POST {{base_url}}/api/v1/reports/batch`

#### **Test Case 1: Multiple User Reports**
```json
{
  "requests": [
    {
      "userId": "user001",
      "reportType": "weekly",
      "metrics": {
        "averageCaloriesPerDay": 2000,
        "averageStepsPerDay": 7500,
        "averageSleepHours": 7.0,
        "workoutDays": 3,
        "waterIntakeLiters": 2.0,
        "mood": "fair",
        "energyLevel": "medium",
        "stressLevel": "high"
      }
    },
    {
      "userId": "user002",
      "reportType": "weekly",
      "metrics": {
        "averageCaloriesPerDay": 2300,
        "averageStepsPerDay": 9000,
        "averageSleepHours": 8.0,
        "workoutDays": 5,
        "waterIntakeLiters": 2.7,
        "mood": "good",
        "energyLevel": "high",
        "stressLevel": "low"
      }
    }
  ]
}
```

---

## 🧪 **Postman Test Scripts**

### **Pre-request Script (for all requests)**
```javascript
// Set timestamp for request tracking
pm.globals.set("timestamp", new Date().toISOString());

// Log request details
console.log("Making request to: " + pm.request.url);
console.log("Request method: " + pm.request.method);
```

### **Test Script for Health Coach API**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has required fields", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('summary');
    pm.expect(jsonData).to.have.property('possibleCauses');
    pm.expect(jsonData).to.have.property('tips');
    pm.expect(jsonData).to.have.property('urgencyLevel');
});

pm.test("Urgency level is valid", function () {
    const jsonData = pm.response.json();
    const validLevels = ['none', 'low', 'medium', 'high'];
    pm.expect(validLevels).to.include(jsonData.urgencyLevel);
});

pm.test("Response time is less than 30 seconds", function () {
    pm.expect(pm.response.responseTime).to.be.below(30000);
});
```

### **Test Script for Food Analysis API**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response has food analysis data", function () {
    const jsonData = pm.response.json();
    pm.expect(jsonData).to.have.property('foodItems');
    pm.expect(jsonData).to.have.property('totalEstimatedCaloriesKcal');
    pm.expect(jsonData.foodItems).to.be.an('array');
});

pm.test("Food items have required properties", function () {
    const jsonData = pm.response.json();
    if (jsonData.foodItems.length > 0) {
        const firstItem = jsonData.foodItems[0];
        pm.expect(firstItem).to.have.property('name');
        pm.expect(firstItem).to.have.property('calories');
        pm.expect(firstItem).to.have.property('confidence');
    }
});
```

### **Test Script for Health Reports API**
```javascript
pm.test("Status code is 200", function () {
    pm.response.to.have.status(200);
});

pm.test("Response is a valid health report", function () {
    const responseText = pm.response.text();
    pm.expect(responseText).to.include('Health Report');
    pm.expect(responseText.length).to.be.above(100);
});

pm.test("Report contains analysis sections", function () {
    const responseText = pm.response.text();
    pm.expect(responseText).to.include('Summary');
    pm.expect(responseText).to.include('Recommendations');
});
```

---

## 🔧 **Error Testing**

### **Test Invalid Requests**

#### **1. Missing Required Fields**
```json
{
  "invalidField": "test"
}
```
**Expected:** 400 Bad Request

#### **2. Empty Symptoms**
```json
{
  "symptoms": ""
}
```
**Expected:** 400 Bad Request

#### **3. Invalid File Upload**
Upload a non-image file (e.g., .txt file) to food analysis endpoint.
**Expected:** 400 Bad Request

---

## 📊 **Performance Testing**

### **Load Testing Collection**
Create a collection with:
- 10 concurrent requests to health coach API
- 5 concurrent image uploads
- 3 concurrent report generations

### **Response Time Benchmarks**
- Health Coach API: < 15 seconds
- Food Analysis API: < 20 seconds  
- Health Reports API: < 25 seconds
- Batch Reports API: < 60 seconds

---

## 🔐 **Authentication Testing**

### **Test Without API Key**
Remove the OpenAI API key from environment and test endpoints.
**Expected:** 500 Internal Server Error (OpenAI authentication failure)

### **Test With Invalid API Key**
Set `openai_api_key` to an invalid value.
**Expected:** 401 Unauthorized from OpenAI

---

## 📝 **Collection Export**

To import these tests into Postman:
1. Create a new collection named "Spring AI Health API"
2. Add the environment variables
3. Create requests for each endpoint with the test cases above
4. Add the test scripts to validate responses
5. Export and share the collection

---

## 🚀 **Quick Start Commands**

```bash
# Start Spring Boot in background
./mvnw spring-boot:run &

# Check if application is running
curl http://localhost:8080/api/v1/coach/advice

# Stop the application
pkill -f "spring-boot:run"
```

**Your Spring AI Health API is ready for testing!** 🎉 