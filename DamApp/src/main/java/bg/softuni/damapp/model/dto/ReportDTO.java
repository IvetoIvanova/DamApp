package bg.softuni.damapp.model.dto;

import jakarta.validation.constraints.NotBlank;

public class ReportDTO {

    @NotBlank(message = "{validation.not_empty}")
    private String reportedUser;
    @NotBlank(message = "{validation.not_empty}")
    private String message;
    @NotBlank(message = "{validation.not_empty}")
    private String adLink;
    @NotBlank
    private String reportingUserFullName;

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

    public String getReportingUserFullName() {
        return reportingUserFullName;
    }

    public void setReportingUserFullName(String reportingUserFullName) {
        this.reportingUserFullName = reportingUserFullName;
    }
}
