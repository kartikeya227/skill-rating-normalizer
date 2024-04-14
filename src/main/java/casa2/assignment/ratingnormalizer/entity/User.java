package casa2.assignment.ratingnormalizer.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Node
@Data
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private int yearsOfExperience;
    private String currentCompanyName;

    @Relationship(type = "WORKS_IN", direction = Relationship.Direction.OUTGOING)
    private Domain domain;

}