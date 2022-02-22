package com.epam.esm.controller;

import com.epam.esm.controller.dto.Certificate;
import com.epam.esm.controller.dto.EntityMapper;
import com.epam.esm.service.CertificateService;
import com.epam.esm.service.dto.CertificateDto;
import com.epam.esm.service.dto.Filter;
import com.epam.esm.service.dto.PageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.epam.esm.controller.link.LinkBuilder.RelType.*;
import static com.epam.esm.controller.link.LinkBuilder.*;


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
    public CollectionModel<Certificate> searchByOptions(Filter filter,
                                                        @RequestParam(name = "sort", required = false) Set<String> sorts,
                                                        PageDto page) {
        List<CertificateDto> certificatesDto = certificateService.findByFilter(filter, page, sorts);
        List<Certificate> certificates = EntityMapper.mapCertificatesFromDto(certificatesDto, Collectors.toList());
        for (Certificate certificate : certificates) {
            List<Link> links = buildLinks(this.getClass(), certificate.getId(), UPDATE, DELETE);
            Link self = buildSelf(this.getClass(), certificate.getId(), FIND);
            certificate.add(self);
            certificate.add(links);
        }

        Link users = buildForName(UserController.class, FIND_ALL, "findUsers");
        Link tags = buildForName(TagController.class, FIND_ALL, "findTags");
        Link self = buildSelf(this.getClass(), FIND_ALL);
        List<Link> others = Stream.of(self, tags, users).collect(Collectors.toList());

        return CollectionModel.of(certificates, others);
    }

    /**
     * Gets certificate by id.
     *
     * @param id *path variable of requesting tag
     * @return the certificate object
     */
    @GetMapping(value = "/{id:^[0-9]+$}")
    public RepresentationModel<Certificate> getCertificate(@PathVariable int id) {
        CertificateDto certificateDto = certificateService.find(id);
        Certificate certificate = EntityMapper.mapCertificateFromDto(certificateDto);

        List<Link> links = buildLinks(CertificateController.class, id, FIND_ALL, UPDATE, DELETE);
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
    public RepresentationModel<Certificate> addCertificate(CertificateDto certificate) {
        CertificateDto certificateDto = certificateService.save(certificate);
        Certificate savedCertificate = EntityMapper.mapCertificateFromDto(certificateDto);

        List<Link> links = buildLinks(this.getClass(), certificateDto.getId(), FIND, UPDATE, DELETE, FIND_ALL);
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
    public RepresentationModel<Certificate> modifyCertificate(CertificateDto certificate) {
        CertificateDto certificateDto = certificateService.update(certificate);
        Certificate updatedCertificate = EntityMapper.mapCertificateFromDto(certificateDto);

        List<Link> links = buildLinks(this.getClass(), certificate.getId(), FIND, DELETE);
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
    public RepresentationModel<Certificate> deleteCertificate(@PathVariable int id) {
        CertificateDto certificateDto = certificateService.delete(id);
        Certificate certificate = EntityMapper.mapCertificateFromDto(certificateDto);

        List<Link> links = buildLinks(this.getClass(), FIND_ALL);
        Link self = buildSelf(this.getClass(), id, DELETE);
        certificate.add(self);
        certificate.add(links);

        return certificate;
    }
}
