package com.example.tallyintegration.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.nio.charset.StandardCharsets;

@Service
public class TallyXmlService {

    @Value("${tallyUrl}")
    private String tallyUrl;

    private final RestClient restClient;

    public TallyXmlService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String sendRequest(String xml) {

        if (xml == null || xml.isBlank()) {
            throw new IllegalArgumentException(
                    "Tally XML request cannot be empty");
        }

        try {
            String response = restClient.post()
                    .uri(tallyUrl)
                    .contentType(new MediaType(
                            "text", "xml", StandardCharsets.UTF_8))
                    .body(xml)
                    .retrieve()
                    .body(String.class);

            if (response == null || response.isBlank()) {
                throw new IllegalStateException(
                        "Empty response received from Tally");
            }

            return response;

        } catch (Exception e) {
            throw new IllegalStateException(
                    "Failed to communicate with Tally: "
                            + e.getMessage(), e);
        }
    }



}
