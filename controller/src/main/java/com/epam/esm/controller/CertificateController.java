package com.epam.esm.controller;

import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;
import com.epam.esm.service.dto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


/**
 * The type Certificate rest controller.
 */
@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;

    /**
     * Search certificates by filter params.
     *
     * @param tags         the certificate names
     * @param names        the certificate names
     * @param descriptions the certificate descriptions
     * @param mode         the search mode (1 - full match/0 - part of request)
     * @param sort         the sort type (NAME,DATE/ASC,DESC | Ex. NAME_ASC, DATE_DESC)
     * @return the certificate objects
     */
    @GetMapping
    public ResponseEntity<Object> searchByOptions(Filter filter,
                                                  @RequestParam(name = "sort", required = false) Set<String> sorts,
                                                  PageDto page) {
        List<CertificateDto> certificates = certificateService.findByFilter(filter, page, sorts);
        return ResponseEntity.ok().body(certificates);
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
     * @param name        *the certificate name
     * @param description *the certificate description
     * @param price       *the certificate price
     * @param duration    *the certificate duration
     * @param tags        *the tags to be attached
     * @return the added certificate object
     */
    @PostMapping
    public ResponseEntity<Object> addCertificate(CertificateDto certificate) {
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
    public ResponseEntity<Object> modifyCertificate(CertificateDto certificate) {
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
        CertificateDto certificateDto = certificateService.delete(id);
        return ResponseEntity.ok(certificateDto);
    }
}
