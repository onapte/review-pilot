package com.reviewpilot.extractor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

import java.util.*;

@Service
public class GithubService {

    @Value("${github.token}")
    private String githubToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Map<String, Object>> getPullRequestFiles(String owner, String repo, int pullNumber) {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files", owner, repo, pullNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.set("Accept", "application/vnd.github.v3+json");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Map<String, Object>>>() {
                });

        return response.getBody();
    }

    public String getFileContent(String owner, String repo, String path, String ref) {
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s?ref=%s", owner, repo, path, ref);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(githubToken);
        headers.set("Accept", "application/vnd.github.v3.raw");

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                String.class);

        return response.getBody();
    }
}
