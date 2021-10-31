package com.windmill312.audit.service.impl;

import com.windmill312.audit.dao.SiteVisitRepository;
import com.windmill312.audit.entity.SiteVisitEntity;
import com.windmill312.audit.exception.InsufficientCountryCodeFormatException;
import com.windmill312.audit.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

    private final SiteVisitRepository siteVisitRepository;
    private final Set<String> isoCountryCodesDictionary = Set.of(Locale.getISOCountries());

    public String getVisitStatistic() {
        StringBuilder stringBuilder = new StringBuilder().append("{ ");
        siteVisitRepository.findAll().forEach(siteVisitEntity -> stringBuilder
                .append(siteVisitEntity.getIsoCountryCode())
                .append(": ")
                .append(siteVisitEntity.getVisitNumber())
                .append(", ")
        );

        if (stringBuilder.length() > 2) {
            stringBuilder.replace(stringBuilder.length() - 2, stringBuilder.length() + 1, "");
        }

        stringBuilder.append(" }");
        return stringBuilder.toString();
    }

    @Transactional
    public void auditSiteVisit(String countryCode) {
        final var upperCasedCountryCode = countryCode.toUpperCase();

        if (isoCountryCodesDictionary.contains(upperCasedCountryCode)) {
            final var optionalSiteVisit = siteVisitRepository.findByIsoCountryCode(upperCasedCountryCode);
            if (optionalSiteVisit.isPresent()) {
                final var existedSiteVisit = optionalSiteVisit.get();
                existedSiteVisit.setVisitNumber(existedSiteVisit.getVisitNumber() + 1);
                log.info("Incrementing visit number from country with code: {}", countryCode);
                siteVisitRepository.save(existedSiteVisit);
            } else {
                final var siteVisit = new SiteVisitEntity();
                siteVisit.setVisitNumber(1L);
                siteVisit.setIsoCountryCode(upperCasedCountryCode);
                log.info("Saving visit from new country with code: {}", countryCode);
                siteVisitRepository.save(siteVisit);
            }
        } else {
            log.error("Country code: {} is not in ISO 3166-1 alpha-2 format", countryCode);
            throw new InsufficientCountryCodeFormatException(String.format("Country code: %s is not in ISO 3166-1 alpha-2 format", countryCode));
        }
    }
}