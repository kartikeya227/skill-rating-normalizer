package casa2.assignment.ratingnormalizer.dto;

public record CreateUserRecord(
        String name,
        int yearsOfExperience,
        String currentCompanyName,
        Long domainId) {}