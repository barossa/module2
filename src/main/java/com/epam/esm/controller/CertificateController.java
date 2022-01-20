package com.epam.esm.controller;

import com.epam.esm.controller.error.ErrorResponseBuilder;
import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.exception.ServiceException;
import com.epam.esm.service.CertificateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.epam.esm.controller.error.ErrorResponseBuilder.*;

@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private static final Logger logger = LogManager.getLogger(CertificateController.class);

    private final CertificateService certificateService;
    private final ErrorResponseBuilder errorBuilder;

    public CertificateController(CertificateService certificateService, ErrorResponseBuilder errorResponseBuilder) {
        this.certificateService = certificateService;
        this.errorBuilder = errorResponseBuilder;
    }

    @GetMapping
    public ResponseEntity<Object> getAllCertificates() {
        try {
            List<Certificate> certificates = certificateService.findAll();
            return ResponseEntity.ok(certificates);

        } catch (ServiceException e) {
            logger.error("Error occurred loading all certificates: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Object> getCertificate(@PathVariable int id) {
        try {
            Optional<Certificate> certificateOptional = certificateService.find(id);
            if (certificateOptional.isPresent()) {
                return ResponseEntity.ok(certificateOptional.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBuilder.build(OBJECT_NOT_FOUND));
            }

        } catch (ServiceException e) {
            logger.error("Error occurred getting certificate: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }

    @PostMapping
    public ResponseEntity<Object> addCertificate(String name, String description, BigDecimal price, long duration, String[] tags) {
        try {
            Certificate certificate = new Certificate(name, description, price, duration);
            Optional<Certificate> certificateOptional = certificateService.save(certificate);
            if (certificateOptional.isPresent()) {
                return ResponseEntity.ok().body(certificateOptional.get());
            } else {
                return ResponseEntity.internalServerError().body(errorBuilder.build(OBJECT_POSTING_ERROR));
            }

        } catch (ServiceException e) {
            logger.error("Error occurred adding certificate: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(errorBuilder.build(INTERNAL_ERROR));
        }
    }
}
