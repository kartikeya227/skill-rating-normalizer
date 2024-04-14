package casa2.assignment.ratingnormalizer.repository;

import casa2.assignment.ratingnormalizer.entity.Skill;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the Skill entity.
 * This interface allows to perform CRUD operations on the Skill entity.
 */
@Repository
public interface SkillRepository extends Neo4jRepository<Skill, Long> {

    /**
     * Checks if a skill is a sub-skill of another skill.
     *
     * @param parentId the Long id of the parent skill
     * @param childId the Long id of the child skill
     * @return true if the child skill is a sub-skill of the parent skill, false otherwise
     */
    @Query("MATCH (p:Skill)-[:SUB_SKILL*0..]->(c:Skill) WHERE id(p) = $parentId AND id(c) = $childId RETURN COUNT(c) > 0")
    boolean isSubSkill(@Param("parentId") Long parentId, @Param("childId") Long childId);

}