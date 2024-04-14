package casa2.assignment.ratingnormalizer.service;

import casa2.assignment.ratingnormalizer.dto.CreateDomainRecord;
import org.springframework.http.ResponseEntity;

public interface DomainService {
    ResponseEntity<?> createDomain(CreateDomainRecord domain);

    ResponseEntity<?> addSkill(Long skillId, Long domainId);
}
