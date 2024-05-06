import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class CompanyLoginForm extends VBox {
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button signupToggleButton; // Toggle between login and signup forms
    private Connection connection;
    private CompanySignupForm companySignupForm;
    private Stage primaryStage;

    public CompanyLoginForm(Connection connection, Stage primaryStage) {
        this.connection = connection;
        this.primaryStage = primaryStage;
        this.companySignupForm = new CompanySignupForm(connection, primaryStage);

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        signupToggleButton = new Button("Sign Up");

        // Set styles
        setSpacing(10);
        setAlignment(Pos.CENTER);
        emailLabel.setStyle("-fx-font-size: 14px;");
        passwordLabel.setStyle("-fx-font-size: 14px;");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        signupToggleButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14px;");

        this.getChildren().addAll(
                emailLabel, emailField,
                passwordLabel, passwordField,
                loginButton, signupToggleButton
        );

        setupLoginAction();
        setupSignupToggleAction();
    }

    // Set up action for the login button
    private void setupLoginAction() {
        loginButton.setOnAction(event -> {
            String email = getEmail();
            String password = getPassword();
            try {
                // Prepare SQL statement
                String query = "SELECT * FROM Company WHERE Email = ? AND Password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                // Execute query
                ResultSet resultSet = statement.executeQuery();

                // Check if user exists
                if (resultSet.next()) {
                    // User authenticated
                    // You can add code here to navigate to the appropriate view
                    String companyId = resultSet.getString("company_id");
                    System.out.println("Company login successful!");
                    primaryStage.setTitle("Company Dashboard");
                    primaryStage.setScene(new Scene(new CompanyView(connection, companyId)));
                } else {
                    // User not found or invalid credentials
                    System.out.println("Invalid email or password. Please try again.");
                }

                // Close statement and result set
                statement.close();
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    // Set up action for the signup toggle button
    private void setupSignupToggleAction() {
        signupToggleButton.setOnAction(event -> {
            this.getChildren().clear();
            this.getChildren().addAll(companySignupForm);
            this.setAlignment(Pos.CENTER);
            System.out.println("Switching to signup form...");
        });
    }

    // Getters for form fields
    public String getEmail() {
        return emailField.getText();
    }

    public String getPassword() {
        return passwordField.getText();
    }
}
