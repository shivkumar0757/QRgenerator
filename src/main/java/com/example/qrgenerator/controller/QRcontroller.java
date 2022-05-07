package com.example.qrgenerator.controller;

import com.example.qrgenerator.services.QRgeneratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;

@RestController
public class QRcontroller {
    private static final String QR_CODE_IMAGE_PATH = "./src/main/resources/QRCode.png";


    @GetMapping(value = "/genrateAndDownloadQRCode/{codeText}/{width}/{height}")
    public void download(
            @PathVariable("codeText") String codeText,
            @PathVariable("width") Integer width,
            @PathVariable("height") Integer height)
            throws Exception {
        QRgeneratorService.generateQRCodeImage(codeText, width, height, QR_CODE_IMAGE_PATH);
    }

    @CrossOrigin(origins = "http://127.0.0.1:8080")
    @GetMapping(value = "/genrateQRCode/{codeText}/{width}/{height}")
    public ResponseEntity<String> generateQRCode(
            @PathVariable("codeText") String codeText,
            @PathVariable("width") Integer width,
            @PathVariable("height") Integer height)
            throws Exception {
        byte[] imgData= QRgeneratorService.getQRCodeImage(codeText, width, height);
        String encodedImage = Base64.getEncoder().encodeToString(imgData);
        return ResponseEntity.status(HttpStatus.OK).body(encodedImage);
    }
}
