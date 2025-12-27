package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginTestData {
    @JsonProperty("testId")
    private String testId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("email")
    private String email;

    @JsonProperty("password")
    private String password;

    @JsonProperty("expectedResult")
    private String expectedResult;

    @JsonProperty("expectedMessage")
    private String expectedMessage;

    @JsonProperty("description")
    private String description;

    // Constructor
    public LoginTestData() {}

    // Getters
    public String getTestId() { return testId; }
    public String getCategory() { return category; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getExpectedResult() { return expectedResult; }
    public String getExpectedMessage() { return expectedMessage; }
    public String getDescription() { return description; }

    // Setters
    public void setTestId(String testId) { this.testId = testId; }
    public void setCategory(String category) { this.category = category; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }
    public void setExpectedMessage(String expectedMessage) { this.expectedMessage = expectedMessage; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "LoginTestData{" +
                "testId='" + testId + '\'' +
                ", category='" + category + '\'' +
                ", email='" + email + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                '}';
    }
}
