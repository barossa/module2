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


/**
 * The type Certificate rest controller.
 */
@RestController
@RequestMapping(value = "/certificates")
public class CertificateController {
    private final CertificateService certificateService;

    /**
     * Instantiates a new Certificate controller.
     *
     * @param certificateService the certificate service
     */
    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Search certificates by filter params.
     *
     * @param tags         the certificate names
     * @param names        the certificate names
     * @param descriptions the certificate descriptions
     * @param mode         the search mode (1 - full complete/0 - part of request)
     * @param sorts        the sort type (NAME,DATE/ASC,DESC | Ex. NAME_ASC, DATE_DESC)
     * @return the certificate objects
     */
    @GetMapping
    public ResponseEntity<Object> searchByOptions(@RequestParam(name = "tag", required = false) List<String> tags,
                                                  @RequestParam(name = "name", required = false) List<String> names,
                                                  @RequestParam(name = "description", required = false) List<String> descriptions,
                                                  @RequestParam(name = "mode", required = false, defaultValue = "1") boolean mode,
                                                  @RequestParam(name = "sort", required = false) Set<String> sorts) {
        List<CertificateDto> certificates = certificateService.findByFilter(new Filter(tags, names, descriptions, mode));
        List<CertificateDto> sortedCertificates = CertificateSortUtils.sort(certificates, sorts);
        return ResponseEntity.ok().body(sortedCertificates);
    }

    /**
     * Gets certificate by id.
     *
     * @param id *path variable of requesting tag
     * @return the certificate object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> getCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.find(id);
        return ResponseEntity.ok(certificate);
    }

    /**
     * Add new certificate.
     *
     * @param certificate the certificate data transfer object
     * @param name *the certificate name
     * @param description *the certificate description
     * @param price *the certificate price
     * @param duration *the certificate duration
     * @param tags        *the tags to be attached
     * @return the added certificate object
     */
    @PostMapping
    public ResponseEntity<Object> addCertificate(CertificateDto certificate, @RequestParam(name = "tag") String[] tags) {
        Set<TagDto> collectedTags = Arrays.stream(tags)
                .map(TagDto::new)
                .collect(Collectors.toSet());
        certificate.setTags(collectedTags);
        CertificateDto savedCertificate = certificateService.save(certificate);
        return ResponseEntity.ok().body(savedCertificate);
    }

    /**
     * Modify certificate.
     *
     * @param certificate the certificate data transfer object
     * @param tags        *the tags to be attached
     * @return the patched certificate object
     */
    @PatchMapping(value = "/{id:^[0-9]+$}")
    public ResponseEntity<Object> modifyCertificate(CertificateDto certificate, @RequestParam(required = false, name = "tag") Set<TagDto> tags) {
        certificate.setTags(tags);
        certificateService.update(certificate);
        CertificateDto updatedCertificate = certificateService.find(certificate.getId());
        return ResponseEntity.ok().body(updatedCertificate);
    }

    /**
     * Delete certificate.
     *
     * @param id *path variable of deleting certificate
     * @return the delete response
     */
    @DeleteMapping("/{id:^[0-9]+$}")
    public ResponseEntity<Object> deleteCertificate(@PathVariable int id) {
        certificateService.delete(id);
        return ResponseEntity.ok(null);
    }
}
