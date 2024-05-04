import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ApplicantView extends VBox {
    private ListView<String> jobList;

    public ApplicantView() {
        Label jobListLabel = new Label("Available Jobs:");
        jobList = new ListView<>();

        this.getChildren().addAll(
                jobListLabel, jobList
        );
    }

    // Method to update job list
    public void updateJobList(String[] jobs) {
        jobList.getItems().addAll(jobs);
    }

    // Method to clear job list
    public void clearJobList() {
        jobList.getItems().clear();
    }
}
