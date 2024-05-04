import java.sql.*;

public class Applicant {
    private int applicantId;
    private String name;
    private String email;
    private String password;
    private byte[] profilePicture;
    private String phone;
    private String skills;
    private byte[] CV;

    public Applicant(String name, String email, String password, String phone, String skills, byte[] profilePicture, byte[] CV) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.skills = skills;
        this.profilePicture = profilePicture;
        this.CV = CV;
    }

    // Getters and setters

    public void signup(Connection connection) throws SQLException {
        String sql = "INSERT INTO Applicant (applicant_id, name, email, password, phone, skills, profile_picture, CV) VALUES (applicant_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            pstmt.setString(4, phone);
            pstmt.setString(5, skills);
            pstmt.setBytes(6, profilePicture);
            pstmt.setBytes(7, CV);
            pstmt.executeUpdate();
        }
    }

    public boolean login(Connection connection) throws SQLException {
        String sql = "SELECT applicant_id FROM Applicant WHERE email = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    applicantId = rs.getInt("applicant_id");
                    return true;
                }
            }
        }
        return false;
    }

    public void applyForJob(Connection connection, Job job) throws SQLException {
        String sql = "INSERT INTO Application (posting_id, job_id, applicant_id) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, job.getPostingId());
            pstmt.setInt(2, job.getJobId());
            pstmt.setInt(3, applicantId);
            pstmt.executeUpdate();
        }
    }
    public int getApplicantId() {
        return applicantId;
    }
}
