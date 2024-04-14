package casa2.assignment.ratingnormalizer.dto;

public record CreateReviewRecord(
        String reviewerUserId,
        String revieweeUserId,
        int rating,
        String comment,
        Long skillId,
        int yearsWorkedTogether) {
}