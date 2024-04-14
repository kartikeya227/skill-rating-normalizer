package casa2.assignment.ratingnormalizer.repository;

import casa2.assignment.ratingnormalizer.entity.Review;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReviewRepository extends Neo4jRepository<Review, Long> {
}