package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.dto.View;
import com.epam.esm.link.LinkBuilder;
import com.epam.esm.service.CertificateService;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


/**
 * The type Certificate rest controller.
 */
@RestController
@RequestMapping(value = "/certificates")
@RequiredArgsConstructor
public class CertificateController {
    private final CertificateService certificateService;
    private final LinkBuilder<CertificateDto> linkBuilder;

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
    @JsonView(View.Base.class)
    public CollectionModel<CertificateDto> searchByOptions(CertificateFilterDto filter,
                                                           @RequestParam(name = "sort", required = false) Set<String> sorts,
                                                           PageDto page) {
        List<CertificateDto> certificates = certificateService.findByFilter(filter, page, sorts);
        for (CertificateDto certificate : certificates) {
            Link self = buildSelf(this.getClass(), certificate.getId(), FIND);
            linkBuilder.attachLinks(certificate, self);
        }
        String query = prepareQuery(pageQuery(page), filterQuery(filter));
        Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);
        return attachToCollection(certificates, self);
    }

    /**
     * Gets certificate by id.
     *
     * @param id *path variable of requesting tag
     * @return the certificate object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<CertificateDto> getCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.find(id);
        Link self = buildSelf(CertificateController.class, id, FIND);
        linkBuilder.attachLinks(certificate, self);
        return certificate;
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
    @JsonView(View.Base.class)
    public RepresentationModel<CertificateDto> addCertificate(@RequestBody CertificateDto certificate) {
        CertificateDto savedCertificate = certificateService.save(certificate);
        Link self = buildSelf(this.getClass(), SAVE);
        linkBuilder.attachLinks(certificate, self);
        return savedCertificate;
    }

    /**
     * Modify certificate.
     *
     * @param certificate the certificate data transfer object
     * @param tags        *the tags to be attached
     * @return the patched certificate object
     */
    @PatchMapping(value = "/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<CertificateDto> modifyCertificate(@RequestBody CertificateDto certificate, @PathVariable int id) {
        certificate.setId(id);
        CertificateDto updatedCertificate = certificateService.update(certificate);
        Link self = buildSelf(this.getClass(), UPDATE);
        linkBuilder.attachLinks(certificate, self);
        return updatedCertificate;
    }

    /**
     * Delete certificate.
     *
     * @param id *path variable of deleting certificate
     * @return the delete response
     */
    @DeleteMapping("/{id:^[0-9]+$}")
    @JsonView(View.Base.class)
    public RepresentationModel<CertificateDto> deleteCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.delete(id);
        Link self = buildSelf(this.getClass(), id, DELETE);
        linkBuilder.attachLinks(certificate, self);
        return certificate;
    }
}