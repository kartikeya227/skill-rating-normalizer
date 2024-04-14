package casa2.assignment.ratingnormalizer.configuration;

import org.neo4j.ogm.config.ClasspathConfigurationSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.core.transaction.Neo4jTransactionManager;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableNeo4jRepositories(basePackages = "casa2.assignment.ratingnormalizer.repository")
@EnableTransactionManagement
public class MyConfiguration {

}