package com.naveen.microservice.wordwrap.controller;

import com.naveen.microservice.wordwrap.controller.dto.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@Slf4j
public class WordWrapController {
    private static final String BASE_API_BATH = "/api/v1/wrap";

    @GetMapping(value = BASE_API_BATH, consumes = "application/json")
    private ResponseEntity test(@Valid @RequestBody final Paragraph paragraph) {
        log.info("Request received with Paragraph object {}", paragraph);
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
