package com.xss.filter.service;

import com.xss.filter.util.ConstantUtil;
import com.xss.filter.util.PromptUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class OpenAIService {

    private final String apiKey = "your-api-key";
    private final String apiUrl = "https://api.openai.com/v1/completions";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        List<String> prompts = PromptUtil.getPrompts();
        List<Pattern> patterns = generatePatterns(prompts);
        ConstantUtil.initializePatterns(patterns);
    }

    private List<Pattern> generatePatterns(List<String> prompts) {
        List<Pattern> patterns = new ArrayList<>();

        for (String prompt : prompts) {
            String pattern = callOpenAI(prompt);
            if (pattern != null) {
                Pattern compiledPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
                patterns.add(compiledPattern);
            }
        }
        return patterns;
    }

    private String callOpenAI(String prompt) {
        try {
            String payload = String.format(
                "{\"model\": \"text-davinci-003\", \"prompt\": \"%s\", \"max_tokens\": 150}", prompt);
            String response = restTemplate.postForObject(apiUrl, payload, String.class);
            JsonNode root = objectMapper.readTree(response);
            return root.path("choices").get(0).path("text").asText().trim();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}