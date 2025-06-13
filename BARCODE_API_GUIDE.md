# Barcode Scanning API Guide

## Overview

The barcode scanning feature allows users to scan packaged food products and receive personalized nutritional analysis based on their health profile. The system fetches product data from OpenFoodFacts and provides AI-powered recommendations.

## Endpoints

### 1. Full Barcode Analysis with User Profile

**POST** `/api/v1/barcode/scan`

Analyzes a product by barcode and provides personalized recommendations based on user's health profile.

#### Request Body

```json
{
  "barcode": "3017620422003",
  "user_profile": {
    "age": 30,
    "gender": "female",
    "weight": 65.0,
    "height": 165.0,
    "health_conditions": "diabetes",
    "dietary_preference": "vegetarian",
    "allergies": "nuts",
    "health_goals": "weight loss",
    "activity_level": "moderate"
  }
}
```

#### Response

```json
{
  "product_info": {
    "name": "Nutella",
    "brand": "Ferrero",
    "categories": "Spreads,Sweet spreads,Cocoa and hazelnuts spreads",
    "ingredients_text": "Sugar, palm oil, hazelnuts...",
    "barcode": "3017620422003",
    "nutrition_per_100g": {
      "energy_kcal": 539,
      "proteins_g": 6.3,
      "carbohydrates_g": 57.5,
      "sugars_g": 56.3,
      "fat_g": 30.9,
      "saturated_fat_g": 10.6,
      "fiber_g": 0,
      "salt_g": 0.107,
      "sodium_mg": 42.8
    },
    "quality_scores": {
      "nutriscore_grade": "e",
      "nova_group": 4,
      "ecoscore_grade": "d"
    },
    "additives": ["en:e322", "en:e476"],
    "allergens": ["en:milk", "en:nuts"],
    "labels": ["en:no-gluten"],
    "openfoodfacts_url": "https://world.openfoodfacts.org/product/3017620422003"
  },
  "nutritional_analysis": "## 📊 Nutritional Overview\nThis product is very high in sugar (56.3g per 100g) and calories (539 kcal per 100g)...",
  "recommendation": "AVOID - Poor nutritional quality. Look for healthier alternatives.",
  "source": "OpenFoodFacts + AI Analysis"
}
```

### 2. Quick Barcode Lookup

**GET** `/api/v1/barcode/lookup/{barcode}`

Quick product lookup without personalized analysis.

#### Example

```bash
curl -X GET "http://localhost:8080/api/v1/barcode/lookup/3017620422003"
```

### 3. Health Check

**GET** `/api/v1/barcode/health`

Returns service status.

## User Profile Fields

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `age` | Integer | Yes | User's age in years |
| `gender` | String | Yes | User's gender (male/female/other) |
| `weight` | Double | No | User's weight in kg |
| `height` | Double | No | User's height in cm |
| `health_conditions` | String | No | Any health conditions (diabetes, hypertension, etc.) |
| `dietary_preference` | String | No | Dietary preferences (vegetarian, vegan, etc.) |
| `allergies` | String | No | Known allergies |
| `health_goals` | String | No | Health goals (weight loss, muscle gain, etc.) |
| `activity_level` | String | No | Physical activity level (low, moderate, high) |

## Example Usage

### cURL Example

```bash
curl -X POST "http://localhost:8080/api/v1/barcode/scan" \
  -H "Content-Type: application/json" \
  -d '{
    "barcode": "3017620422003",
    "user_profile": {
      "age": 25,
      "gender": "male",
      "weight": 70.0,
      "height": 175.0,
      "health_conditions": "None",
      "dietary_preference": "No specific preference",
      "allergies": "None",
      "health_goals": "maintain health",
      "activity_level": "moderate"
    }
  }'
```

### JavaScript/Fetch Example

```javascript
const barcodeData = {
  barcode: "3017620422003",
  user_profile: {
    age: 25,
    gender: "male",
    weight: 70.0,
    height: 175.0,
    health_conditions: "None",
    dietary_preference: "No specific preference",
    allergies: "None",
    health_goals: "maintain health",
    activity_level: "moderate"
  }
};

fetch('http://localhost:8080/api/v1/barcode/scan', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
  },
  body: JSON.stringify(barcodeData)
})
.then(response => response.json())
.then(data => console.log('Analysis:', data))
.catch(error => console.error('Error:', error));
```

## Error Handling

The API returns appropriate HTTP status codes:

- `200 OK` - Successful analysis
- `400 Bad Request` - Invalid barcode format or missing required fields
- `500 Internal Server Error` - Server error during processing

## Data Sources

- **Product Information**: OpenFoodFacts API (https://world.openfoodfacts.org/)
- **Nutritional Analysis**: OpenAI GPT-4 Mini
- **Quality Scores**: 
  - Nutri-Score (A-E rating system)
  - NOVA Classification (1-4, food processing level)
  - Eco-Score (A-E, environmental impact)

## Implementation Features

✅ **OpenFoodFacts Integration** - Fetches real product data  
✅ **Personalized Analysis** - AI recommendations based on user profile  
✅ **Quality Scoring** - Nutri-Score, NOVA, and Eco-Score integration  
✅ **Allergen Detection** - Checks for user allergies  
✅ **Health Condition Awareness** - Considers user's health conditions  
✅ **Ingredient Analysis** - Reviews ingredients list  
✅ **Error Handling** - Graceful handling of missing products  

## Mobile App Integration

For mobile apps with barcode scanning:

1. Use camera to scan barcode
2. Extract barcode number from scan result
3. Call `/api/v1/barcode/scan` endpoint
4. Display nutritional analysis and recommendations
5. Allow users to save/track scanned products

## Next Steps

Consider implementing:
- User authentication and profile storage
- Scanning history and favorites
- Offline barcode lookup cache
- Product comparison features
- Nutritional goal tracking 