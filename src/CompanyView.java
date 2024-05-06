import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class CompanyView extends VBox {
    private Connection connection;
    private ListView<JobPosting> jobPostingListView;
    private ListView<Applicant> applicantListView;
    private TextField titleField;
    private TextArea descriptionArea;
    private TextField requirementsField;
    private DatePicker deadlinePicker;
    private Button postButton;
    private String companyId;

    public CompanyView(Connection connection, String companyId) {
        this.connection = connection;
        this.companyId = companyId;
        jobPostingListView = new ListView<>();
        applicantListView = new ListView<>();
        loadJobPostings();
        loadApplicants();
        // Create form components for posting a job
        Label titleLabel = new Label("Title:");
        titleField = new TextField();

        Label descriptionLabel = new Label("Description:");
        descriptionArea = new TextArea();

        Label requirementsLabel = new Label("Requirements:");
        requirementsField = new TextField();

        Label deadlineLabel = new Label("Deadline:");
        deadlinePicker = new DatePicker();

        postButton = new Button("Post Job");
        postButton.setOnAction(event -> postJob());
        Label jobPostingLabel = new Label("Job Postings");

        applicantListView.setOnMouseClicked(event -> {
            Applicant selectedApplicant = applicantListView.getSelectionModel().getSelectedItem();
            if (selectedApplicant != null) {
                displayApplicantDetails(selectedApplicant);
            }
        });
        
        // Add event handler for job posting selection
        jobPostingListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayApplicantsForJob(newValue);
            }
        });

        // Add job posting form components to the layout
        getChildren().addAll(jobPostingLabel, jobPostingListView, applicantListView,
                titleLabel, titleField, descriptionLabel, descriptionArea,
                requirementsLabel, requirementsField, deadlineLabel, deadlinePicker, postButton);
    }

    private void postJob() {
        String title = titleField.getText();
        String description = descriptionArea.getText();
        String requirements = requirementsField.getText();
        String deadline = deadlinePicker.getValue().toString();

        // Insert the job posting into the database
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO Job (posting_id, title, description, requirements, deadline, company_id) " +
                            "VALUES (job_seq.nextval, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?)");
            statement.setString(1, title);
            statement.setString(2, description);
            statement.setString(3, requirements);
            statement.setString(4, deadline);
            statement.setString(5, companyId);

            int rowsInserted = statement.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Job posted successfully!");
                showAlert("Success", "Job posted successfully!");
                // Optionally, you can update the job postings list view here
            } else {
                System.out.println("Failed to post job.");
                showAlert("Failure", "There was an error posting the job.");
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadJobPostings() {
        try {
            // Retrieve job postings from the database
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Job");
            ResultSet resultSet = statement.executeQuery();

            // Create a list to store job postings
            List<JobPosting> jobPostings = new ArrayList<>();
            while (resultSet.next()) {
                // Create JobPosting object from the result set
                JobPosting jobPosting = new JobPosting(
                    resultSet.getString("title"),
                    resultSet.getString("description"),
                    resultSet.getString("requirements"),
                    resultSet.getDate("deadline").toString(),
                    resultSet.getString("posting_id")
                );
                jobPostings.add(jobPosting);
            }

            // Display job postings in the ListView
            jobPostingListView.getItems().addAll(jobPostings);

            // Close statement and result set
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Modify the loadApplicants method to handle BLOB data retrieval
    private void loadApplicants() {
        try {
            // Retrieve applicants from the database
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM Applicant");
            ResultSet resultSet = statement.executeQuery();

            List<Applicant> applicants = new ArrayList<>();
            while (resultSet.next()) {
                // Retrieve BLOB data
                Blob profilePictureBlob = resultSet.getBlob("profile_picture");
                Blob cvBlob = resultSet.getBlob("cv");

                // Convert BLOB data to string representation
                String profilePicture = null;
                if (profilePictureBlob != null) {
                    byte[] profilePictureBytes = profilePictureBlob.getBytes(1, (int) profilePictureBlob.length());
                    profilePicture = Base64.getEncoder().encodeToString(profilePictureBytes);
                }

                String cv = null;
                if (cvBlob != null) {
                    byte[] cvBytes = cvBlob.getBytes(1, (int) cvBlob.length());
                    cv = Base64.getEncoder().encodeToString(cvBytes);
                }

                // Create Applicant object with BLOB data
                Applicant applicant = new Applicant(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("skills"),
                        profilePicture,
                        cv
                );
                applicants.add(applicant);
            }

            // Display applicants in the ListView
            applicantListView.getItems().addAll(applicants);

            // Close statement and result set
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

  private void displayApplicantsForJob(JobPosting jobPosting) {
    try {
        // Clear previous applicants from the ListView
        applicantListView.getItems().clear();

        // If jobPosting is null, return without performing any further actions
        if (jobPosting == null) {
            return;
        }

        // Prepare SQL statement to retrieve applicants for the given job posting using SQL joins
        String query = "SELECT a.name, a.email, a.phone, a.skills, a.profile_picture, a.cv " +
                       "FROM Applicant a " +
                       "JOIN Application app ON a.applicant_id = app.applicant_id " +
                       "JOIN Job j ON app.posting_id = j.posting_id " +
                       "WHERE j.posting_id = ?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, jobPosting.getId()); // Assuming job posting id is stored in the JobPosting object

        // Execute query
        ResultSet resultSet = statement.executeQuery();

        // Create a list to store applicants
        List<Applicant> applicants = new ArrayList<>();

        // Iterate over the result set and create Applicant objects
        while (resultSet.next()) {
            // Retrieve BLOB data as InputStream
            InputStream profilePictureStream = resultSet.getBinaryStream("profile_picture");
            InputStream cvStream = resultSet.getBinaryStream("cv");

            // Convert BLOB data to byte arrays or other desired format
            // For example, you can use Base64 encoding for strings
            String profilePicture = null;
            try {
                profilePicture = encodeBlob(profilePictureStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            String cv = null;
            try {
                cv = encodeBlob(cvStream);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // Create Applicant object with BLOB data
            Applicant applicant = new Applicant(
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("skills"),
                    profilePicture,
                    cv
            );
            applicants.add(applicant);
        }

        // Display applicants in the ListView
        applicantListView.getItems().addAll(applicants);

        // Close statement and result set
        statement.close();
        resultSet.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

// Method to encode BLOB data (e.g., profile picture, CV) to Base64 string
private String encodeBlob(InputStream inputStream) throws IOException {
    if (inputStream == null) {
        return null;
    }
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    byte[] buffer = new byte[4096];
    int bytesRead;
    while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
    }
    return Base64.getEncoder().encodeToString(outputStream.toByteArray());
}

private void displayApplicantDetails(Applicant applicant) {
    // Create a dialog to display applicant details
    Dialog<Void> dialog = new Dialog<>();
    dialog.setTitle("Applicant Details");
    dialog.setHeaderText("Details for " + applicant.getName());

    // Create labels to display applicant details
    Label nameLabel = new Label("Name: " + applicant.getName());
    Label emailLabel = new Label("Email: " + applicant.getEmail());
    Label phoneLabel = new Label("Phone: " + applicant.getPhone());
    Label skillsLabel = new Label("Skills: " + applicant.getSkills());

    // Create a VBox to hold the labels
    VBox content = new VBox(10);
    content.getChildren().addAll(nameLabel, emailLabel, phoneLabel, skillsLabel);
    content.setPadding(new Insets(20));

    // Set the content of the dialog
    dialog.getDialogPane().setContent(content);

    // Add OK button to close the dialog
    ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
    dialog.getDialogPane().getButtonTypes().add(okButton);

    // Show the dialog
    dialog.showAndWait();
}

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
