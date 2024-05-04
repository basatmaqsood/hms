import java.sql.*;

public class Application {
    private int postingId;
    private int jobId;
    private int companyId;

    public Application(int postingId, int jobId, int companyId) {
        this.postingId = postingId;
        this.jobId = jobId;
        this.companyId = companyId;
    }

    // Getters and setters

    public void insertApplication(Connection connection) throws SQLException {
        String sql = "INSERT INTO Application (posting_id, job_id, company_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, postingId);
            pstmt.setInt(2, jobId);
            pstmt.setInt(3, companyId);
            pstmt.executeUpdate();
        }
    }
}
