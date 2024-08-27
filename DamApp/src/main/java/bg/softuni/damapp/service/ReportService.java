package bg.softuni.damapp.service;

import bg.softuni.damapp.model.dto.ReportDTO;
import bg.softuni.damapp.model.entity.Report;

import java.util.List;

public interface ReportService {

    void saveReport(ReportDTO reportDTO);

    List<Report> getAllReports();
}
