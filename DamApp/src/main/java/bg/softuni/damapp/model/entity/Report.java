package bg.softuni.damapp.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
public class Report extends BaseEntity {

    @Column(nullable = false)
    private String reportingUserFullName;
    @Column(nullable = false)
    private String reportedUser;
    @Column(nullable = false)
    private String message;
    @Column(nullable = false)
    private String adLink;
    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Report() {
    }

    public Report(String reportedUser, String message, String adLink) {
        this.reportedUser = reportedUser;
        this.message = message;
        this.adLink = adLink;
    }

    public String getReportedUser() {
        return reportedUser;
    }

    public void setReportedUser(String reportedUser) {
        this.reportedUser = reportedUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAdLink() {
        return adLink;
    }

    public void setAdLink(String adLink) {
        this.adLink = adLink;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public String getReportingUserFullName() {
        return reportingUserFullName;
    }

    public void setReportingUserFullName(String reportingUserFullName) {
        this.reportingUserFullName = reportingUserFullName;
    }
}
