package com.windmill312.audit.dao;

import com.windmill312.audit.entity.SiteVisitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface SiteVisitRepository extends JpaRepository<SiteVisitEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<SiteVisitEntity> findByIsoCountryCode(String isoCountryCode);
}