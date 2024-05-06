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
    @Override
    public String toString() {
        return title;
    }

    public String getId() {
        return jobid;
    }
    public String getDescription() {
        return description;
    }
    public String gettitle() {
        return title;
    }
    public String getrequirements() {
        return requirements;
    }
    public String getdeadline() {
        return deadline;
    }

}
