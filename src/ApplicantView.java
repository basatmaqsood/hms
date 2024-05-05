import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ApplicantView extends VBox {
    private ListView<JobListing> jobList;
    private Connection connection;
    private String applicantId;

    public ApplicantView(Connection connection, String applicantId) {
        this.connection = connection;
        this.applicantId = applicantId;
        Label jobListLabel = new Label("Available Jobs:");
        jobList = new ListView<>();

        // Set cell factory to display custom list cells
        jobList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(JobListing item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setGraphic(createJobListingPane(item));
                }
            }
        });

        // Set VBox constraints
        VBox.setVgrow(jobList, Priority.ALWAYS);

        this.getChildren().addAll(
                jobListLabel, jobList);

        // Load available jobs from the database
        loadAvailableJobs();
    }

    // Method to load available jobs from the database
    private void loadAvailableJobs() {
        try {
            String query = "SELECT posting_id, title, requirements, deadline, company_id, job_id FROM Job WHERE deadline > ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, java.sql.Date.valueOf(LocalDate.now()));
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int postingId = resultSet.getInt("posting_id");
                String title = resultSet.getString("title");
                String requirements = resultSet.getString("requirements");
                String deadline = resultSet.getString("deadline");
                String companyId = resultSet.getString("company_id");
                String jobId = resultSet.getString("job_id");
                Button applyButton = new Button("Apply");
                applyButton.setOnAction(event -> applyForJob(postingId, companyId, jobId));
                JobListing jobListing = new JobListing(title, requirements, deadline, applyButton);
                jobList.getItems().add(jobListing);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to apply for a job
    private void applyForJob(int postingId, String companyId, String jobId) {
        try {
            // Insert application details into the Application table
            String query = "INSERT INTO Application (applicant_id, posting_id, job_id, company_id) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, applicantId);
            statement.setInt(2, postingId);
            statement.setString(3, jobId);
            statement.setString(4, companyId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Applied for job successfully!");
            } else {
                System.out.println("Failed to apply for job.");
            }
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to create a GridPane for a job listing
    private GridPane createJobListingPane(JobListing jobListing) {
        GridPane pane = new GridPane();
        pane.setPadding(new Insets(10));
        pane.setHgap(10);
        pane.setVgap(5);

        Label titleLabel = new Label(jobListing.getTitle());
        Label requirementsLabel = new Label("Requirements: " + jobListing.getRequirements());
        Label deadlineLabel = new Label("Deadline: " + jobListing.getDeadline());
        Button applyButton = jobListing.getApplyButton();

        // Add components to the GridPane
        pane.add(titleLabel, 0, 0);
        pane.add(requirementsLabel, 0, 1);
        pane.add(deadlineLabel, 0, 2);
        pane.add(applyButton, 1, 0, 1, 3); // Span applyButton across multiple rows

        return pane;
    }

    // Class to represent a job listing with title, requirements, deadline, and
    // apply button
    private static class JobListing {
        private String title;
        private String requirements;
        private String deadline;
        private Button applyButton;

        public JobListing(String title, String requirements, String deadline, Button applyButton) {
            this.title = title;
            this.requirements = requirements;
            this.deadline = deadline;
            this.applyButton = applyButton;
        }

        public String getTitle() {
            return title;
        }

        public String getRequirements() {
            return requirements;
        }

        public String getDeadline() {
        LocalDate deadlineDate;
        try {
            deadlineDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (DateTimeParseException e) {
            // If parsing fails, try parsing without the time component
            deadlineDate = LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        }

        // Format the deadline date without the time component
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return deadlineDate.format(formatter);
        }

        public Button getApplyButton() {
            return applyButton;
        }
    }
}
