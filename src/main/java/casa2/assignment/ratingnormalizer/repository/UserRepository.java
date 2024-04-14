package casa2.assignment.ratingnormalizer.repository;

import casa2.assignment.ratingnormalizer.dto.SkillDto;
import casa2.assignment.ratingnormalizer.entity.User;
import org.neo4j.driver.types.Relationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Repository interface for the User entity.
 * This interface allows to perform CRUD operations on the User entity.
 */
@Repository
public interface UserRepository extends Neo4jRepository<User, UUID> {

    /**
     * Returns the count of common skills between two users.
     *
     * @param userId1 the UUID of the first user
     * @param userId2 the UUID of the second user
     * @return the count of common skills between the two users
     */
    @Query("MATCH (u1:User)-[:KNOWS_SKILL]->(us1:UserSkill)-[:TARGET_SKILL]->(s:Skill)<-[:TARGET_SKILL]-(us2:UserSkill)<-[:KNOWS_SKILL]-(u2:User) WHERE u1.id = $userId1 AND u2.id = $userId2 RETURN COUNT(DISTINCT s)")
    int countCommonSkills(@Param("userId1") UUID userId1, @Param("userId2") UUID userId2);

    /**
     * Checks if a user has reviewed a specific skill for another user.
     *
     * @param reviewerId the UUID of the reviewer
     * @param revieweeId the UUID of the reviewee
     * @param skillId the Long id of the skill
     * @return true if the user has reviewed the specific skill for the other user, false otherwise
     */
    @Query("MATCH (r:Review)-[:REVIEWED_BY]->(reviewer:User), (r)<-[:RECEIVED_REVIEW]-(reviewee:User), (r)-[:ABOUT]->(skill:Skill) " +
            "WHERE reviewer.id = $reviewerId AND reviewee.id = $revieweeId AND id(skill) = $skillId " +
            "RETURN COUNT(r) > 0")
    boolean hasUserReviewedForSkill(@Param("reviewerId") UUID reviewerId, @Param("revieweeId") UUID revieweeId, @Param("skillId") Long skillId);

    /**
     * Counts the number of reviews a user has for a specific skill.
     *
     * @param userId the UUID of the user
     * @param skillId the Long id of the skill
     * @return the count of reviews for the user and skill
     */
    @Query("MATCH (u:User)<-[:RECEIVED_REVIEW]-(r:Review)-[:ABOUT]->(s:Skill) WHERE u.id = $userId AND id(s) = $skillId RETURN count(r)")
    int countReviewsForUserAndSkill(@Param("userId") UUID userId, @Param("skillId") Long skillId);

    @Query("MATCH (u:User)-[r:KNOWS]->(s:Skill) WHERE u.id = $userId RETURN {skillName: s.name, properties: properties(r)}")
    List<Map<String, Object>> getAllSkillsForUser(@Param("userId") UUID userId);

    /**
     * Retrieves the relationship between a user and a specific skill.
     *
     * @param userId the UUID of the user
     * @param skillId the Long id of the skill
     * @return a list of maps, each containing the skill name and properties of the relationship between the user and the skill
     */
    @Query("MATCH (u:User)-[r:KNOWS]->(s:Skill) WHERE u.id = $userId AND id(s) = $skillId RETURN {skillName: s.name, properties: properties(r)}")
    List<Map<String, Object>> getRelationshipBetweenUserAndSkill(@Param("userId") UUID userId, @Param("skillId") Long skillId);

    /**
     * Adds a review to a user.
     *
     * @param userId the UUID of the user
     * @param reviewId the Long id of the review
     */
    @Query("MATCH (u:User), (r:Review) WHERE u.id = $userId AND id(r) = $reviewId " +
            "MERGE (u)<-[rel:RECEIVED_REVIEW]-(r) " +
            "RETURN rel")
    void receivedReview(@Param("userId") UUID userId, @Param("reviewId") Long reviewId);

    /**
     * Updates or creates a relationship between a user and a skill, setting the average original and normalized ratings.
     *
     * @param skillId the Long id of the skill
     * @param userId the UUID of the user
     * @param averageOriginalRating the new average original rating
     * @param averageNormalizedRating the new average normalized rating
     */
    @Query("MATCH (u:User), (s:Skill) WHERE u.id = $userId AND id(s) = $skillId " +
            "MERGE (u)-[r:KNOWS]->(s) " +
            "SET r.averageOriginalRating = $averageOriginalRating, r.averageNormalizedRating = $averageNormalizedRating " +
            "RETURN r")
    void userKnows(@Param("skillId") Long skillId, @Param("userId") UUID userId, @Param("averageOriginalRating") Double averageOriginalRating, @Param("averageNormalizedRating") Double averageNormalizedRating);
}