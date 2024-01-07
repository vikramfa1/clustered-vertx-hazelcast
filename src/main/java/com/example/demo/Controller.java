package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class Controller {

    @GetMapping(value = "/app/info", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public String dataFetch() {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://catfact.ninja/fact";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);
        String value = responseEntity.getBody();
        return value;
    }
}
