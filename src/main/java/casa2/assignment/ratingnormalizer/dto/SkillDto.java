package casa2.assignment.ratingnormalizer.dto;

import casa2.assignment.ratingnormalizer.enums.Relevance;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SkillDto {
    private String skillName;
    private String originalRating;
    private String normalizedRating;
}
