package Users;


import org.testng.annotations.*;
import org.testng.Assert;
import io.restassured.response.Response;
import io.qameta.allure.*;
import com.github.javafaker.Faker;

import base.BaseTest;
import utils.APIUtils;

/**
 * Posts API Test Class
 * Demonstrates additional API testing scenarios
 */
@Epic("Content Management APIs")
@Feature("Posts Operations")
public class PostsAPITests extends BaseTest {

    private Faker faker;

    @BeforeClass
    public void setupPostsTests() {
        System.out.println("\n🔧 Setting up Posts API Tests...");
        faker = new Faker();
        System.out.println("✅ Posts API Tests setup completed\n");
    }

    @Test(priority = 1)
    @Story("Get All Posts")
    @Description("Verify that API returns list of all posts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetAllPosts() {
        System.out.println("🧪 TEST: Get All Posts");

        Response response = getRequest()
            .when()
                .get("/posts")
            .then()
                .extract()
                .response();

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 3000);

        // Validate response is array and not empty
        Assert.assertTrue(response.jsonPath().getList("$").size() > 0, 
            "Posts list should not be empty");

        // Validate first post structure
        Assert.assertNotNull(response.jsonPath().get("[0].id"), "Post should have ID");
        Assert.assertNotNull(response.jsonPath().get("[0].title"), "Post should have title");
        Assert.assertNotNull(response.jsonPath().get("[0].body"), "Post should have body");
        Assert.assertNotNull(response.jsonPath().get("[0].userId"), "Post should have userId");

        int postsCount = response.jsonPath().getList("$").size();
        System.out.println("✅ Total posts found: " + postsCount);
        System.out.println("✅ Test passed: Get All Posts\n");
    }

    @Test(priority = 2)
    @Story("Get Post by ID")
    @Description("Verify that API returns specific post details")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetPostById() {
        System.out.println("🧪 TEST: Get Post By ID");

        int postId = 1;

        Response response = getRequest()
            .pathParam("postId", postId)
            .when()
                .get("/posts/{postId}")
            .then()
                .extract()
                .response();

        APIUtils.printResponse(response);

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 2000);

        // Validate response data
        Assert.assertEquals(response.jsonPath().getInt("id"), postId, 
            "Post ID should match requested ID");
        Assert.assertNotNull(response.jsonPath().getString("title"), 
            "Post title should not be null");
        Assert.assertTrue(response.jsonPath().getString("body").length() > 0, 
            "Post body should not be empty");

        String postTitle = response.jsonPath().getString("title");
        System.out.println("✅ Post retrieved: " + postTitle);
        System.out.println("✅ Test passed: Get Post By ID\n");
    }

    @Test(priority = 3)
    @Story("Create Post")
    @Description("Verify that API can create new posts")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreatePost() {
        System.out.println("🧪 TEST: Create Post");

        // Prepare test data
        String title = faker.lorem().sentence(4);
        String body = faker.lorem().paragraph(3);
        int userId = faker.number().numberBetween(1, 10);

        String requestBody = String.format(
            "{\"title\": \"%s\", \"body\": \"%s\", \"userId\": %d}",
            title, body, userId
        );

        Response response = getRequest()
            .body(requestBody)
            .when()
                .post("/posts")
            .then()
                .extract()
                .response();

        APIUtils.printResponse(response);

        // Validations
        APIUtils.validateStatusCode(response, 201);
        APIUtils.validateResponseTime(response, 3000);

        // Validate created post data
        Assert.assertNotNull(response.jsonPath().get("id"), "Created post should have ID");
        Assert.assertEquals(response.jsonPath().getString("title"), title, 
            "Title should match input");
        Assert.assertEquals(response.jsonPath().getString("body"), body, 
            "Body should match input");
        Assert.assertEquals(response.jsonPath().getInt("userId"), userId, 
            "UserId should match input");

        int createdPostId = response.jsonPath().getInt("id");
        System.out.println("✅ Post created with ID: " + createdPostId);
        System.out.println("✅ Test passed: Create Post\n");
    }

    @Test(priority = 4)
    @Story("Get Posts by User")
    @Description("Verify that API returns posts for specific user")
    @Severity(SeverityLevel.NORMAL)
    public void testGetPostsByUser() {
        System.out.println("🧪 TEST: Get Posts By User");

        int userId = 1;

        Response response = getRequest()
            .queryParam("userId", userId)
            .when()
                .get("/posts")
            .then()
                .extract()
                .response();

        // Validations
        APIUtils.validateStatusCode(response, 200);
        APIUtils.validateResponseTime(response, 3000);

        // Validate all returned posts belong to the specified user
        java.util.List<Integer> userIds = response.jsonPath().getList("userId", Integer.class);
        for (Integer returnedUserId : userIds) {
            Assert.assertEquals(returnedUserId.intValue(), userId, 
                "All posts should belong to user " + userId);
        }

        System.out.println("✅ Found " + userIds.size() + " posts for user " + userId);
        System.out.println("✅ Test passed: Get Posts By User\n");
    }

    @AfterClass
    public void tearDownPostsTests() {
        System.out.println("🧹 Cleaning up Posts API Tests...");
        System.out.println("✅ Posts API Tests cleanup completed");
        System.out.println("🎯 All Posts API Tests Completed Successfully!\n");
    }
}
