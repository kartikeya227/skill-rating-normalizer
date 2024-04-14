package casa2.assignment.ratingnormalizer.controller;

import casa2.assignment.ratingnormalizer.dto.CreateSkillRecord;
import casa2.assignment.ratingnormalizer.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static casa2.assignment.ratingnormalizer.constant.ApplicationConstants.*;

@RestController
@RequestMapping(SKILL_BASE_URL)
public class SkillController {



    @Autowired
    private SkillService skillService;

    @PostMapping(CREATE_SKILL_URL)
    public ResponseEntity<?> createSkill(@RequestBody CreateSkillRecord skill) {
        return skillService.createSkill(skill);
    }

    @PostMapping(LINK_SKILL_TO_SKILL_URL)
    public ResponseEntity<?> linkSkillToSkill(@RequestParam Long parentSkillId, @RequestParam Long childSkillId) {
        return skillService.linkSkillToSkill(parentSkillId, childSkillId);
    }
}