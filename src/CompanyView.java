import javafx.scene.control.*;
import javafx.scene.layout.*;

public class CompanyView extends VBox {
    private ListView<String> applicantList;
    private ListView<String> applicationList;

    public CompanyView() {
        Label applicantLabel = new Label("Applicants:");
        applicantList = new ListView<>();

        Label applicationLabel = new Label("Applications:");
        applicationList = new ListView<>();

        this.getChildren().addAll(
                applicantLabel, applicantList,
                applicationLabel, applicationList
        );
    }

    // Method to update applicant list
    public void updateApplicantList(String[] applicants) {
        applicantList.getItems().addAll(applicants);
    }

    // Method to update application list
    public void updateApplicationList(String[] applications) {
        applicationList.getItems().addAll(applications);
    }

    // Method to clear applicant list
    public void clearApplicantList() {
        applicantList.getItems().clear();
    }

    // Method to clear application list
    public void clearApplicationList() {
        applicationList.getItems().clear();
    }
}
