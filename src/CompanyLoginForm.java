import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.sql.*;

public class CompanyLoginForm extends VBox {
    private TextField emailField;
    private PasswordField passwordField;
    private Button loginButton;
    private Button signupToggleButton; // Toggle between login and signup forms
    private Connection connection;
    private CompanySignupForm companySignupForm;

    public CompanyLoginForm(Connection connection) {
        this.connection = connection;
        this.companySignupForm = new CompanySignupForm(this.connection);

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
                    System.out.println("Company login successful!");
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
