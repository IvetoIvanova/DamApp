package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ReportDTO;
import bg.softuni.damapp.model.entity.Report;
import bg.softuni.damapp.repository.ReportRepository;
import bg.softuni.damapp.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Override
    public void saveReport(ReportDTO reportDTO) {
        Report report = new Report();
        report.setReportedUser(reportDTO.getReportedUser());
        report.setMessage(reportDTO.getMessage());
        report.setAdLink(reportDTO.getAdLink());
        report.setReportingUserFullName(reportDTO.getReportingUserFullName());
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }
}
