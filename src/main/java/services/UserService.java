package services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.qameta.allure.Step;
import mdoels.request.UserRequest;
import mdoels.response.UserResponse;
import utils.APIUtils;

/**
 * User Service class for User API operations
 * Follows Page Object Model pattern for API testing
 */
public class UserService {

    private static final String USERS_ENDPOINT = "/users";

    private RequestSpecification requestSpec;

    public UserService(RequestSpecification requestSpec) {
        this.requestSpec = requestSpec;
    }

    /**
     * Get all users
     */
    @Step("Get all users from API")
    public Response getAllUsers() {
        System.out.println("🔍 Getting all users...");

        Response response = requestSpec
            .when()
                .get(USERS_ENDPOINT)
            .then()
                .extract()
                .response();

        System.out.println("✅ Retrieved users list");
        return response;
    }

    /**
     * Get user by ID
     */
    @Step("Get user by ID: {userId}")
    public Response getUserById(int userId) {
        System.out.println("🔍 Getting user with ID: " + userId);

        Response response = requestSpec
            .pathParam("userId", userId)
            .when()
                .get(USERS_ENDPOINT + "/{userId}")
            .then()
                .extract()
                .response();

        System.out.println("✅ Retrieved user: " + userId);
        return response;
    }

    /**
     * Create new user
     */
    @Step("Create new user: {userRequest}")
    public Response createUser(UserRequest userRequest) {
        System.out.println("📝 Creating new user: " + userRequest.getName());

        Response response = requestSpec
            .body(APIUtils.toJson(userRequest))
            .when()
                .post(USERS_ENDPOINT)
            .then()
                .extract()
                .response();

        System.out.println("✅ User created successfully");
        return response;
    }

    /**
     * Update user
     */
    @Step("Update user {userId} with data: {userRequest}")
    public Response updateUser(int userId, UserRequest userRequest) {
        System.out.println("✏️ Updating user: " + userId);

        Response response = requestSpec
            .pathParam("userId", userId)
            .body(APIUtils.toJson(userRequest))
            .when()
                .put(USERS_ENDPOINT + "/{userId}")
            .then()
                .extract()
                .response();

        System.out.println("✅ User updated successfully");
        return response;
    }

    /**
     * Delete user
     */
    @Step("Delete user with ID: {userId}")
    public Response deleteUser(int userId) {
        System.out.println("🗑️ Deleting user: " + userId);

        Response response = requestSpec
            .pathParam("userId", userId)
            .when()
                .delete(USERS_ENDPOINT + "/{userId}")
            .then()
                .extract()
                .response();

        System.out.println("✅ User deleted successfully");
        return response;
    }

    /**
     * Convert response to UserResponse object
     */
    public UserResponse convertToUserResponse(Response response) {
        return APIUtils.fromJson(response.asString(), UserResponse.class);
    }

    /**
     * Convert response to UserResponse array
     */
    public UserResponse[] convertToUserResponseArray(Response response) {
        return APIUtils.fromJson(response.asString(), UserResponse[].class);
    }
}
