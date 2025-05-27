from fastapi import FastAPI
from pydantic import BaseModel
from mistralai import Mistral

app = FastAPI()

class CodePatch(BaseModel):
    filename: str
    patch: str

# Mistral API setup
api_key = "<MISTRAL_API_KEY>"
model = "mistral-small-latest"

client = Mistral(api_key=api_key)

@app.post("/review")
async def review_patch(data: CodePatch):
    prompt = f"""
    You are a professional code reviewer.

    Please review the following Python patch in `{data.filename}`.
    Identify:
    - Bugs or logical errors
    - Readability or style issues
    - Suggestions for improvement
    - Any good practices used

    Patch:
    {data.patch}

    Respond with bullet-pointed review comments only.
    """

    try:
        response = await client.chat.stream_async(
            model=model,
            messages=[{"role": "user", "content": prompt}]
        )

        review_message = ""
        async for chunk in response:
            delta = chunk.data.choices[0].delta
            content = delta.content

            if isinstance(content, str):
                review_message += content

        print(review_message)

        return {"review": review_message.strip()}

    except Exception as e:
        return {"error": str(e)}
