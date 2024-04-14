package casa2.assignment.ratingnormalizer.service;

import casa2.assignment.ratingnormalizer.dto.CreateSkillRecord;
import org.springframework.http.ResponseEntity;

public interface SkillService {
    ResponseEntity<?> createSkill(CreateSkillRecord skill);

    ResponseEntity<?> linkSkillToSkill(Long parentSkillId, Long childSkillId);
}
