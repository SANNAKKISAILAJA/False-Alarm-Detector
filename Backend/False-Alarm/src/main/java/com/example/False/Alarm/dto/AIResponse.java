package com.example.False.Alarm.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class AIResponse {
    @JsonProperty("predictions")
    private List<Prediction> predictions;

    public static class Prediction {
        private String label;
        private double score;

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public double getScore() {
            return score;
        }

        public void setScore(double score) {
            this.score = score;
        }
    }

    public List<Prediction> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<Prediction> predictions) {
        this.predictions = predictions;
    }
}
