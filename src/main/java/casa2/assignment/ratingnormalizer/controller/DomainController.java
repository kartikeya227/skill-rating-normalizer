package casa2.assignment.ratingnormalizer.controller;

import casa2.assignment.ratingnormalizer.dto.CreateDomainRecord;
import casa2.assignment.ratingnormalizer.service.DomainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static casa2.assignment.ratingnormalizer.constant.ApplicationConstants.*;

@RestController
@RequestMapping(DOMAIN_BASE_URL)
public class DomainController {

    @Autowired
    private DomainService domainService;

    @PostMapping(CREATE_DOMAIN_URL)
    public ResponseEntity<?> createDomain(@RequestBody CreateDomainRecord domain) {
        return domainService.createDomain(domain);
    }

    @PostMapping(ADD_SKILL_TO_DOMAIN_URL)
    public ResponseEntity<?> addSkill(@RequestParam Long skillId, @RequestParam Long domainId) {
        return domainService.addSkill(skillId, domainId);
    }
}