import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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

            // Create login forms
            ApplicantLoginForm applicantLoginForm = new ApplicantLoginForm(connection);
            CompanyLoginForm companyLoginForm = new CompanyLoginForm(connection);

            // Create signup forms

            // Create toggle buttons for selecting user type
            ToggleButton applicantButton = new ToggleButton("Applicant");
            ToggleButton companyButton = new ToggleButton("Company");
            ToggleGroup toggleGroup = new ToggleGroup();
            applicantButton.setToggleGroup(toggleGroup);
            companyButton.setToggleGroup(toggleGroup);

            // Create VBox to hold toggle buttons
            VBox toggleBox = new VBox(10, applicantButton, companyButton);
            toggleBox.setAlignment(Pos.CENTER);

            // Create VBox to hold login forms
            VBox loginBox = new VBox(20);
            loginBox.setAlignment(Pos.CENTER);

            // Add action to applicant button
            applicantButton.setOnAction(event -> {
                loginBox.getChildren().clear();
                loginBox.getChildren().addAll(applicantLoginForm);
            });

            // Add action to company button
            companyButton.setOnAction(event -> {
                loginBox.getChildren().clear();
                loginBox.getChildren().addAll(companyLoginForm);
            });

            // Create border pane to hold views and forms
            BorderPane root = new BorderPane();
            root.setCenter(toggleBox);
            root.setBottom(loginBox);

            // Set stage title and scene
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Hiring Management System");
            primaryStage.setScene(scene);

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