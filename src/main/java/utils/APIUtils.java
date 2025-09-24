package utils;


import io.restassured.response.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.Assert;

/**
 * Utility class for common API operations
 */
public class APIUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Convert object to JSON string
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    /**
     * Convert JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert JSON to object", e);
        }
    }

    /**
     * Extract value from JSON response
     */
    public static String extractValue(Response response, String jsonPath) {
        try {
            return response.jsonPath().getString(jsonPath);
        } catch (Exception e) {
            System.err.println("Failed to extract value from path: " + jsonPath);
            return null;
        }
    }

    /**
     * Validate status code
     */
    public static void validateStatusCode(Response response, int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        System.out.println("📊 Response Status: " + actualStatusCode);

        Assert.assertEquals(actualStatusCode, expectedStatusCode, 
            "Status code mismatch. Expected: " + expectedStatusCode + ", Actual: " + actualStatusCode);
    }

    /**
     * Validate response time
     */
    public static void validateResponseTime(Response response, long maxTimeInMs) {
        long responseTime = response.getTime();
        System.out.println("⏱️ Response Time: " + responseTime + "ms");

        Assert.assertTrue(responseTime <= maxTimeInMs, 
            "Response time exceeded limit. Expected: <= " + maxTimeInMs + "ms, Actual: " + responseTime + "ms");
    }

    /**
     * Print response details for debugging
     */
    public static void printResponse(Response response) {
        System.out.println("\n📋 Response Details:");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Response Time: " + response.getTime() + "ms");
        System.out.println("Content Type: " + response.getContentType());
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("====================================\n");
    }
}