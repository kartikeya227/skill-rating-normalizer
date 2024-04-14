package casa2.assignment.ratingnormalizer.service.impl;

import casa2.assignment.ratingnormalizer.dto.CreateDomainRecord;
import casa2.assignment.ratingnormalizer.entity.Domain;
import casa2.assignment.ratingnormalizer.entity.Skill;
import casa2.assignment.ratingnormalizer.repository.DomainRepository;
import casa2.assignment.ratingnormalizer.repository.SkillRepository;
import casa2.assignment.ratingnormalizer.service.DomainService;
import casa2.assignment.ratingnormalizer.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service implementation for the Domain entity.
 * This class provides implementations for the methods declared in the DomainService interface.
 */
@Service
@Slf4j
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Creates a new domain.
     *
     * @param domainRecord the domain record to be created.
     * @return the response entity with the created domain.
     */
    @Override
    public ResponseEntity<?> createDomain(CreateDomainRecord domainRecord) {
        log.info("Request to create domain: {}", domainRecord.domainName());

        Domain domain = Domain
                .builder()
                .domainName(domainRecord.domainName())
                .build();

        domainRepository.save(domain);

        log.info("Domain created successfully.");
        return ResponseHandler.generateResponse("Domain created successfully.", HttpStatus.CREATED, domain);
    }

    /**
     * Adds a skill to a domain.
     *
     * @param skillId the id of the skill to be added.
     * @param domainId the id of the domain where the skill will be added.
     * @return the response entity with the updated domain.
     */
    @Override
    public ResponseEntity<?> addSkill(Long skillId, Long domainId) {
        log.info("Request to add skill with id: {} to domain with id: {}", skillId, domainId);

        // Check if Domain exists
        Domain domain = domainRepository.findById(domainId).orElse(null);
        if (domain == null) {
            log.error("Domain not found.");
            return ResponseHandler.generateResponse("Domain not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if Skill exists
        Skill skill = skillRepository.findById(skillId).orElse(null);
        if (skill == null) {
            log.error("Skill not found.");
            return ResponseHandler.generateResponse("Skill not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if Skill is already added to Domain
        if (domainRepository.isSkillInDomain(skillId, domainId)) {
            log.error("Skill already added to domain.");
            return ResponseHandler.generateResponse("Skill already added to domain.", HttpStatus.CONFLICT, null);
        }

        domain.addSkill(skill);
        domainRepository.save(domain);

        log.info("Skill added to domain successfully.");
        return ResponseHandler.generateResponse("Skill added to domain successfully.", HttpStatus.OK, domain);
    }
}