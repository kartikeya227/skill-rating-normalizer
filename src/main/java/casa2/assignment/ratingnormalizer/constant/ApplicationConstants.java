package casa2.assignment.ratingnormalizer.constant;

public class ApplicationConstants {
    // Application-Controller related constants
    public static final String APPLICATION_URL = "/application";
    public static final String HEALTH_CHECK_ENDPOINT = "/health-check";
    public static final String HEALTH_CHECK_SUCCESSFUL = "Health check successful";
    public static final String SERVICE_IS_RUNNING = "User Recommendation System microservice is running";
    public static final String CHECK_DB_CONNECTION_ENDPOINT = "/check-db-connection";
    public static final String DB_TEST_QUERY = "RETURN 1";
    public static final String DB_CONNECTION_SUCCESSFUL = "Connection to Neo4j is successful";
    public static final String DB_CONNECTION_FAILED = "Failed to connect to Neo4j: ";

    // User related constants
    public static final String USER_BASE_URL = "/user";
    public static final String CREATE_USER_URL = "/create";
    public static final String GIVE_REVIEW_URL = "/give-review";
    public static final String GET_SKILLS_URL = "/get-skills/{userId}";

    // Skill related constants
    public static final String SKILL_BASE_URL = "/skill";
    public static final String CREATE_SKILL_URL = "/create";
    public static final String LINK_SKILL_TO_SKILL_URL = "/link-to-skill";

    // Domain related constants
    public static final String DOMAIN_BASE_URL = "/domain";
    public static final String CREATE_DOMAIN_URL = "/create";
    public static final String ADD_SKILL_TO_DOMAIN_URL = "/add-skill";

}
