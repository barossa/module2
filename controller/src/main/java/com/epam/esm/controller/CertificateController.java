package com.epam.esm.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.CertificateSortUtils;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;
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

    @GetMapping("/search")
    public ResponseEntity<Object> searchByOptions(@RequestParam(name = "tag", required = false) List<String> tags,
                                                  @RequestParam(name = "name", required = false) List<String> names,
                                                  @RequestParam(name = "description", required = false) List<String> descriptions,
                                                  @RequestParam(name = "mode", required = false, defaultValue = "1") boolean mode,
                                                  @RequestParam(name = "sort", required = false) Set<String> sorts) {
        List<CertificateDto> certificates = certificateService.findByFilter(new Filter(tags, names, descriptions, mode));
        List<CertificateDto> sortedCertificates = CertificateSortUtils.sort(certificates, sorts);
        return ResponseEntity.ok().body(sortedCertificates);
    }

}
