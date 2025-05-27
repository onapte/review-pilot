package com.reviewpilot.extractor.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.HashMap;
import java.util.Map;

@Service
public class LLMService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String getLLMReview(String filename, String patch) {
        String url = "http://localhost:9000/review";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> body = new HashMap<>();
        body.put("filename", filename);
        body.put("patch", patch);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map<String, String>> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    entity,
                    new ParameterizedTypeReference<Map<String, String>>() {
                    });
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Object review = response.getBody().get("review");
                if (review instanceof String) {
                    return (String) review;
                } else {
                    return "LLM response does not contain a valid 'review' field.";
                }
            }
        } catch (Exception e) {
            return "Error calling LLM: " + e.getMessage();
        }

        return "No review returned.";
    }
}
