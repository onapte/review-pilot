package com.reviewpilot.extractor.controller;

import com.reviewpilot.extractor.service.RedisService;
import com.reviewpilot.extractor.service.ParserService;
import com.reviewpilot.extractor.service.ParserService.ParsedPRInfo;
import com.reviewpilot.extractor.service.GithubService;
import com.reviewpilot.extractor.service.LLMService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PayloadController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private GithubService githubService;

    @Autowired
    private LLMService llmService;

    @GetMapping("/next-payload")
    public String getNextPayload() {
        String payload = redisService.popOldestPayload();
        if (payload == null) {
            return "{\"message\": \"No payloads in queue.\"}";
        }

        try {
            ParsedPRInfo parsedPayload = ParserService.parsePRPayload(payload);

            List<Map<String, Object>> files = githubService.getPullRequestFiles(
                    parsedPayload.owner, parsedPayload.repo, parsedPayload.prNumber);

            StringBuilder response = new StringBuilder();
            for (Map<String, Object> file : files) {
                String filename = (String) file.get("filename");
                Object patchObj = file.get("patch");

                if (!(patchObj instanceof String)) {
                    System.out.println("Skipping file without patch: " + file.get("filename"));
                    continue;
                }
                String patch = (String) patchObj;
                String review = llmService.getLLMReview(filename, patch);

                response.append("<p>");
                response.append("File: ").append(filename).append("\n")
                        .append("Patch: ").append(patch).append("\n\n")
                        .append("LLM Review: ").append(review).append("\n");
            }

            response.append("</p>\n\n\n");

            return response.toString();

        } catch (Exception e) {
            return "{\"error\": \"Failed to parse or fetch PR data: " + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/healthz")
    public String healthCheck() {
        return "Status: OK";
    }
}
