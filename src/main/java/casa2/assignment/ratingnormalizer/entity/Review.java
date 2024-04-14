package casa2.assignment.ratingnormalizer.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

/**
 * Represents a review in the system.
 * Each review has an original rating, a normalized rating, a comment, and the number of years the reviewer and reviewee worked together.
 * Each review is associated (edges) with a reviewer (User), a reviewee (User), and a skill.
 */
@Node
@Data
@Builder
public class Review {

    @Id
    @GeneratedValue
    private Long id;

    private final int originalRating;
    private final int normalizedRating;
    private final String comment;
    private final int yearsWorkedTogether;

    @Relationship(type = "REVIEWED_BY", direction = Relationship.Direction.OUTGOING)
    private User reviewer;

    @Relationship(type = "ABOUT", direction = Relationship.Direction.OUTGOING)
    private Skill skill;

}