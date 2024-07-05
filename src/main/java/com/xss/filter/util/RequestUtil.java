package com.xss.filter.util;

import java.util.List;

public class RequestUtil {
    private Object model;
    private Object tokenizer;
    private List<String> sampleTexts;
    private double threshold;

    // Getters and Setters
    public Object getModel() {
        return model;
    }

    public void setModel(Object model) {
        this.model = model;
    }

    public Object getTokenizer() {
        return tokenizer;
    }

    public void setTokenizer(Object tokenizer) {
        this.tokenizer = tokenizer;
    }

    public List<String> getSampleTexts() {
        return sampleTexts;
    }

    public void setSampleTexts(List<String> sampleTexts) {
        this.sampleTexts = sampleTexts;
    }

    public double getThreshold() {
        return threshold;
    }

    public void setThreshold(double threshold) {
        this.threshold = threshold;
    }
}
