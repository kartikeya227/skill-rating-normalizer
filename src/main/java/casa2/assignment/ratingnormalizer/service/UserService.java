package casa2.assignment.ratingnormalizer.service;

import casa2.assignment.ratingnormalizer.dto.CreateReviewRecord;
import casa2.assignment.ratingnormalizer.dto.CreateUserRecord;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> getSkills(String userId);

    ResponseEntity<?> createUser(CreateUserRecord user);

    ResponseEntity<?> giveReview(CreateReviewRecord review);
}
