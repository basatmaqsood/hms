import java.sql.*;
import java.util.Date;

public class Job {
    private int postingId;
    private int jobId;
    private String title;
    private String description;
    private String requirements;
    private Date deadline;

    public Job(int jobId, String title, String description, String requirements, Date deadline) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.requirements = requirements;
        this.deadline = deadline;
    }

    // Getters and setters

    public void apply(Connection connection, Applicant applicant) throws SQLException {
        String sql = "INSERT INTO Application (posting_id, job_id, applicant_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, postingId);
            pstmt.setInt(2, jobId);
            pstmt.setInt(3, applicant.getApplicantId());
            pstmt.executeUpdate();
        }
    }

    public int getJobId() {
        return jobId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getRequirements() {
        return requirements;
    }

    public Date getDeadline() {
        return deadline;
    }

    public int getPostingId() {
        return postingId;
    }

}
