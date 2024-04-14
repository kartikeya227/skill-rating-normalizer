package casa2.assignment.ratingnormalizer.controller;

import casa2.assignment.ratingnormalizer.util.ResponseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static casa2.assignment.ratingnormalizer.constant.ApplicationConstants.*;


/**
 * The ApplicationController is a REST controller that handles requests at the "/application" URL.
 * It provides a health check endpoint to verify the service is running.
 */
@RestController
@RequestMapping(APPLICATION_URL)
public class ApplicationController {

    @Autowired
    private Neo4jClient neo4jClient;

    /**
     * The healthCheck method is a GET endpoint at "/health-check".
     * It uses the ResponseHandler class to generate a response indicating that the service is running.
     *
     * @return A ResponseEntity containing the message, status, and a hash of the message, status, and response object.
     */
    @GetMapping(HEALTH_CHECK_ENDPOINT)
    public ResponseEntity<?> healthCheck() {
        return ResponseHandler.generateResponse(HEALTH_CHECK_SUCCESSFUL, HttpStatus.OK, SERVICE_IS_RUNNING);
    }

    /**
     * This method is a GET endpoint at "/check-db-connection".
     * It checks the connection to the Neo4j database by executing a simple query.
     * If the query is successful, it returns a 200 OK response with a success message.
     * If the query fails, it catches the exception and returns a 500 Internal Server Error response with an error message.
     *
     * @return A ResponseEntity containing the status of the database connection.
     */
    @GetMapping(CHECK_DB_CONNECTION_ENDPOINT)
    public ResponseEntity<?> checkDbConnection() {
        try {
            neo4jClient.query(DB_TEST_QUERY).fetch().one();
            return ResponseHandler.generateResponse(DB_CONNECTION_SUCCESSFUL, HttpStatus.OK, SERVICE_IS_RUNNING);
        } catch (Exception e) {
            return ResponseHandler.generateResponse(DB_CONNECTION_FAILED, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}