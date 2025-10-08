package Users;

import org.testng.annotations.*;
import org.testng.Assert;
import io.restassured.response.Response;
import io.qameta.allure.*;
import com.github.javafaker.Faker;

import base.BaseTest;
import services.UserService;
import mdoels.request.UserRequest;
import mdoels.response.UserResponse;
import utils.APIUtils;
import utils.ExtentManager;

/**
 * Comprehensive User API Test Class
 * Demonstrates CRUD operations, validations, and error handling
 */
@Epic("User Management APIs")
@Feature("User CRUD Operations")
public class UserAPITests extends BaseTest {

    private UserService userService;
    private Faker faker;
    private int createdUserId;

    @BeforeClass
    public void setupUserTests() {
        System.out.println("\n🔧 Setting up User API Tests...");
        userService = new UserService(getRequest());
        faker = new Faker();
        System.out.println("✅ User API Tests setup completed\n");
    }

    @BeforeMethod
    public void initUserService() {
        // initialize userService after BaseTest.setupRequest() runs so requestSpec is available
        userService = new UserService(getRequest());
    }

    @Test(priority = 1)
    @Story("Get All Users")
    @Description("Verify that API returns list of all users")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllUsers() {
        System.out.println("🧪 TEST: Get All Users");
        ExtentManager.createTest("Get All Users", "Verify API returns all users");

        // Execute API call
        Response response = userService.getAllUsers();

        // Print response for debugging
        APIUtils.printResponse(response);

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 3000);

        // Validate response structure
        UserResponse[] users = userService.convertToUserResponseArray(response);
        Assert.assertTrue(users.length > 0, "Users list should not be empty");

        // Validate first user has required fields
        UserResponse firstUser = users[0];
        Assert.assertNotNull(firstUser.getId(), "User ID should not be null");
        Assert.assertNotNull(firstUser.getName(), "User name should not be null");
        Assert.assertNotNull(firstUser.getEmail(), "User email should not be null");

        System.out.println("✅ Total users found: " + users.length);
        System.out.println("✅ Test passed: Get All Users\n");
    }

    @Test(priority = 2)
    @Story("Get Single User")
    @Description("Verify that API returns specific user details")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserById() {
        System.out.println("🧪 TEST: Get User By ID");
        ExtentManager.createTest("Get User By ID", "Verify API returns specific user");

        int userId = 1;

        // Execute API call
        Response response = userService.getUserById(userId);

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 2000);

        // Validate response data
        UserResponse user = userService.convertToUserResponse(response);
        Assert.assertEquals(user.getId(), userId, "User ID should match requested ID");
        Assert.assertNotNull(user.getName(), "User name should not be null");
        Assert.assertTrue(user.getEmail().contains("@"), "Email should contain @ symbol");

        System.out.println("✅ User retrieved: " + user.getName());
        System.out.println("✅ Test passed: Get User By ID\n");
    }

    @Test(priority = 3, dataProvider = "userData")
    @Story("Create User")
    @Description("Verify that API can create new users with valid data")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser(String name, String username, String email) {
        System.out.println("🧪 TEST: Create User - " + name);
        ExtentManager.createTest("Create User", "Verify user creation with data: " + name);

        // Prepare test data
        UserRequest userRequest = new UserRequest();
        userRequest.setName(name);
        userRequest.setUsername(username);
        userRequest.setEmail(email);
        userRequest.setPhone(faker.phoneNumber().phoneNumber());
        userRequest.setWebsite(faker.internet().url());

        // Execute API call
        Response response = userService.createUser(userRequest);

        // Print response for debugging
        APIUtils.printResponse(response);

        // Validations
        APIUtils.validateStatusCode(response, 201);
        APIUtils.validateResponseTime(response, 3000);

        // Validate response data
        UserResponse createdUser = userService.convertToUserResponse(response);
        Assert.assertNotNull(createdUser.getId(), "Created user should have ID");
        Assert.assertEquals(createdUser.getName(), name, "Name should match input");
        Assert.assertEquals(createdUser.getEmail(), email, "Email should match input");

        // Store created user ID for later tests
        if (createdUserId == 0) {
            createdUserId = createdUser.getId();
        }

        System.out.println("✅ User created with ID: " + createdUser.getId());
        System.out.println("✅ Test passed: Create User\n");
    }

    @Test(priority = 4, dependsOnMethods = "testCreateUser")
    @Story("Update User")
    @Description("Verify that API can update existing user data")
    @Severity(SeverityLevel.NORMAL)
    public void testUpdateUser() {
        System.out.println("🧪 TEST: Update User");
        ExtentManager.createTest("Update User", "Verify user update functionality");

        int userIdToUpdate = 1; // Using existing user from JSONPlaceholder

        // Prepare updated data
        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Updated " + faker.name().fullName());
        updateRequest.setUsername("updated_" + faker.name().username());
        updateRequest.setEmail("updated." + faker.internet().emailAddress());
        updateRequest.setPhone(faker.phoneNumber().phoneNumber());

        // Execute API call
        Response response = userService.updateUser(userIdToUpdate, updateRequest);

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 3000);

        // Validate response data
        UserResponse updatedUser = userService.convertToUserResponse(response);
        Assert.assertEquals(updatedUser.getId(), userIdToUpdate, "User ID should remain same");
        Assert.assertEquals(updatedUser.getName(), updateRequest.getName(), "Name should be updated");
        Assert.assertEquals(updatedUser.getEmail(), updateRequest.getEmail(), "Email should be updated");

        System.out.println("✅ User updated: " + updatedUser.getName());
        System.out.println("✅ Test passed: Update User\n");
    }

    @Test(priority = 5)
    @Story("Delete User")
    @Description("Verify that API can delete existing users")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteUser() {
        System.out.println("🧪 TEST: Delete User");
        ExtentManager.createTest("Delete User", "Verify user deletion functionality");

        int userIdToDelete = 1; // Using existing user from JSONPlaceholder

        // Execute API call
        Response response = userService.deleteUser(userIdToDelete);

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 2000);

        System.out.println("✅ User deleted successfully");
        System.out.println("✅ Test passed: Delete User\n");
    }

    @Test(priority = 6)
    @Story("Error Handling")
    @Description("Verify API handles invalid user ID gracefully")
    @Severity(SeverityLevel.MINOR)
    public void testGetUserNotFound() {
        System.out.println("🧪 TEST: Get Non-Existent User");
        ExtentManager.createTest("Get Non-Existent User", "Verify 404 error handling");

        int invalidUserId = 999999;

        // Execute API call
        Response response = userService.getUserById(invalidUserId);

        // Print response for debugging
        APIUtils.printResponse(response);

        // Validations
        APIUtils.validateStatusCode(response, 404);
        APIUtils.validateResponseTime(response, 2000);

        System.out.println("✅ API correctly returned 404 for invalid user ID");
        System.out.println("✅ Test passed: Error Handling\n");
    }

    @Test(priority = 7)
    @Story("Data Validation")
    @Description("Verify API response contains expected user fields")
    @Severity(SeverityLevel.NORMAL)
    public void testUserDataValidation() {
        System.out.println("🧪 TEST: User Data Validation");
        ExtentManager.createTest("User Data Validation", "Verify user data structure");

        // Get a specific user
        Response response = userService.getUserById(2);
        APIUtils.validateStatusCode(response, 200);

        // Convert to object and validate all fields
        UserResponse user = userService.convertToUserResponse(response);

        // Comprehensive validation
        Assert.assertTrue(user.getId() > 0, "User ID should be positive");
        Assert.assertNotNull(user.getName(), "Name should not be null");
        Assert.assertFalse(user.getName().isEmpty(), "Name should not be empty");
        Assert.assertTrue(user.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.[A-Za-z]{2,}$"), 
            "Email should be valid format");
        Assert.assertNotNull(user.getUsername(), "Username should not be null");

        System.out.println("✅ User data validation passed for: " + user.getName());
        System.out.println("✅ Test passed: Data Validation\n");
    }

    /**
     * Data Provider for user creation tests
     */
    @DataProvider(name = "userData")
    public Object[][] getUserTestData() {
        return new Object[][]{
            {faker.name().fullName(), faker.name().username(), faker.internet().emailAddress()},
            {faker.name().fullName(), faker.name().username(), faker.internet().emailAddress()},
            {faker.name().fullName(), faker.name().username(), faker.internet().emailAddress()}
        };
    }

    @AfterClass
    public void tearDownUserTests() {
        System.out.println("🧹 Cleaning up User API Tests...");
        ExtentManager.flushReports();
        System.out.println("✅ User API Tests cleanup completed");
        System.out.println("📊 Reports generated in: target/extent-reports/");
        System.out.println("🎯 All User API Tests Completed Successfully!\n");
    }
}
