package casa2.assignment.ratingnormalizer.enums;

import lombok.Getter;

/**
 * Represents the relevance of a user's skill in a domain.
 * The relevance is a value between 0 and 5, where 0 means no relevance and 5 means high relevance.
 */
@Getter
public enum Relevance {
    ZERO("No Relevance"),
    ONE("Low Relevance"),
    TWO("Below Average Relevance"),
    THREE("Average Relevance"),
    FOUR("Above Average Relevance"),
    FIVE("High Relevance");

    private final String value;

    Relevance(String value) {
        this.value = value;
    }

    public static Relevance fromInteger(int x) {
        return switch (x) {
            case 0 -> ZERO;
            case 1 -> ONE;
            case 2 -> TWO;
            case 3 -> THREE;
            case 4 -> FOUR;
            case 5 -> FIVE;
            default -> null;
        };
    }
}
