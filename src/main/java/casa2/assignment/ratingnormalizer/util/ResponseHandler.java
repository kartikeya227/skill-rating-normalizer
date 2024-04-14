package casa2.assignment.ratingnormalizer.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class provides utility methods for generating HTTP responses.
 */
public class ResponseHandler {

    /**
     * Generates a ResponseEntity with a given message, status, response object.
     * and includes this hash in the response.
     *
     * @param message The message to be included in the response.
     * @param status The HTTP status of the response.
     * @param responseObj The object to be included in the response.
     * @return A ResponseEntity containing the message, status, response object
     */
    public static ResponseEntity<?> generateResponse(String message, HttpStatus status, Object responseObj) {

        Map<String, Object> map = new HashMap<String, Object>();

        map.put("message", message);
        map.put("status", status.value());
        map.put("data", responseObj);

        return new ResponseEntity<Object>(map, status);
    }
}