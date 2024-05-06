import java.math.BigInteger;
import java.time.LocalDate;
import java.util.UUID;

public class JobPosting {
    private String title;
    private String description;
    private String requirements;
    private String deadline;
    private String jobid;

    public JobPosting(String title, String description, String requirements, String deadline, String jobid) {
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.deadline = deadline;
        this.jobid = jobid;
    }



    public String getId() {
        // Return the ID of the job posting
        return jobid;
    }

    private String generateJobId() {
        // Generate a numeric ID from the UUID
        UUID uuid = UUID.randomUUID();
        // Remove dashes and convert to BigInteger
        String uuidStr = uuid.toString().replaceAll("-", "");
        return new BigInteger(uuidStr, 16).toString();
    }

    // Getters and setters
    // Implement as needed
}
