package org.example.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.ChangePasswordTestData;
import org.example.model.LoginTestData;
import org.example.model.RegisterTestData;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonDataReader {
    private static final String JSON_FILE_PATH = "src/main/resources/test_data/test_data.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode rootNode;

    static {
        try {
            rootNode = objectMapper.readTree(new File(JSON_FILE_PATH));
        } catch (IOException e) {
            throw new RuntimeException("Failed to load test data JSON file: " + e.getMessage(), e);
        }
    }

    public static List<LoginTestData> getLoginTestData(String jsonPath) {
        List<LoginTestData> testDataList = new ArrayList<>();
        try {
            JsonNode dataNode = navigateToNode(jsonPath);
            if (dataNode != null && dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    LoginTestData testData = objectMapper.treeToValue(node, LoginTestData.class);
                    testDataList.add(testData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Login test data from path: " + jsonPath, e);
        }
        return testDataList;
    }

    public static List<RegisterTestData> getRegisterTestData(String jsonPath) {
        List<RegisterTestData> testDataList = new ArrayList<>();
        try {
            JsonNode dataNode = navigateToNode(jsonPath);
            if (dataNode != null && dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    RegisterTestData testData = objectMapper.treeToValue(node, RegisterTestData.class);
                    testDataList.add(testData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Register test data from path: " + jsonPath, e);
        }
        return testDataList;
    }

    public static List<ChangePasswordTestData> getChangePasswordTestData(String jsonPath) {
        List<ChangePasswordTestData> testDataList = new ArrayList<>();
        try {
            JsonNode dataNode = navigateToNode(jsonPath);
            if (dataNode != null && dataNode.isArray()) {
                for (JsonNode node : dataNode) {
                    ChangePasswordTestData testData = objectMapper.treeToValue(node, ChangePasswordTestData.class);
                    testDataList.add(testData);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse Change Password test data from path: " + jsonPath, e);
        }
        return testDataList;
    }

    private static JsonNode navigateToNode(String path) {
        String[] pathParts = path.split("\\.");
        JsonNode currentNode = rootNode;

        for (String part : pathParts) {
            if (currentNode == null) {
                return null;
            }
            currentNode = currentNode.get(part);
        }

        return currentNode;
    }

    public static List<LoginTestData> getAllLoginEquivalencePartitioning() {
        List<LoginTestData> allData = new ArrayList<>();
        allData.addAll(getLoginTestData("login_test_data.equivalence_partitioning.valid_credentials"));
        allData.addAll(getLoginTestData("login_test_data.equivalence_partitioning.invalid_email_format"));
        allData.addAll(getLoginTestData("login_test_data.equivalence_partitioning.invalid_password_format"));
        allData.addAll(getLoginTestData("login_test_data.equivalence_partitioning.empty_fields"));
        return allData;
    }

    public static List<LoginTestData> getAllLoginBoundaryValueAnalysis() {
        List<LoginTestData> allData = new ArrayList<>();
        allData.addAll(getLoginTestData("login_test_data.boundary_value_analysis.password_length"));
        allData.addAll(getLoginTestData("login_test_data.boundary_value_analysis.email_length"));
        return allData;
    }
}
