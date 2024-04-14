package casa2.assignment.ratingnormalizer.controller;

import casa2.assignment.ratingnormalizer.dto.CreateReviewRecord;
import casa2.assignment.ratingnormalizer.dto.CreateUserRecord;
import casa2.assignment.ratingnormalizer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static casa2.assignment.ratingnormalizer.constant.ApplicationConstants.*;

@RestController
@RequestMapping(USER_BASE_URL)
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Creates a new user.
     * @param user The user to be created.
     * @return The created user.
     */
    @PostMapping(CREATE_USER_URL)
    public ResponseEntity<?> createUser(@RequestBody CreateUserRecord user) {
        return userService.createUser(user);
    }

    /**
     * Gives a user a skill review.
     * @param review The review to be given.
     * @return The given review.
     */
    @PostMapping(GIVE_REVIEW_URL)
    public ResponseEntity<?> giveReview(@RequestBody CreateReviewRecord review) {
        return userService.giveReview(review);
    }

    /**
     * Gets the skills of a user.
     * @param userId The ID of the user.
     * @return The skills of the user.
     */
    @GetMapping(GET_SKILLS_URL)
    public ResponseEntity<?> getSkills(@PathVariable String userId) {
        return ResponseEntity.ok(userService.getSkills(userId));
    }
}