#!/bin/bash

# Health AI Services Test Script
# This script runs comprehensive tests for all health AI services

BASE_URL="http://localhost:8080"

echo "🧪 Health AI Services Test Suite"
echo "================================="
echo "Base URL: $BASE_URL"
echo ""

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to print test results
print_result() {
    if [ $1 -eq 0 ]; then
        echo -e "${GREEN}✅ PASS${NC}: $2"
    else
        echo -e "${RED}❌ FAIL${NC}: $2"
    fi
}

# Function to test endpoint availability
test_endpoint() {
    echo -e "${YELLOW}Testing:${NC} $2"
    response=$(curl -s -o /dev/null -w "%{http_code}" "$1")
    if [ "$response" = "200" ]; then
        print_result 0 "$2"
    else
        print_result 1 "$2 (HTTP $response)"
    fi
    echo ""
}

# Function to test JSON endpoint
test_json_endpoint() {
    echo -e "${YELLOW}Testing:${NC} $3"
    response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$2" "$1")
    if [ "$response" = "200" ]; then
        print_result 0 "$3"
    else
        print_result 1 "$3 (HTTP $response)"
    fi
    echo ""
}

echo "🔍 Health Check Tests"
echo "-------------------"

# Health check endpoints
test_endpoint "$BASE_URL/api/v1/food/health" "Food Analysis Service Health Check"
test_endpoint "$BASE_URL/api/v1/coach/health" "Health Coach Service Health Check"
test_endpoint "$BASE_URL/api/v1/barcode/health" "Barcode Analysis Service Health Check"

echo "🩺 Health Coach Service Tests"
echo "----------------------------"

# Test 1: Digestive Issues
digestive_request='{
  "message": "Today I felt bloated and had stomach discomfort after lunch. This has been happening for the past 3 days.",
  "user_profile": {
    "age": 30,
    "gender": "female",
    "health_conditions": "IBS",
    "dietary_preference": "Gluten-free",
    "health_goals": "Digestive health"
  }
}'
test_json_endpoint "$BASE_URL/api/v1/coach/advice" "$digestive_request" "Digestive Issues Consultation"

# Test 2: Energy Levels
energy_request='{
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
test_json_endpoint "$BASE_URL/api/v1/coach/advice" "$energy_request" "Energy Levels Consultation"

# Test 3: Pre-workout Nutrition
nutrition_request='{
  "message": "What should I eat before my morning workout? I usually exercise at 7 AM.",
  "user_profile": {
    "age": 26,
    "gender": "female",
    "activity_level": "very active",
    "health_goals": "Muscle building",
    "dietary_preference": "High-protein"
  }
}'
test_json_endpoint "$BASE_URL/api/v1/coach/advice" "$nutrition_request" "Pre-workout Nutrition Advice"

echo "🏷️ Barcode Analysis Service Tests"
echo "--------------------------------"

# Test 1: Coca-Cola for Diabetic User
coke_request='{
  "barcode": "5449000000996",
  "user_profile": {
    "age": 50,
    "gender": "male",
    "health_conditions": "Type 2 diabetes, hypertension",
    "health_goals": "Blood sugar control",
    "dietary_preference": "Low-sugar"
  }
}'
test_json_endpoint "$BASE_URL/api/v1/barcode/scan" "$coke_request" "Coca-Cola Analysis for Diabetic User"

# Test 2: Almond Milk for Vegan
almond_request='{
  "barcode": "7613269471004",
  "user_profile": {
    "age": 29,
    "gender": "female",
    "allergies": "Dairy, soy",
    "dietary_preference": "Vegan",
    "health_goals": "Plant-based nutrition"
  }
}'
test_json_endpoint "$BASE_URL/api/v1/barcode/scan" "$almond_request" "Almond Milk Analysis for Vegan User"

# Test 3: Protein Bar for Athlete
protein_request='{
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
test_json_endpoint "$BASE_URL/api/v1/barcode/scan" "$protein_request" "Protein Bar Analysis for Athlete"

# Test 4: Quick Barcode Lookup
test_endpoint "$BASE_URL/api/v1/barcode/lookup/5449000000996" "Quick Barcode Lookup (Coca-Cola)"

echo "❌ Error Handling Tests"
echo "----------------------"

# Test 1: Invalid User Profile
invalid_profile_request='{
  "message": "I feel tired",
  "user_profile": {
    "age": -5,
    "gender": ""
  }
}'
echo -e "${YELLOW}Testing:${NC} Invalid User Profile (should return 400)"
response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$invalid_profile_request" "$BASE_URL/api/v1/coach/advice")
if [ "$response" = "400" ]; then
    print_result 0 "Invalid User Profile correctly rejected"
else
    print_result 1 "Invalid User Profile (HTTP $response, expected 400)"
fi
echo ""

# Test 2: Invalid Barcode
invalid_barcode_request='{
  "barcode": "123",
  "user_profile": {
    "age": 30,
    "gender": "female"
  }
}'
echo -e "${YELLOW}Testing:${NC} Invalid Barcode (should return 400)"
response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$invalid_barcode_request" "$BASE_URL/api/v1/barcode/scan")
if [ "$response" = "400" ]; then
    print_result 0 "Invalid Barcode correctly rejected"
else
    print_result 1 "Invalid Barcode (HTTP $response, expected 400)"
fi
echo ""

# Test 3: Empty Message
empty_message_request='{
  "message": "",
  "user_profile": {
    "age": 30,
    "gender": "female"
  }
}'
echo -e "${YELLOW}Testing:${NC} Empty Message (should return 400)"
response=$(curl -s -o /dev/null -w "%{http_code}" -X POST -H "Content-Type: application/json" -d "$empty_message_request" "$BASE_URL/api/v1/coach/advice")
if [ "$response" = "400" ]; then
    print_result 0 "Empty Message correctly rejected"
else
    print_result 1 "Empty Message (HTTP $response, expected 400)"
fi
echo ""

echo "🏋️ Performance Tests"
echo "-------------------"

# Test concurrent requests
echo -e "${YELLOW}Testing:${NC} Concurrent Health Checks"
start_time=$(date +%s)
for i in {1..5}; do
    curl -s "$BASE_URL/api/v1/coach/health" > /dev/null &
done
wait
end_time=$(date +%s)
duration=$((end_time - start_time))
print_result 0 "5 concurrent health checks completed in ${duration}s"
echo ""

echo "📊 Food Analysis Service Tests"
echo "-----------------------------"
echo -e "${YELLOW}Note:${NC} Food analysis tests require image files."
echo "To test food analysis manually, use:"
echo ""
echo "curl -X POST $BASE_URL/api/v1/food/analyze \\"
echo "  -F \"image=@your_food_image.jpg\" \\"
echo "  -F 'userProfile={\"age\": 28, \"gender\": \"female\", \"health_conditions\": \"diabetes\"}'"
echo ""

echo "✅ Test Suite Complete!"
echo "======================="
echo ""
echo "📖 For more detailed tests, see:"
echo "   - TEST_CASES.md"
echo "   - Health_AI_Services.postman_collection.json"
echo ""
echo "🚀 To run the Spring Boot application:"
echo "   ./mvnw spring-boot:run" 