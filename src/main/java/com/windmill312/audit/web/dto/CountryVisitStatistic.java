package com.windmill312.audit.web.dto;

import lombok.Data;

@Data
public class CountryVisitStatistic {
    private String isoCountryCode;
    private Long visitNumber;
}