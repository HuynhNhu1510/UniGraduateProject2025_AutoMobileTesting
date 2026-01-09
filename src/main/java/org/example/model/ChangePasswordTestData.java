package org.example.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChangePasswordTestData {

    @JsonProperty("testId")
    private String testId;

    @JsonProperty("currentPassword")
    private String currentPassword;

    @JsonProperty("newPassword")
    private String newPassword;

    @JsonProperty("expectedResult")
    private String expectedResult;

    @JsonProperty("expectedMessage")
    private String expectedMessage;

    @JsonProperty("description")
    private String description;

    @JsonProperty("category")
    private String category;

    // Getters and Setters
    public String getTestId() { return testId; }
    public void setTestId(String testId) { this.testId = testId; }

    public String getCurrentPassword() { return currentPassword; }
    public void setCurrentPassword(String currentPassword) { this.currentPassword = currentPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }

    public String getExpectedResult() { return expectedResult; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }

    public String getExpectedMessage() { return expectedMessage; }
    public void setExpectedMessage(String expectedMessage) { this.expectedMessage = expectedMessage; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    @Override
    public String toString() {
        return testId + " - " + description;
    }
}
