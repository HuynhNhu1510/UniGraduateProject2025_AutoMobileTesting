package org.example.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterTestData {

    @JsonProperty("testId")
    private String testId;

    @JsonProperty("category")
    private String category;

    @JsonProperty("fullName")
    private String fullName;

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

    @JsonProperty("scenario")
    private String scenario;

    @JsonProperty("fullNameValid")
    private Boolean fullNameValid;

    @JsonProperty("emailValid")
    private Boolean emailValid;

    @JsonProperty("passwordValid")
    private Boolean passwordValid;

    // Constructor
    public RegisterTestData() {}

    // Getters
    public String getTestId() { return testId; }
    public String getCategory() { return category; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getExpectedResult() { return expectedResult; }
    public String getExpectedMessage() { return expectedMessage; }
    public String getDescription() { return description; }
    public String getScenario() { return scenario; }
    public Boolean getFullNameValid() { return fullNameValid; }
    public Boolean getEmailValid() { return emailValid; }
    public Boolean getPasswordValid() { return passwordValid; }

    // Setters
    public void setTestId(String testId) { this.testId = testId; }
    public void setCategory(String category) { this.category = category; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setExpectedResult(String expectedResult) { this.expectedResult = expectedResult; }
    public void setExpectedMessage(String expectedMessage) { this.expectedMessage = expectedMessage; }
    public void setDescription(String description) { this.description = description; }
    public void setScenario(String scenario) { this.scenario = scenario; }
    public void setFullNameValid(Boolean fullNameValid) { this.fullNameValid = fullNameValid; }
    public void setEmailValid(Boolean emailValid) { this.emailValid = emailValid; }
    public void setPasswordValid(Boolean passwordValid) { this.passwordValid = passwordValid; }

    @Override
    public String toString() {
        return "RegisterTestData{" +
                "testId='" + testId + '\'' +
                ", category='" + category + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                '}';
    }
}
