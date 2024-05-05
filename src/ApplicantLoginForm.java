import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.*;

public class ApplicantLoginForm extends VBox {
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button signupToggleButton; // Toggle between login and signup forms
    private Connection connection;
    private Stage primaryStage;
    ApplicantSignupForm applicantSignupForm;

    public ApplicantLoginForm(Connection connection, Stage primaryStage) {
        this.connection = connection;
        this.primaryStage = primaryStage;
        this.applicantSignupForm = new ApplicantSignupForm(this.connection, primaryStage);

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        loginButton = new Button("Login");
        signupToggleButton = new Button("Sign Up");

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
                String query = "SELECT * FROM Applicant WHERE Email = ? AND Password = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setString(1, email);
                statement.setString(2, password);

                // Execute query
                ResultSet resultSet = statement.executeQuery();

                // Check if user exists
                if (resultSet.next()) {
                    // User authenticated
                    // You can add code here to navigate to the appropriate view
                    System.out.println("Applicant login successful!");
                    String applicantId =  resultSet.getString("applicant_id");
                    ApplicantView applicantView = new ApplicantView(connection,applicantId);
                    primaryStage.setTitle("Applicant Dashboard");
                    primaryStage.setScene(new Scene(applicantView, 800, 600));

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
            // Toggle to the signup form
            // You can add code here to switch the view to the signup form
            this.getChildren().clear();
            this.getChildren().addAll(applicantSignupForm);
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
