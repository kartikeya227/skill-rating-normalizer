package casa2.assignment.ratingnormalizer.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a domain in the system.
 * Each domain has a list of skills, represented by Skill entities.
 * The HAS_SKILL (edge) relationship type is used to denote the relationship between the domain and its skills.
 */
@Node
@Data
@Builder
public class Domain {

    @Id
    @GeneratedValue
    private Long id;

    private String domainName;

    @Relationship(type = "HAS_SKILL", direction = Relationship.Direction.OUTGOING)
    private List<Skill> skills;

    public void addSkill(Skill skill) {
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        this.skills.add(skill);
    }
}