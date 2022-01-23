package com.epam.esm.controller;

import com.epam.esm.model.entity.Certificate;
import com.epam.esm.model.entity.Tag;
import com.epam.esm.service.CertificateService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    @GetMapping
    public ResponseEntity<Object> getAllCertificates() {
        List<Certificate> certificates = certificateService.findAll();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getCertificate(@PathVariable int id) {
        Certificate certificate = certificateService.find(id);
        return ResponseEntity.ok(certificate);
    }

    @PostMapping
    public ResponseEntity<Object> addCertificate(Certificate certificate, @RequestParam(name = "tag") String[] tags) {
        Set<Tag> collectedTags = Arrays.stream(tags)
                .map(Tag::new)
                .collect(Collectors.toSet());
        certificate.setTags(collectedTags);
        Certificate savedCertificate = certificateService.save(certificate);
        return ResponseEntity.ok().body(savedCertificate);
    }

    @PatchMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> modifyCertificate(Certificate certificate, @RequestParam(required = false, name = "tag") Set<Tag> tags) {
        certificate.setTags(tags);
        certificateService.update(certificate);
        Certificate updatedCertificate = certificateService.find(certificate.getId());
        return ResponseEntity.ok().body(updatedCertificate);
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int id) {
        certificateService.delete(id);
        return ResponseEntity.ok(null);
    }
}
