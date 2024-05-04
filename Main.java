import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {
    private static final String DB_URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static final String DB_USER = "system";
    private static final String DB_PASSWORD = "Oracle_1";

    @Override
    public void start(Stage primaryStage) {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Create views
            ApplicantView applicantView = new ApplicantView();
            CompanyView companyView = new CompanyView();

            // Create login forms
            LoginForm applicantLoginForm = new LoginForm();
            LoginForm companyLoginForm = new LoginForm();

            // Create signup forms
            SignupForm applicantSignupForm = new SignupForm();
            SignupForm companySignupForm = new SignupForm();

            // Create job form
            JobForm jobForm = new JobForm();

            // Create border pane to hold views
            BorderPane root = new BorderPane();
            root.setLeft(applicantView);
            root.setRight(companyView);

            // Set stage title and scene
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Hiring Management System");
            primaryStage.setScene(scene);

            // Show login form for applicants by default
            root.setCenter(applicantLoginForm);

            // Handle login events
            applicantLoginForm.getLoginButton().setOnAction(event -> {
                String email = applicantLoginForm.getEmail();
                String password = applicantLoginForm.getPassword();
                // Authenticate applicant
                // If successful, display applicant view
                // If failed, display error message
            });

            companyLoginForm.getLoginButton().setOnAction(event -> {
                String email = companyLoginForm.getEmail();
                String password = companyLoginForm.getPassword();
                // Authenticate company
                // If successful, display company view
                // If failed, display error message
            });

            // Handle signup events
            applicantSignupForm.getSignupButton().setOnAction(event -> {
                String name = applicantSignupForm.getName();
                String email = applicantSignupForm.getEmail();
                String password = applicantSignupForm.getPassword();
                // Signup applicant
                // If successful, display login form
                // If failed, display error message
            });

            companySignupForm.getSignupButton().setOnAction(event -> {
                String name = companySignupForm.getName();
                String email = companySignupForm.getEmail();
                String password = companySignupForm.getPassword();
                // Signup company
                // If successful, display login form
                // If failed, display error message
            });

            // Handle job post event
            jobForm.getPostButton().setOnAction(event -> {
                String title = jobForm.getTitle();
                String description = jobForm.getDescription();
                String requirements = jobForm.getRequirements();
                String deadline = jobForm.getDeadline();
                // Post job
                // If successful, display success message
                // If failed, display error message
            });

            // Show the stage
            primaryStage.show();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
