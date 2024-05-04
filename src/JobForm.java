import javafx.scene.control.*;
import javafx.scene.layout.*;

public class JobForm extends VBox {
    private TextField titleField;
    private TextArea descriptionArea;
    private TextField requirementsField;
    private DatePicker deadlinePicker;
    private Button postButton;

    public JobForm() {
        Label titleLabel = new Label("Title:");
        titleField = new TextField();

        Label descriptionLabel = new Label("Description:");
        descriptionArea = new TextArea();

        Label requirementsLabel = new Label("Requirements:");
        requirementsField = new TextField();

        Label deadlineLabel = new Label("Deadline:");
        deadlinePicker = new DatePicker();

        postButton = new Button("Post Job");

        this.getChildren().addAll(
                titleLabel, titleField,
                descriptionLabel, descriptionArea,
                requirementsLabel, requirementsField,
                deadlineLabel, deadlinePicker,
                postButton
        );
    }

    // Getters for form fields
    public String getTitle() {
        return titleField.getText();
    }

    public String getDescription() {
        return descriptionArea.getText();
    }

    public String getRequirements() {
        return requirementsField.getText();
    }

    public String getDeadline() {
        return deadlinePicker.getValue().toString();
    }

    // Method to clear form fields
    public void clearFields() {
        titleField.clear();
        descriptionArea.clear();
        requirementsField.clear();
        deadlinePicker.getEditor().clear();
    }

    // Getter for post button
    public Button getPostButton() {
        return postButton;
    }
}
