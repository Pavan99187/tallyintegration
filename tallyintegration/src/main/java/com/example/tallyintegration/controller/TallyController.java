package com.example.tallyintegration.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/api/tally")
public class TallyController {

  private final RestClient restClient;

    public TallyController(RestClient restClient) {
        this.restClient = restClient;
    }

    @PostMapping(
            value = "/receive",
            consumes = {
                    MediaType.APPLICATION_XML_VALUE,
                    MediaType.TEXT_XML_VALUE
            },
            produces = MediaType.APPLICATION_XML_VALUE
    )
    public ResponseEntity<String> receiveXml(@RequestBody String xml) {

        try {
            System.out.println("XML SENT TO TALLY:");
            System.out.println(xml);

            String tallyResponse = restClient.post()
                    .uri("http://localhost:9000")
                    .contentType(MediaType.APPLICATION_XML)
                    .body(xml)
                    .retrieve()
                    .body(String.class);

            return ResponseEntity.ok(tallyResponse);

        } catch (RestClientException e) {
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                    .body("<RESPONSE>Error connecting to TallyPrime: "
                            + escapeXml(e.getMessage())
                            + "</RESPONSE>");
        }
    }

    private String escapeXml(String value) {
        if (value == null) {
            return "Unknown error";
        }
        return value.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&apos;");
    }


}
