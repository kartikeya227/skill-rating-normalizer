package casa2.assignment.ratingnormalizer.service.impl;

import casa2.assignment.ratingnormalizer.dto.CreateSkillRecord;
import casa2.assignment.ratingnormalizer.entity.Skill;
import casa2.assignment.ratingnormalizer.repository.SkillRepository;
import casa2.assignment.ratingnormalizer.service.SkillService;
import casa2.assignment.ratingnormalizer.util.ResponseHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Service implementation for the Skill entity.
 * This class provides implementations for the methods declared in the SkillService interface.
 */
@Service
@Slf4j
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    /**
     * Creates a new skill.
     *
     * @param skillRecord the skill record to be created.
     * @return the response entity with the created skill.
     */
    @Override
    public ResponseEntity<?> createSkill(CreateSkillRecord skillRecord) {
        log.info("Request to create skill: {}", skillRecord.skillName());

        Skill skill = Skill
                .builder()
                .skillName(skillRecord.skillName())
                .build();

        skillRepository.save(skill);

        log.info("Skill created successfully.");
        return ResponseHandler.generateResponse("Skill created successfully.", HttpStatus.CREATED, skill);
    }

    /**
     * Links a skill to another skill.
     *
     * @param parentSkillId the id of the parent skill.
     * @param childSkillId  the id of the child skill.
     * @return the response entity with the updated parent skill.
     */
    @Override
    public ResponseEntity<?> linkSkillToSkill(Long parentSkillId, Long childSkillId) {
        log.info("Request to link skill with id: {} to skill with id: {}", parentSkillId, childSkillId);

        // Check if the parent skill is the same as the child skill
        if (parentSkillId.equals(childSkillId)) {
            log.error("Parent skill cannot be the same as the child skill.");
            return ResponseHandler.generateResponse("Parent skill cannot be the same as the child skill.", HttpStatus.BAD_REQUEST, null);
        }

        // Check if parent skill exists
        Skill parentSkill = skillRepository.findById(parentSkillId).orElse(null);
        if (parentSkill == null) {
            log.error("Parent skill not found.");
            return ResponseHandler.generateResponse("Parent skill not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if child skill exists
        Skill childSkill = skillRepository.findById(childSkillId).orElse(null);
        if (childSkill == null) {
            log.error("Child skill not found.");
            return ResponseHandler.generateResponse("Child skill not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if the child skill is already a sub-skill of the parent skill
        if (skillRepository.isSubSkill(parentSkillId, childSkillId)) {
            log.error("Child skill is already a sub-skill of the parent skill.");
            return ResponseHandler.generateResponse("Child skill is already a sub-skill of the parent skill.", HttpStatus.BAD_REQUEST, null);
        }

        parentSkill.addSubSkill(childSkill);
        skillRepository.save(parentSkill);

        log.info("Skill linked successfully.");
        return ResponseHandler.generateResponse("Skill linked successfully.", HttpStatus.OK, parentSkill);
    }
}