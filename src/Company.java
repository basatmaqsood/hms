import java.sql.*;
import java.util.List;

public class Company {
    private int companyId;
    private String name;
    private String websiteLink;
    private String description;

    public Company(String name, String websiteLink, String description) {
        this.name = name;
        this.websiteLink = websiteLink;
        this.description = description;
    }

    // Getters and setters

    public void signup(Connection connection) throws SQLException {
        String sql = "INSERT INTO Company (name, website_link, description) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, name);
            pstmt.setString(2, websiteLink);
            pstmt.setString(3, description);
            pstmt.executeUpdate();
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    companyId = generatedKeys.getInt(1);
                }
            }
        }
    }

    public boolean login(Connection connection, String email, String password) throws SQLException {
        String sql = "SELECT company_id FROM Company WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    companyId = rs.getInt("company_id");
                    return true;
                }
            }
        }
        return false;
    }

    public void postJob(Connection connection, Job job) throws SQLException {
        String sql = "INSERT INTO Job (job_id, title, description, requirements, deadline, company_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, job.getJobId());
            pstmt.setString(2, job.getTitle());
            pstmt.setString(3, job.getDescription());
            pstmt.setString(4, job.getRequirements());
            pstmt.setDate(5, new java.sql.Date(job.getDeadline().getTime()));
            pstmt.setInt(6, companyId);
            pstmt.executeUpdate();
        }
    }

    public List<Applicant> viewApplicants(Connection connection) {
        // Implement view applicants functionality
        return null;
    }

    public List<Application> viewApplications(Connection connection) {
        // Implement view applications functionality
        return null;
    }

    public int getCompanyId() {
        return companyId;
    }
}
