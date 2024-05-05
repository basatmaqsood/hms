import javafx.scene.control.*;
import javafx.scene.layout.*;
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
  

    public CompanyView(Connection connection,String companyId) {
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
                // Label applicantLabel = new Label("Applicants");
        
                // Add job posting form components to the layout
                getChildren().addAll(jobPostingLabel, jobPostingListView, applicantListView,
                        titleLabel, titleField, descriptionLabel, descriptionArea,
                        requirementsLabel, requirementsField, deadlineLabel, deadlinePicker, postButton);
        
                // Other initialization code...
            }
        
            private void postJob() {

                String title = titleField.getText();
                String description = descriptionArea.getText();
                String requirements = requirementsField.getText();
                String deadline = deadlinePicker.getValue().toString();
            ;
        
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
                        showAlert("Failure", "There was error posting the job.");

                    }
        
                    statement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            
        // Load job postings and applicants from the database


        // Set up UI components
        Label jobPostingLabel = new Label("Job Postings");
        Label applicantLabel = new Label("Applicants");

        // Add event handlers
        jobPostingListView.setOnMouseClicked(event -> {
            JobPosting selectedJobPosting = jobPostingListView.getSelectionModel().getSelectedItem();
            if (selectedJobPosting != null) {
                // Display applicants for the selected job posting
                // displayApplicantsForJob(selectedJobPosting);
            }
        });

        applicantListView.setOnMouseClicked(event -> {
            Applicant selectedApplicant = applicantListView.getSelectionModel().getSelectedItem();
            if (selectedApplicant != null) {
                // Display applicant details
                displayApplicantDetails(selectedApplicant);
            }
        });

        // Add UI components to the layout
        getChildren().addAll(jobPostingLabel, jobPostingListView, applicantLabel, applicantListView);
    }

    private void loadJobPostings() {
        // Code to load job postings from the database
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
    
            // Prepare SQL statement to retrieve applicants for the given job posting
            String query = "SELECT * FROM Applicant WHERE job_posting_id = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, jobPosting.getId()); // Assuming job posting id is stored in the JobPosting object
    
            // Execute query
            ResultSet resultSet = statement.executeQuery();
    
            // Create a list to store applicants
            List<Applicant> applicants = new ArrayList<>();
    
            // Iterate over the result set and create Applicant objects
            while (resultSet.next()) {
                Applicant applicant = new Applicant(
                        resultSet.getString("name"),
                        resultSet.getString("email"),
                        resultSet.getString("phone"),
                        resultSet.getString("skills"),
                        resultSet.getString("profile_picture"),
                        resultSet.getString("cv")
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
    


    private void displayApplicantDetails(Applicant applicant) {
        // Clear previous details from the details area
        GridPane applicantDetailsPane = new GridPane();
        applicantDetailsPane.setHgap(10);
        applicantDetailsPane.setVgap(5);

        // Populate the GridPane with applicant details
        applicantDetailsPane.addRow(0, new Label("Name:"), new Label(applicant.getName()));
        applicantDetailsPane.addRow(1, new Label("Email:"), new Label(applicant.getEmail()));
        applicantDetailsPane.addRow(2, new Label("Phone:"), new Label(applicant.getPhone()));
        applicantDetailsPane.addRow(3, new Label("Skills:"), new Label(applicant.getSkills()));


        getChildren().remove(applicantDetailsPane);
        getChildren().add(applicantDetailsPane);
    }
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}