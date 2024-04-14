package casa2.assignment.ratingnormalizer.repository;

import casa2.assignment.ratingnormalizer.entity.Domain;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for the Domain entity.
 * This interface allows to perform CRUD operations on the Domain entity.
 */
@Repository
public interface DomainRepository extends Neo4jRepository<Domain, Long> {

    /**
     * Checks if a skill is in a specific domain.
     *
     * @param skillId the Long id of the skill
     * @param domainId the Long id of the domain
     * @return true if the skill is in the domain, false otherwise
     */
    @Query("MATCH (d:Domain)-[:HAS_SKILL]->(s:Skill)-[:SUB_SKILL*0..]->(ss:Skill) WHERE id(d) = $domainId AND id(ss) = $skillId RETURN COUNT(ss) > 0")
    boolean isSkillInDomain(@Param("skillId") Long skillId, @Param("domainId") Long domainId);

    /**
     * Returns the count of common skills between two domains.
     *
     * @param domainId1 the Long id of the first domain
     * @param domainId2 the Long id of the second domain
     * @return the count of common skills between the two domains
     */
    @Query("MATCH (d1:Domain)-[:HAS_SKILL]->(s:Skill)-[:SUB_SKILL*0..]->(ss:Skill)<-[:SUB_SKILL*0..]-(s2:Skill)<-[:HAS_SKILL]-(d2:Domain) WHERE id(d1) = $domainId1 AND id(d2) = $domainId2 RETURN COUNT(DISTINCT ss)")
    int countCommonSkills(@Param("domainId1") Long domainId1, @Param("domainId2") Long domainId2);
}