package casa2.assignment.ratingnormalizer.util.strategies;

import casa2.assignment.ratingnormalizer.dto.CreateReviewRecord;
import casa2.assignment.ratingnormalizer.entity.Domain;
import casa2.assignment.ratingnormalizer.entity.User;
import casa2.assignment.ratingnormalizer.repository.DomainRepository;
import casa2.assignment.ratingnormalizer.repository.UserRepository;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * This class implements the RatingNormalizationStrategy interface and provides a custom strategy for normalizing ratings.
 * It uses various factors such as years worked together, domain, skill, common skills, and experience to normalize the rating.
 */
@Service
public class CustomRatingNormalizationStrategy implements RatingNormalizationStrategy {
    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * This method normalizes the rating based on various factors.
     * @param reviewer The user who is reviewing.
     * @param reviewee The user who is being reviewed.
     * @param review The review record.
     * @return The normalized rating.
     */
    @Override
    public double normalizeRating(User reviewer, User reviewee, CreateReviewRecord review) {
        int yearsWorkedTogetherFactor = calculateYearsWorkedTogetherFactor(review.yearsWorkedTogether());
        int domainFactor = calculateFieldFactor(reviewer.getDomain(), reviewee.getDomain());
        int skillFactor = calculateReviewerSkillFactor(reviewer, review);
        int commonSkillsFactor = calculateCommonSkillsFactor(reviewer, reviewee);
        int experienceFactor = calculateExperienceFactor(reviewer.getYearsOfExperience(), reviewee.getYearsOfExperience());

        double totalFactor = yearsWorkedTogetherFactor + domainFactor + skillFactor + commonSkillsFactor + experienceFactor;
        double averageFactor = totalFactor / 5.0;

        return (review.rating() * (averageFactor / 100.0));
    }

    /**
     * This method calculates the years worked together factor.
     * @param yearsWorkedTogether The number of years the reviewer and reviewee have worked together.
     * @return The years worked together factor.
     */
    private int calculateYearsWorkedTogetherFactor(int yearsWorkedTogether) {
        if (yearsWorkedTogether == 0) {
            return 0; // return a minimum factor if they haven't worked together
        } else if (yearsWorkedTogether == 1) {
            return 20;
        } else if (yearsWorkedTogether == 2) {
            return 40;
        } else if (yearsWorkedTogether >= 3 && yearsWorkedTogether <= 6) {
            return 75;
        } else if (yearsWorkedTogether > 6 && yearsWorkedTogether <= 10) {
            return 80;
        } else if (yearsWorkedTogether > 10 && yearsWorkedTogether <= 15) {
            return 90;
        } else if (yearsWorkedTogether > 15 && yearsWorkedTogether <= 25) {
            return 95;
        } else {
            return 100; // for yearsWorkedTogether > 25
        }
    }

    /**
     * This method calculates the field factor based on the domains of the reviewer and reviewee.
     * @param reviewerDomain The domain of the reviewer.
     * @param revieweeDomain The domain of the reviewee.
     * @return The field factor.
     */
    private int calculateFieldFactor(Domain reviewerDomain, Domain revieweeDomain) {
        if (reviewerDomain.equals(revieweeDomain)) {
            return 100;
        } else {
            // Fetch count of common skills between two domains
            int count = domainRepository.countCommonSkills(reviewerDomain.getId(), revieweeDomain.getId());
            if (count == 0) {
                return 0;
            } else if (count == 1) {
                return 10;
            } else if (count == 2) {
                return 20;
            } else if (count == 3) {
                return 40;
            } else if (count == 4) {
                return 50;
            } else {
                return 60; // for count > 4
            }
        }
    }

    /**
     * This method calculates the reviewer skill factor based on the relevance of the skill being reviewed.
     * @param reviewer The user who is reviewing.
     * @param review The review record.
     * @return The reviewer skill factor.
     */
    private int calculateReviewerSkillFactor(User reviewer, CreateReviewRecord review) {
        List<Map<String, Object>> skillMap = userRepository.getRelationshipBetweenUserAndSkill(reviewer.getId(), review.skillId());
        if (skillMap.isEmpty()) {
            return 0; // return a minimum factor if the user doesn't have the skill
        }
        Map<String, Object> relationshipProperties = (Map<String, Object>) skillMap.getFirst().get("properties");

        double relevance = relationshipProperties != null ? (double) relationshipProperties.getOrDefault("averageNormalizedRating", 0.0) : 0.0;
        return (int) Math.min(relevance * 20, 100);
    }

    /**
     * This method calculates the common skills factor based on the number of common skills between the reviewer and reviewee.
     * @param reviewer The user who is reviewing.
     * @param reviewee The user who is being reviewed.
     * @return The common skills factor.
     */
    private int calculateCommonSkillsFactor(User reviewer, User reviewee) {
        int commonSkillsCount = userRepository.countCommonSkills(reviewer.getId(), reviewee.getId());

        return Math.min(commonSkillsCount * 10, 100);
    }

    /**
     * This method calculates the experience factor based on the difference in experience between the reviewer and reviewee.
     * @param reviewerExperience The years of experience of the reviewer.
     * @param revieweeExperience The years of experience of the reviewee.
     * @return The experience factor.
     */
    private int calculateExperienceFactor(int reviewerExperience, int revieweeExperience) {
        int experienceDifference = reviewerExperience - revieweeExperience;

        if (experienceDifference == 0) {
            return 50; // mid factor
        } else if (experienceDifference > 0) {
            if (experienceDifference <= 5) {
                return 60; // slightly higher factor
            } else if (experienceDifference <= 10) {
                return 70; // even higher factor
            } else if (experienceDifference <= 15) {
                return 80; // high factor
            } else if (experienceDifference <= 20) {
                return 90; // very high factor
            } else {
                return 100; // maximum factor
            }
        } else {
            if (experienceDifference >= -5) {
                return 40; // slightly lower factor
            } else if (experienceDifference >= -10) {
                return 30; // even lower factor
            } else if (experienceDifference >= -15) {
                return 20; // low factor
            } else if (experienceDifference >= -20) {
                return 10; // very low factor
            } else {
                return 0; // minimum factor
            }
        }
    }
}