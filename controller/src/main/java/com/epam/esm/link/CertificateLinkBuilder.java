package com.epam.esm.link;

import com.epam.esm.controller.CertificateController;
import com.epam.esm.dto.CertificateDto;
import com.epam.esm.util.SecurityUtils;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.dto.UserRoles.ADMIN_ROLE;
import static com.epam.esm.link.LinkUtils.RelType.*;
import static com.epam.esm.link.LinkUtils.buildLink;
import static com.epam.esm.link.LinkUtils.buildLinks;

@Component
public class CertificateLinkBuilder implements LinkBuilder<CertificateDto> {

    @Override
    public void attachLinks(CertificateDto certificateDto, Link self) {
        Link view = buildLink(CertificateController.class, certificateDto.getId(), FIND);
        certificateDto.add(self, view);
        List<String> roles = SecurityUtils.getCurrentRoles();
        if(roles.contains(ADMIN_ROLE)){
            List<Link> adminLinks = buildLinks(CertificateController.class, certificateDto.getId(), UPDATE, DELETE, SAVE);
            certificateDto.add(adminLinks);
        }
        LinkUtils.distinctLinks(certificateDto);
    }
}
