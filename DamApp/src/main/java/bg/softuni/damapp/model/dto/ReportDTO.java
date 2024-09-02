package bg.softuni.damapp.model.dto;

import bg.softuni.damapp.model.entity.User;
import jakarta.validation.constraints.NotBlank;

public class ReportDTO {

    @NotBlank(message = "{validation.not_empty}")
    private String reportedUser;
    @NotBlank(message = "{validation.not_empty}")
    private String message;
    @NotBlank(message = "{validation.not_empty}")
    private String adLink;
    private User reportingUser;

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(String reportedUser) {
        this.reportedUser = reportedUser;
    }

    public User getReportingUser() {
        return reportingUser;
    }

    public void setReportingUser(User reportingUser) {
        this.reportingUser = reportingUser;
    }
}
