package dataprovider;

import org.example.helpers.JsonDataReader;
import org.example.model.ChangePasswordTestData;
import org.example.model.LoginTestData;
import org.example.model.RegisterTestData;
import org.testng.annotations.DataProvider;

import java.util.List;

public class TestDataProviders {
    @DataProvider(name = "loginValidCredentials_EP")
    public Object[][] loginValidCredentials() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.valid_credentials");
        return convertLoginDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Email Format
     */
    @DataProvider(name = "loginInvalidEmailFormat_EP")
    public Object[][] loginInvalidEmailFormat() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.equivalence_partitioning.invalid_email_format");
        return convertLoginDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Password Format
     */
    @DataProvider(name = "loginInvalidPasswordFormat_EP")
    public Object[][] loginInvalidPasswordFormat() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.equivalence_partitioning.invalid_password_format");
        return convertLoginDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Empty Fields
     */
    @DataProvider(name = "loginEmptyFields_EP")
    public Object[][] loginEmptyFields() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.equivalence_partitioning.empty_fields");
        return convertLoginDataToArray(testData);
    }

    /**
     * BOUNDARY VALUE ANALYSIS - Password Length
     */
    @DataProvider(name = "loginPasswordLength_BVA")
    public Object[][] loginPasswordLength() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.boundary_value_analysis.password_length");
        return convertLoginDataToArray(testData);
    }

    /**
     * BOUNDARY VALUE ANALYSIS - Email Length
     */
    @DataProvider(name = "loginEmailLength_BVA")
    public Object[][] loginEmailLength() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.boundary_value_analysis.email_length");
        return convertLoginDataToArray(testData);
    }

    /**
     * ALL Equivalence Partitioning Login Tests
     */
    @DataProvider(name = "allLoginEquivalencePartitioning")
    public Object[][] allLoginEquivalencePartitioning() {
        List<LoginTestData> testData = JsonDataReader.getAllLoginEquivalencePartitioning();
        return convertLoginDataToArray(testData);
    }

    /**
     * ALL Boundary Value Analysis Login Tests
     */
    @DataProvider(name = "allLoginBoundaryValueAnalysis")
    public Object[][] allLoginBoundaryValueAnalysis() {
        List<LoginTestData> testData = JsonDataReader.getAllLoginBoundaryValueAnalysis();
        return convertLoginDataToArray(testData);
    }

    @DataProvider(name = "loginInvalidSpecialChars_EP")
    public Object[][] loginInvalidSpecialChars() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.equivalence_partitioning.invalid_special_characters");
        return convertLoginDataToArray(testData);
    }

    /**
     * SPECIAL CASES - Security, Unicode, Edge Cases
     */
    @DataProvider(name = "loginSpecialCases")
    public Object[][] loginSpecialCases() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.special_cases");
        return convertLoginDataToArray(testData);
    }

    /**
     * COMBINATION TESTING
     */
    @DataProvider(name = "loginCombinationTests")
    public Object[][] loginCombinationTests() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.combination_testing");
        return convertLoginDataToArray(testData);
    }

    /**
     * BOUNDARY VALUE ANALYSIS - Special Character Count
     */
    @DataProvider(name = "loginSpecialCharCount_BVA")
    public Object[][] loginSpecialCharCount() {
        List<LoginTestData> testData = JsonDataReader.getLoginTestData(
                "login_test_data.boundary_value_analysis.special_character_count");
        return convertLoginDataToArray(testData);
    }

    // ==================== REGISTER DATA PROVIDERS ====================

    /**
     * DECISION TABLE - All combinations of valid/invalid inputs
     */
    @DataProvider(name = "registerDecisionTable")
    public Object[][] registerDecisionTable() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.decision_table");
        return convertRegisterDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Full Name Patterns
     */
    @DataProvider(name = "registerInvalidFullName_EP")
    public Object[][] registerInvalidFullName() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.equivalence_partitioning.invalid_fullname_patterns");
        return convertRegisterDataToArray(testData);
    }

    /**
     * BOUNDARY VALUE ANALYSIS - Full Name Length
     */
    @DataProvider(name = "registerFullNameLength_BVA")
    public Object[][] registerFullNameLength() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.boundary_value_analysis.fullname_length");
        return convertRegisterDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Full Name Validation
     */
    @DataProvider(name = "registerFullNameValidation_EP")
    public Object[][] registerFullNameValidation() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.equivalence_partitioning.fullname_validation");
        return convertRegisterDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Email Validation
     */
    @DataProvider(name = "registerEmailValidation_EP")
    public Object[][] registerEmailValidation() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.equivalence_partitioning.email_validation");
        return convertRegisterDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Password Validation
     */
    @DataProvider(name = "registerPasswordValidation_EP")
    public Object[][] registerPasswordValidation() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.equivalence_partitioning.password_validation");
        return convertRegisterDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Special Characters
     */
    @DataProvider(name = "registerInvalidSpecialChars_EP")
    public Object[][] registerInvalidSpecialChars() {
        List<RegisterTestData> testData = JsonDataReader.getRegisterTestData(
                "register_test_data.equivalence_partitioning.invalid_special_characters_register");
        return convertRegisterDataToArray(testData);
    }

    // ==================== CHANGE PASSWORD DATA PROVIDERS ====================

    /**
     * EQUIVALENCE PARTITIONING - Valid Passwords
     */
    @DataProvider(name = "changePasswordValid_EP")
    public Object[][] changePasswordValid() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.valid_passwords");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Current Password
     */
    @DataProvider(name = "changePasswordInvalidCurrent_EP")
    public Object[][] changePasswordInvalidCurrent() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.invalid_current");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid New Password Format
     */
    @DataProvider(name = "changePasswordInvalidNew_EP")
    public Object[][] changePasswordInvalidNew() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.invalid_new_format");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * BOUNDARY VALUE ANALYSIS - New Password Length
     */
    @DataProvider(name = "changePasswordLength_BVA")
    public Object[][] changePasswordLength() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.boundary_value_analysis.new_password_length");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid New Password Format
     */
    @DataProvider(name = "changePasswordInvalidNewFormat_EP")
    public Object[][] changePasswordInvalidNewFormat() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.new_password_format");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Invalid Special Characters
     */
    @DataProvider(name = "changePasswordInvalidSpecialChars_EP")
    public Object[][] changePasswordInvalidSpecialChars() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.invalid_special_characters_change");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * EQUIVALENCE PARTITIONING - Empty Fields
     */
    @DataProvider(name = "changePasswordEmptyFields_EP")
    public Object[][] changePasswordEmptyFields() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.equivalence_partitioning.empty_fields");
        return convertChangePasswordDataToArray(testData);
    }

    /**
     * SPECIAL CASES
     */
    @DataProvider(name = "changePasswordSpecialCases")
    public Object[][] changePasswordSpecialCases() {
        List<ChangePasswordTestData> testData = JsonDataReader.getChangePasswordTestData(
                "change_password_test_data.special_cases");
        return convertChangePasswordDataToArray(testData);
    }

    // ==================== HELPER METHODS ====================

    private Object[][] convertLoginDataToArray(List<LoginTestData> dataList) {
        Object[][] dataArray = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = dataList.get(i);
        }
        return dataArray;
    }

    private Object[][] convertRegisterDataToArray(List<RegisterTestData> dataList) {
        Object[][] dataArray = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = dataList.get(i);
        }
        return dataArray;
    }

    private Object[][] convertChangePasswordDataToArray(List<ChangePasswordTestData> dataList) {
        Object[][] dataArray = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i][0] = dataList.get(i);
        }
        return dataArray;
    }
}
