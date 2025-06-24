from fastapi import FastAPI
from pydantic import BaseModel
from transformers import pipeline

app = FastAPI()

class InputText(BaseModel):
    text: str

classifier = pipeline("text-classification", model="unitary/toxic-bert")

@app.post("/predict")
def predict(data: InputText):
    result = classifier(data.text)
    return {"predictions": result}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
