import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.math.BigInteger;
import java.sql.*;
import java.util.UUID;

public class CompanySignupForm extends VBox {
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField websiteField;
    private TextArea descriptionArea;
    private Button signupButton;
    private Stage primaryStage;
    private Connection connection;

    public CompanySignupForm(Connection connection, Stage primaryStage) {
        this.connection = connection;
        this.primaryStage = primaryStage;
        // Fields specific to Company
        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Label websiteLabel = new Label("Website:");
        websiteField = new TextField();

        Label descriptionLabel = new Label("Description:");
        descriptionArea = new TextArea();

        signupButton = new Button("Signup");

        this.getChildren().addAll(
                nameLabel, nameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                websiteLabel, websiteField,
                descriptionLabel, descriptionArea,
                signupButton
        );

        setupSignupAction();
    }

    // Set up action for the signup button
    private void setupSignupAction() {
        signupButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String website = websiteField.getText();
            String description = descriptionArea.getText();
            UUID numericUUID = UUID.randomUUID();
            String companyId = new BigInteger(numericUUID.toString().replaceAll("-", ""), 16).toString();

            try {
                // Prepare SQL statement
                PreparedStatement statement = connection.prepareStatement("INSERT INTO Company (name, email, password, website_link , description,company_id) VALUES (?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.setString(4, website);
                statement.setString(5, description);
                statement.setString(6, companyId);

                // Execute query
                int rowsInserted = statement.executeUpdate();

                // Check if insertion was successful
                if (rowsInserted > 0) {
                    // Insertion successful
                    System.out.println("Company signup successful!");
                    this.getChildren().clear();
                    this.getChildren().addAll(new CompanyLoginForm(connection,primaryStage));
                
                } else {
                    // Insertion failed
                    System.out.println("Company signup failed. Please try again.");
                }

                // Close statement
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
