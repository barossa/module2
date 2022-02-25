package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.CertificateFilterDto;
import com.epam.esm.dto.PageDto;
import com.epam.esm.service.CertificateService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

import static com.epam.esm.link.HttpMethod.GET;
import static com.epam.esm.link.LinkBuilder.*;
import static com.epam.esm.link.LinkBuilder.RelType.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


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
    public CollectionModel<CertificateDto> searchByOptions(CertificateFilterDto filter,
                                                           @RequestParam(name = "sort", required = false) Set<String> sorts,
                                                           PageDto page) {
        List<CertificateDto> certificates = certificateService.findByFilter(filter, page, sorts);
        for (CertificateDto certificate : certificates) {
            List<Link> links = buildLinks(this.getClass(), certificate.getId(), UPDATE, DELETE);
            Link self = buildSelf(this.getClass(), certificate.getId(), FIND);
            certificate.add(self);
            certificate.add(links);
        }
        CollectionModel<CertificateDto> collection = CollectionModel.of(certificates);
        if (!certificates.isEmpty()) {
            String pageQuery = pageQuery(page);
            String filterQuery = filterQuery(filter);
            String query = prepareQuery(pageQuery, filterQuery);
            Link self = linkTo(this.getClass()).slash(query).withSelfRel().withType(GET);
            collection.add(self);
        }
        return collection;
    }

    /**
     * Gets certificate by id.
     *
     * @param id *path variable of requesting tag
     * @return the certificate object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    public RepresentationModel<CertificateDto> getCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.find(id);
        List<Link> links = buildLinks(CertificateController.class, id, FIND_ALL, UPDATE, DELETE, SAVE);
        Link self = buildSelf(CertificateController.class, id, FIND);
        certificate.add(self);
        certificate.add(links);

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
    public RepresentationModel<CertificateDto> addCertificate(@RequestBody CertificateDto certificate) {
        CertificateDto savedCertificate = certificateService.save(certificate);
        List<Link> links = buildLinks(this.getClass(), savedCertificate.getId(), FIND, UPDATE, DELETE, FIND_ALL);
        Link self = buildSelf(this.getClass(), SAVE);
        savedCertificate.add(self);
        savedCertificate.add(links);

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
    public RepresentationModel<CertificateDto> modifyCertificate(@RequestBody CertificateDto certificate, @PathVariable int id) {
        certificate.setId(id);
        CertificateDto updatedCertificate = certificateService.update(certificate);
        List<Link> links = buildLinks(this.getClass(), certificate.getId(), FIND, DELETE, SAVE);
        Link self = buildSelf(this.getClass(), UPDATE);
        updatedCertificate.add(self);
        updatedCertificate.add(links);
        return updatedCertificate;
    }

    /**
     * Delete certificate.
     *
     * @param id *path variable of deleting certificate
     * @return the delete response
     */
    @DeleteMapping("/{id:^[0-9]+$}")
    public RepresentationModel<CertificateDto> deleteCertificate(@PathVariable int id) {
        CertificateDto certificate = certificateService.delete(id);
        List<Link> links = buildLinks(this.getClass(), FIND_ALL, SAVE);
        Link self = buildSelf(this.getClass(), id, DELETE);
        certificate.add(self);
        certificate.add(links);
        return certificate;
    }
}