package casa2.assignment.ratingnormalizer.mapper;

import casa2.assignment.ratingnormalizer.dto.SkillDto;
import casa2.assignment.ratingnormalizer.enums.Relevance;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * SkillMapper is a utility class that provides a method to map a list of UserSkill objects to a list of SkillDto objects.
 */
public class SkillMapper {

    /**
     * This method takes a list of UserSkill objects and maps it to a list of SkillDto objects.
     * Each UserSkill object is converted to a SkillDto object where:
     * - skillName is the name of the skill from the UserSkill object
     * - originalRating is the original rating from the UserSkill object, converted to a Relevance enum
     * - normalizedRating is the normalized rating from the UserSkill object, converted to a Relevance enum
     *
     * @param skills the list of UserSkill objects to be converted
     * @return a list of SkillDto objects
     */
    public static List<SkillDto> mapToSkillDtoList(List<Map<String, Object>> skills) {
        return skills.stream()
                .map(skill -> {
                    String skillName = (String) skill.get("skillName");
                    Map<String, Object> properties = (Map<String, Object>) skill.get("properties");
                    Double averageOriginalRating = (Double) properties.get("averageOriginalRating");
                    Double averageNormalizedRating = (Double) properties.get("averageNormalizedRating");

                    return SkillDto
                            .builder()
                            .skillName(skillName)
                            .originalRating(Relevance.fromInteger((int) Math.round(averageOriginalRating)).getValue())
                            .normalizedRating(Relevance.fromInteger((int) Math.round(averageNormalizedRating)).getValue())
                            .build();
                })
                .collect(Collectors.toList());
    }
}