package com.windmill312.audit.unit.service;

import com.windmill312.audit.dao.SiteVisitRepository;
import com.windmill312.audit.entity.SiteVisitEntity;
import com.windmill312.audit.exception.InsufficientCountryCodeFormatException;
import com.windmill312.audit.service.impl.AuditServiceImpl;
import com.windmill312.audit.unit.UnitTests;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static com.windmill312.audit.common.TestUtils.randomPositiveNumber;
import static com.windmill312.audit.common.TestUtils.randomString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuditServiceTest extends UnitTests {
    @Mock
    private SiteVisitRepository siteVisitRepository;

    @InjectMocks
    private AuditServiceImpl auditService;

    @Captor
    private ArgumentCaptor<SiteVisitEntity> siteVisitEntityCaptor;

    @Test
    void shouldSuccessfullyReturnVisitStatistic() {
        final var firstSiteVisit = new SiteVisitEntity();
        firstSiteVisit.setId(randomPositiveNumber().longValue());
        firstSiteVisit.setIsoCountryCode(randomString());
        firstSiteVisit.setVisitNumber(randomPositiveNumber().longValue());
        final var secondSiteVisit = new SiteVisitEntity();
        secondSiteVisit.setId(randomPositiveNumber().longValue());
        secondSiteVisit.setIsoCountryCode(randomString());
        secondSiteVisit.setVisitNumber(randomPositiveNumber().longValue());

        final var expectedResult = new StringBuilder("{ ")
                .append(firstSiteVisit.getIsoCountryCode())
                .append(": ")
                .append(firstSiteVisit.getVisitNumber())
                .append(", ")
                .append(secondSiteVisit.getIsoCountryCode())
                .append(": ")
                .append(secondSiteVisit.getVisitNumber())
                .append(" }")
                .toString();

        when(siteVisitRepository.findAll()).thenReturn(List.of(firstSiteVisit, secondSiteVisit));

        String actualResult = auditService.getVisitStatistic();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    void shouldThrowExceptionIfCountryCodeIsInvalid() {
        final var countryCode = randomString();

        final var e = assertThrows(
                InsufficientCountryCodeFormatException.class,
                () -> auditService.auditSiteVisit(countryCode)
        );
        assertEquals(
                String.format("Country code: %s is not in ISO 3166-1 alpha-2 format", countryCode),
                e.getMessage()
        );
    }

    @Test
    void shouldSuccessfullySaveVisitNumberFromNewCountry() {
        final var countryCode = Arrays.stream(Locale.getISOCountries()).findAny().get();

        when(siteVisitRepository.findByIsoCountryCode(any())).thenReturn(Optional.empty());
        when(siteVisitRepository.save(siteVisitEntityCaptor.capture())).thenAnswer(returnsFirstArg());

        auditService.auditSiteVisit(countryCode);

        verify(siteVisitRepository, times(1)).findByIsoCountryCode(countryCode);
        final var savedSiteVisitEntity = siteVisitEntityCaptor.getValue();
        assertEquals(countryCode, savedSiteVisitEntity.getIsoCountryCode());
        assertEquals(1, savedSiteVisitEntity.getVisitNumber());
        assertNull(savedSiteVisitEntity.getId());
    }

    @Test
    void shouldSuccessfullyIncrementVisitNumberFromExistentCountry() {
        final var countryCode = Arrays.stream(Locale.getISOCountries()).findAny().get();
        final var initialVisitNumber = randomPositiveNumber().longValue();
        final var existentSiteVisitEntity = new SiteVisitEntity();
        existentSiteVisitEntity.setId(randomPositiveNumber().longValue());
        existentSiteVisitEntity.setIsoCountryCode(randomString());
        existentSiteVisitEntity.setVisitNumber(initialVisitNumber);

        when(siteVisitRepository.findByIsoCountryCode(any())).thenReturn(Optional.of(existentSiteVisitEntity));
        when(siteVisitRepository.save(siteVisitEntityCaptor.capture())).thenAnswer(returnsFirstArg());

        auditService.auditSiteVisit(countryCode);

        verify(siteVisitRepository, times(1)).findByIsoCountryCode(countryCode);
        final var savedSiteVisitEntity = siteVisitEntityCaptor.getValue();
        assertEquals(existentSiteVisitEntity.getIsoCountryCode(), savedSiteVisitEntity.getIsoCountryCode());
        assertEquals(initialVisitNumber + 1, savedSiteVisitEntity.getVisitNumber());
        assertEquals(existentSiteVisitEntity.getId(), savedSiteVisitEntity.getId());
    }
}