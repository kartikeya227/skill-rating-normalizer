package casa2.assignment.ratingnormalizer.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

@Node
@Data
@Builder
public class Skill {

    @Id
    @GeneratedValue
    private Long id;

    private String skillName;

    @Relationship(type = "SUB_SKILL", direction = Relationship.Direction.OUTGOING)
    private List<Skill> subSkills;

    public void addSubSkill(Skill skill) {
        if (subSkills == null) {
            subSkills = new ArrayList<>();
        }
        subSkills.add(skill);
    }

}