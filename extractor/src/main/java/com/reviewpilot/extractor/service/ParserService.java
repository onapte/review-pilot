package com.reviewpilot.extractor.service;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ParserService {
    public static ParsedPRInfo parsePRPayload(String payloadJson) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(payloadJson);

        String owner = root.path("repository").path("full_name").asText().split("/")[0];
        String repo = root.path("repository").path("name").asText();
        int prNumber = root.path("number").asInt();
        String sha = root.path("pull_request").path("head").path("sha").asText();

        return new ParsedPRInfo(owner, repo, prNumber, sha);
    }

    public static class ParsedPRInfo {
        public String owner, repo, sha;
        public int prNumber;

        public ParsedPRInfo(String owner, String repo, int prNumber, String sha) {
            this.owner = owner;
            this.repo = repo;
            this.prNumber = prNumber;
            this.sha = sha;
        }
    }
}
