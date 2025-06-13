| **#** | **Feature** | **OpenAI model** | **Endpoint** | **Runs** | **Output format** |
| --- | --- | --- | --- | --- | --- |
| 1 | **Food-in-image analyzer** | gpt-4o-vision-4.1 | /v1/chat/completions | Real-time (≈3-5 s) | JSON (nutrition block) |
| 2 | **“Tell us how you feel” coach** | gpt-o4-mini | /v1/chat/completions | On demand | Markdown / plain text or JSON |
| 3 | **Weekly/Monthly health report** | any GPT-4-class (batch) | /v1/batch (+ /v1/chat/completions) | Weekly cron | PDF / Markdown (embedded JSON) |
| 4 | Barcode | gpt-4.1-mini |  | on demand |plain text+graph  |

# **Setup:**

```markdown
POST https://api.openai.com/v1/chat/completions
Authorization: Bearer $OPENAI_API_KEY
Content-Type: application/json
```

```markdown
{
"model": "gpt-4o-vision-4.1",  // swap per feature
"temperature": 0.2,
"tools": [...],                // optional function-calling
"messages": [...]
}
```

# **1. Food-in-Image Analyze:**

提示词：

```markdown
{
"messages": [
{ "role": "system",
"content": "You are a nutrition analyst. Detect each food in the image, look up USDA calories, and return strict JSON." },    //replace with actual prompt
{ "role": "user",
"content": [
{ "type": "image_url", "image_url": { "url": "<SIGNED_URL>" } },
{ "type": "text", "text": "Return JSON only; schema below." },
{ "type": "text", "text": "{ \"schema\": { ... } }" }
]
}
]
}
```

**Expected response schema:**

可能需要用backend lambda查询USDA API

```markdown
{
"food_items": [
{
"name": "ramen",
"serving_size_g": 500,
"calories_kcal": 650,
"macros": {
"protein_g": 24,
"fat_g": 18,
"carb_g": 90
},
"micros": { "sodium_mg": 2100, "fiber_g": 4 },
"confidence": 0.83
}
],
"total_estimated_calories_kcal": 650,
"sources": ["USDA-FDC:234872"]
}
```

# **2. Daily Symptom Coach (“Tell Us About How You Feel”)日常对话**

这一部分考虑用o4-mini，成本更低，medqa评分更高,接近96:

RAG

https://www.vals.ai/benchmarks/medqa-05-30-2025

```markdown
{
"messages": [
{ "role": "system",
"content": "You are a health-coach assistant; be concise, friendly, evidence-based." },
{ "role": "user",
"content": "Today I felt bloated and had a mild headache after lunch…" }
]
}
```

```markdown
{
"summary": "You likely experienced…",
"possible_causes": [
"High-sodium lunch → water retention",
"Skipped breakfast → low blood sugar"
],
"tips": [
"Drink 2 glasses of water within the next hour",
"Include a protein-rich snack mid-morning"
],
"urgency": "low"   // none | low | medium | high
}
```

## **3. Weekly Health Report (Batch)**

Batch对于生成健康周报来说成本会降低50-60%左右

工作流：

## 1. 收集用户的日常饮食/运动/睡眠数据

metrics.json  # { "nutrition": [...], "steps": 45000, "sleep": {...} }

## 2.用cmd将提示词和一周数据汇总：

cat > prompt_u123.json <<EOF
Prompt
Here are this week’s metrics:

‘’’json

<metrics.json>

## 3.加入工作流

echo ‘{ “custom_id”:“U123”, “method”:“POST”, “url”:”/v1/chat/completions”, “body”:{ …prompt… } }’ >> batch_items.jsonl

## 4.提交job batch

### Poll & store

```bash
openai batch retrieve BATCH_ID
openai batches content BATCH_ID > reports.jsonl
```

示例报告输出：

Your Week in Review (May 26 – Jun 1)

## Nutrition

- Avg. calories: 1 850 kcal (-8 % vs previous)
- Veggie servings: 3 / day ✅

## Activity

- Steps: 45 000 (goal 70 000) → Aim for +3 000 steps/day.

## Sleep

- Avg. duration: 6 h 20 m 😴 Add 40 min wind-down routine.

## Suggestion

 

---