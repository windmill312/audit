package com.windmill312.audit.service;

public interface AuditService {
    void auditSiteVisit(String countryCode);

    String getVisitStatistic();
}