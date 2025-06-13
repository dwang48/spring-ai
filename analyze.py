# analyze_food.py
import os
from openai import OpenAI
from typing import Dict
from dotenv import load_dotenv
load_dotenv()

# Print the API key for debugging
print(f"API Key: {os.getenv('OPENAI_API_KEY')}")

# Initialize the OpenAI client
client = OpenAI(api_key=os.getenv("OPENAI_API_KEY"))

def load_prompt_template(template_name: str) -> str:
    """Load the prompt template from the file."""
    template_path = os.path.join(os.path.dirname(__file__), f'{template_name}_prompt_template.txt')
    with open(template_path, 'r') as file:
        return file.read()

def generate_prompt(template_name: str, user_info: Dict, food_info: Dict) -> str:
    """Populate the prompt template with user and food data."""
    prompt = load_prompt_template(template_name)
    
    # Extract nested data
    nutrition = food_info.get('nutrition', {})
    serving = food_info.get('serving', {})
    nutrition_scores = food_info.get('nutrition_scores', {})
    dietary_info = food_info.get('dietary_info', {})
    environmental_impact = food_info.get('environmental_impact', {})
    
    prompt = prompt.format(
        # User info
        age=user_info.get('age', 'Not specified'),
        gender=user_info.get('gender', 'Not specified'),
        weight=user_info.get('weight', 'Not specified'),
        height=user_info.get('height', 'Not specified'),
        health_conditions=user_info.get('health_conditions', 'None'),
        dietary_preference=user_info.get('dietary_preference', 'No specific preference'),
        allergies=user_info.get('allergies', 'None'),
        health_goals=user_info.get('health_goals', 'maintain health'),
        
        # Food info
        name=food_info.get('name', 'Unknown'),
        brand=food_info.get('brand', 'Unknown'),
        
        # Serving info
        serving=serving,
        
        # Nutrition info
        nutrition=nutrition,
        
        # Scores
        nutrition_scores=nutrition_scores,
        
        # Dietary info
        dietary_info=dietary_info,
        
        # Environmental impact
        environmental_impact=environmental_impact
    )
    return prompt

def get_simple_nutrition_analysis(user_info: Dict, food_info: Dict, client) -> str:
    """Generate a simple nutrition analysis using OpenAI's API."""
    try:
        prompt = generate_prompt("simple", user_info, food_info)
        print(f"Generated prompt: {prompt}")  # Debug print
        
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "You are a nutritionist AI assistant."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=300,
            temperature=0.7,
        )
        return response.choices[0].message.content.strip()
    except Exception as e:
        print(f"Error in get_simple_nutrition_analysis: {str(e)}")
        return f"An error occurred while generating the analysis: {str(e)}"

def get_detailed_nutrition_analysis(user_info: Dict, food_info: Dict, client) -> str:
    """Generate a detailed nutrition analysis using OpenAI's API."""
    try:
        prompt = generate_prompt("detailed", user_info, food_info)
        print(f"Generated prompt: {prompt}")  # Debug print
        
        response = client.chat.completions.create(
            model="gpt-4o-mini",
            messages=[
                {"role": "system", "content": "You are a nutritionist AI assistant."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=400,
            temperature=0.7,
        )
        return response.choices[0].message.content.strip()
    except Exception as e:
        print(f"Error in get_detailed_nutrition_analysis: {str(e)}")
        return f"An error occurred while generating the analysis: {str(e)}"
