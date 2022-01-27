package com.epam.esm.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.TagDto;
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
        List<CertificateDto> certificates = certificateService.findAll();
        return ResponseEntity.ok(certificates);
    }

    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.find(id);
        return ResponseEntity.ok(certificate);
    }

    @PostMapping
    public ResponseEntity<Object> addCertificate(CertificateDto certificate, @RequestParam(name = "tag") String[] tags) {
        Set<TagDto> collectedTags = Arrays.stream(tags)
                .map(TagDto::new)
                .collect(Collectors.toSet());
        certificate.setTags(collectedTags);
        CertificateDto savedCertificate = certificateService.save(certificate);
        return ResponseEntity.ok().body(savedCertificate);
    }

    @PatchMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> modifyCertificate(CertificateDto certificate, @RequestParam(required = false, name = "tag") Set<TagDto> tags) {
        certificate.setTags(tags);
        certificateService.update(certificate);
        CertificateDto updatedCertificate = certificateService.find(certificate.getId());
        return ResponseEntity.ok().body(updatedCertificate);
    }

    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int id) {
        certificateService.delete(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/byTag")
    public ResponseEntity<Object> searchByTagName(@RequestParam(defaultValue = "") String tagName) {
        List<CertificateDto> certificates = certificateService.findByTagName(tagName);
        return ResponseEntity.ok().body(certificates);
    }

    @GetMapping("/byPart")
    public ResponseEntity<Object> searchByPartOfNameOrDescription(@RequestParam(defaultValue = "", name = "query") String[] searches) {
        List<CertificateDto> certificates = certificateService.findByPartOfNameOrDescription(searches);
        return ResponseEntity.ok().body(certificates);
    }
}
