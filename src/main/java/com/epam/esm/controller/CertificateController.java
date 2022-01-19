package com.epam.esm.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
public class CertificateController {
    private static final Logger logger = LogManager.getLogger(CertificateController.class);

    private CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getCertificate(@PathVariable int id){
        try{
            Optional<Certificate> certificate = certificateService.find(id);
            return ResponseEntity.status(200).body(certificate.get().toString());
        }catch (ServiceException e){
            logger.error("Error occurred getting certificate: {}", e.getMessage());
            return ResponseEntity.status(404).body("Service exception occurred");
        }
    }

    @PostMapping
    public ResponseEntity<String> addCertificate(String name, String description, BigDecimal price, long duration, String[] tags) {
        try {
            LocalDateTime now = LocalDateTime.now();
            Certificate certificate = new Certificate(name, description, price, duration, now, now);
            Optional<Certificate> certificateOptional = certificateService.save(certificate);
            return ResponseEntity.status(200).body(certificateOptional.get().toString());
        } catch (ServiceException e) {
            logger.error("Error occurred adding certificate: {}", e.getMessage());
            return ResponseEntity.status(404).body("Service exception occurred");
        }
    }
}
