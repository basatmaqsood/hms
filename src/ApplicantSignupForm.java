import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.math.BigInteger;
import java.sql.*;
import java.util.UUID;

public class ApplicantSignupForm extends VBox {
    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;
    private TextField phoneField;
    private TextField skillsField;
    private Button signupButton;
    private Connection connection;
    String profilePicPath;
    String cvPath;


    public ApplicantSignupForm(Connection connection) {
        this.connection = connection;

        // Fields specific to Applicant
        Label nameLabel = new Label("Name:");
        nameField = new TextField();

        Label emailLabel = new Label("Email:");
        emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        passwordField = new PasswordField();

        Label phoneLabel = new Label("Phone:");
        phoneField = new TextField();

        Label skillsLabel = new Label("Skills:");
        skillsField = new TextField();

        Label profilePicLabel = new Label("Profile Picture:");
        Button profilePicButton = new Button("Upload Profile Picture");
        profilePicButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Profile Picture");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                // Get the selected file path and display it
                profilePicPath = file.getAbsolutePath();
                System.out.println("Selected Profile Picture: " + profilePicPath);
            }
        });

        Label cvLabel = new Label("CV:");
        Button cvButton = new Button("Upload CV");
        cvButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select CV");
            File file = fileChooser.showOpenDialog(null);
            if (file != null) {
                // Get the selected file path and display it
                cvPath = file.getAbsolutePath();
                System.out.println("Selected CV: " + cvPath);
            }
        });

        signupButton = new Button("Signup");

        this.getChildren().addAll(
                nameLabel, nameField,
                emailLabel, emailField,
                passwordLabel, passwordField,
                phoneLabel, phoneField,
                skillsLabel, skillsField,
                profilePicLabel, profilePicButton,
                cvLabel, cvButton,
                signupButton);

        setupSignupAction();
    }

    // Set up action for the signup button
    private void setupSignupAction() {
        signupButton.setOnAction(event -> {
            String name = nameField.getText();
            String email = emailField.getText();
            String password = passwordField.getText();
            String phone = phoneField.getText();
            String skills = skillsField.getText();
            UUID numericUUID = UUID.randomUUID();
            String applicantId = new BigInteger(numericUUID.toString().replaceAll("-", ""), 16).toString();



            try {
                // Prepare SQL statement
                PreparedStatement statement = connection.prepareStatement(
                        "INSERT INTO Applicant (name, email, password, phone, skills, applicant_id, profile_picture, cv) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                statement.setString(1, name);
                statement.setString(2, email);
                statement.setString(3, password);
                statement.setString(4, phone);
                statement.setString(5, skills);
                statement.setString(6, applicantId);
                File profilePicFile = new File(profilePicPath);
                InputStream profilePicInputStream;
                try {
                    profilePicInputStream = new FileInputStream(profilePicFile);
                    statement.setBlob(7, profilePicInputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                File cvFile = new File(cvPath);
                InputStream cvInputStream;
                try {
                    cvInputStream = new FileInputStream(cvFile);
                    statement.setBlob(8, cvInputStream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



                System.out.println(applicantId);
                // Execute query
                int rowsInserted = statement.executeUpdate();

                // Check if insertion was successful
                if (rowsInserted > 0) {
                    // Insertion successful
                    System.out.println("Applicant signup successful!");
                } else {
                    // Insertion failed
                    System.out.println("Applicant signup failed. Please try again.");
                }

                // Close statement
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }
}
