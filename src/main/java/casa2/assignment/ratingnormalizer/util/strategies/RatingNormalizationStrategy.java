package casa2.assignment.ratingnormalizer.util.strategies;

import casa2.assignment.ratingnormalizer.dto.CreateReviewRecord;
import casa2.assignment.ratingnormalizer.entity.User;

public interface RatingNormalizationStrategy {
    double normalizeRating(User reviewer, User reviewee, CreateReviewRecord review);
}