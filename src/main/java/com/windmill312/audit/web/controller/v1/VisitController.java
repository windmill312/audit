package com.windmill312.audit.web.controller.v1;

import com.windmill312.audit.service.AuditService;
import com.windmill312.audit.web.controller.OpenController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VisitController extends OpenController {

    private final AuditService auditService;

    /*
        This response is too hard for parsing. Names of fields are absent. It should be changed on structure like this
        {
            statistic = [
                {
                    isoCountryCode: 'RU',
                    visitNumber: 123
                },
                ...
            ]
     */
    @GetMapping("/visit/statistic")
    public ResponseEntity<String> getVisitStatistic() {
        log.info("Got getting visit statistic request");
        return ResponseEntity.ok().body(auditService.getVisitStatistic());
    }

    @PostMapping("/visit")
    public ResponseEntity<Void> auditSiteVisit(@RequestParam String countryCode) {
        log.info("Got request for auditing site visit for country with code: {}", countryCode);
        auditService.auditSiteVisit(countryCode);
        return ResponseEntity.ok().build();
    }
}