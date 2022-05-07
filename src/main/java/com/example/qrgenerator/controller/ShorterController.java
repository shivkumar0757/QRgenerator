package com.example.qrgenerator.controller;


import com.example.qrgenerator.services.CodeGenerator;
import com.example.qrgenerator.data.Shorter;
import com.example.qrgenerator.data.ShorterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.time.ZonedDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
public class ShorterController {
    Logger logger = LoggerFactory.getLogger(ShorterController.class.getSimpleName());

    private final ShorterRepository repository;
    private CodeGenerator codeGenerator;
    @Value("${shorter.length}")
    private Integer shorterLength;

    @Autowired
    public ShorterController(final ShorterRepository repository) {
        this.repository = repository;
        this.codeGenerator = new CodeGenerator();
    }

    @PostMapping(path = "urls/", consumes = APPLICATION_JSON_VALUE)
    public Shorter createShortUrl(@RequestBody Shorter shorter) {
        String hash = codeGenerator.generate(shorterLength);
        logger.info("Generated hash: "+hash);
        if (shorter != null) {
            String shorterString = URLDecoder.decode(shorter.getOriginalUrl());
            logger.info(shorterString);
            shorter = new Shorter(null, hash, shorterString, ZonedDateTime.now());
            return repository.save(shorter);
        } else {
            return null;
        }
    }

    @GetMapping(path = "urls/{hash}")
    public ResponseEntity redirectShorter(@PathVariable("hash") String hash) {
        //TODO find hash in DB and redirect to original URL
        logger.info(hash);
        Shorter shorter = repository.findByHash(hash);
        if (shorter != null) {
            HttpHeaders headers = new HttpHeaders();
            System.out.println(shorter.getOriginalUrl());
            System.out.println(shorter.getHash());
            headers.add("Location", shorter.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "allurls/")
    public ResponseEntity getAll() {
        return ResponseEntity.ok(repository.findAll());
    }
}
