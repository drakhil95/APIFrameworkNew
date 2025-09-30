package mdoels.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import groovy.transform.ToString;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * User request model for API operations
 */
@Getter // Lombok annotation to generate getters
@Setter  // Lombok annotation to generate setters
@NoArgsConstructor // Lombok annotation to generate no args constructor
@AllArgsConstructor  // Lombok annotation to generate all args constructor
@ToString // Lombok annotation to generate toString method
public class UserRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("username")
    private String username;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phone")
    private String phone;

    @JsonProperty("website")
    private String website;
}