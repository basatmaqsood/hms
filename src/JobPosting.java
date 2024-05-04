import java.math.BigInteger;
import java.util.UUID;

public class JobPosting {
    private String title;
    private String description;
    private String requirements;
    private String deadline;
    UUID numericUUID = UUID.randomUUID();
    String jobid = new BigInteger(numericUUID.toString().replaceAll("-", ""), 16).toString();


    public JobPosting(String title, String description, String requirements, String deadline) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.deadline = deadline;
    }

    public String getId() {
        // Return the ID of the job posting
        return jobid;
    }

    // Getters and setters
    // Implement as needed
}
