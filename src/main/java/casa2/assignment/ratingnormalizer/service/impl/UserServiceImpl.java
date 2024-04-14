package casa2.assignment.ratingnormalizer.service.impl;

import casa2.assignment.ratingnormalizer.dto.CreateReviewRecord;
import casa2.assignment.ratingnormalizer.dto.CreateUserRecord;
import casa2.assignment.ratingnormalizer.dto.SkillDto;
import casa2.assignment.ratingnormalizer.entity.Domain;
import casa2.assignment.ratingnormalizer.entity.Review;
import casa2.assignment.ratingnormalizer.entity.Skill;
import casa2.assignment.ratingnormalizer.entity.User;
import casa2.assignment.ratingnormalizer.mapper.SkillMapper;
import casa2.assignment.ratingnormalizer.repository.DomainRepository;
import casa2.assignment.ratingnormalizer.repository.ReviewRepository;
import casa2.assignment.ratingnormalizer.repository.SkillRepository;
import casa2.assignment.ratingnormalizer.repository.UserRepository;
import casa2.assignment.ratingnormalizer.service.UserService;
import casa2.assignment.ratingnormalizer.util.ResponseHandler;
import casa2.assignment.ratingnormalizer.util.strategies.RatingNormalizationStrategy;
import lombok.extern.slf4j.Slf4j;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Service implementation for the User entity.
 * This class provides implementations for the methods declared in the UserService interface.
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingNormalizationStrategy ratingNormalizationStrategy;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private SkillRepository skillRepository;


    /**
     * Retrieves the skills of a user.
     *
     * @param userId the id of the user.
     * @return the response entity with the user's skills.
     */
    @Override
    public ResponseEntity<?> getSkills(String userId) {
        log.info("Request to retrieve user skills for user with id: {}", userId);

        // Check if the user exists
        if (!userRepository.existsById(UUID.fromString(userId))) {
            log.error("User not found.");
            return ResponseHandler.generateResponse("User not found.", HttpStatus.NOT_FOUND, null);
        }

        // Get all skills for the user
        List<Map<String, Object>> skillMap = userRepository.getAllSkillsForUser(UUID.fromString(userId));
        List<SkillDto> skills = SkillMapper.mapToSkillDtoList(skillMap);
        return ResponseHandler.generateResponse("User skills retrieved successfully.", HttpStatus.OK, skills);
    }

    /**
     * Creates a new user.
     *
     * @param userRecord the user record to be created.
     * @return the response entity with the created user.
     */
    @Override
    public ResponseEntity<?> createUser(CreateUserRecord userRecord) {
        log.info("Request to create a new user.");
        // Check if domain exists
        Domain domain = domainRepository.findById(userRecord.domainId()).orElse(null);
        if (domain == null) {
            log.error("Domain not found.");
            return ResponseHandler.generateResponse("Domain not found.", HttpStatus.NOT_FOUND, null);
        }

        User user = User
                .builder()
                .name(userRecord.name())
                .currentCompanyName(userRecord.currentCompanyName())
                .yearsOfExperience(userRecord.yearsOfExperience())
                .domain(domain)
                .build();

        userRepository.save(user);

        log.info("User created successfully.");
        return ResponseHandler.generateResponse("User created successfully.", HttpStatus.CREATED, user);
    }

    /**
     * Allows a user to give a review.
     *
     * @param reviewRecord the review record to be created.
     * @return the response entity.
     */
    @Override
    @Transactional("transactionManager")
    public ResponseEntity<?> giveReview(CreateReviewRecord reviewRecord) {
        log.info("Request to give a review.");

        // Check if the reviewee and reviewer are the same
        if (reviewRecord.reviewerUserId().equals(reviewRecord.revieweeUserId())) {
            log.error("Reviewer and reviewee cannot be the same user.");
            return ResponseHandler.generateResponse("Reviewer and reviewee cannot be the same user.", HttpStatus.BAD_REQUEST, null);
        }

        // Check if the user (reviewer) exists
        User reviewer = userRepository.findById(UUID.fromString(reviewRecord.reviewerUserId())).orElse(null);
        if (reviewer == null) {
            log.error("Reviewer not found.");
            return ResponseHandler.generateResponse("Reviewer not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if the user (reviewee) exists
        User reviewee = userRepository.findById(UUID.fromString(reviewRecord.revieweeUserId())).orElse(null);
        if (reviewee == null) {
            log.error("Reviewee not found.");
            return ResponseHandler.generateResponse("Reviewee not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if the skill exists
        Skill skill = skillRepository.findById(reviewRecord.skillId()).orElse(null);
        if (skill == null) {
            log.error("Skill not found.");
            return ResponseHandler.generateResponse("Skill not found.", HttpStatus.NOT_FOUND, null);
        }

        // Check if the user has already reviewed the reviewee for the given skill
        if (userRepository.hasUserReviewedForSkill(reviewer.getId(), reviewee.getId(), reviewRecord.skillId())) {
            log.error("Reviewer has already reviewed the reviewee for the given skill.");
            return ResponseHandler.generateResponse("Reviewer has already reviewed the reviewee for the given skill.", HttpStatus.BAD_REQUEST, null);
        }

        // Normalize the rating
        double normalizedRating = ratingNormalizationStrategy.normalizeRating(reviewer, reviewee, reviewRecord);

        // Create the review
        Review review = Review
                .builder()
                .reviewer(reviewer)
                .comment(reviewRecord.comment())
                .yearsWorkedTogether(reviewRecord.yearsWorkedTogether())
                .originalRating(reviewRecord.rating())
                .normalizedRating((int) Math.round(normalizedRating))
                .skill(skill)
                .build();

        // Save the review first
        final Review savedReview = reviewRepository.save(review);

        // Get current rating for the skill
        List<Map<String, Object>> skillMap = userRepository.getRelationshipBetweenUserAndSkill(reviewee.getId(), reviewRecord.skillId());

        if (!skillMap.isEmpty()) {
            Map<String, Object> relationshipProperties = (Map<String, Object>) skillMap.getFirst().get("properties");

            // Get count of reviews for the skill
            int reviewCount = userRepository.countReviewsForUserAndSkill(reviewee.getId(), reviewRecord.skillId());
            Double averageOriginalRating = (Double) relationshipProperties.get("averageOriginalRating");
            Double averageNormalizedRating = (Double) relationshipProperties.get("averageNormalizedRating");
            // Calculate new average rating
            double newAverageOriginalRating = ((averageOriginalRating * reviewCount) + reviewRecord.rating()) / (reviewCount + 1);
            double newAverageNormalizedRating = ((averageNormalizedRating * reviewCount) + normalizedRating) / (reviewCount + 1);
            // Update edge between user and skill
            userRepository.userKnows(reviewRecord.skillId(), reviewee.getId(), newAverageOriginalRating, newAverageNormalizedRating);
        } else {
            // Create edge between user and skill
            userRepository.userKnows(reviewRecord.skillId(), reviewee.getId(), (double) reviewRecord.rating(), normalizedRating);
        }

        // Add the review to the user
        userRepository.receivedReview(reviewee.getId(), savedReview.getId());

        log.info("Review created successfully.");
        return ResponseHandler.generateResponse("Review created successfully.", HttpStatus.CREATED, null);
    }
}