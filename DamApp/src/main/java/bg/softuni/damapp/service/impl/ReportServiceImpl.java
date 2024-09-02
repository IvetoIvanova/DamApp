package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.model.dto.ReportDTO;
import bg.softuni.damapp.model.entity.Report;
import bg.softuni.damapp.model.entity.User;
import bg.softuni.damapp.model.user.DamUserDetails;
import bg.softuni.damapp.repository.ReportRepository;
import bg.softuni.damapp.repository.UserRepository;
import bg.softuni.damapp.service.ReportService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;

    public ReportServiceImpl(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void saveReport(ReportDTO reportDTO) {
        Report report = new Report();
        report.setReportedUser(reportDTO.getReportedUser());
        report.setMessage(reportDTO.getMessage());
        report.setAdLink(reportDTO.getAdLink());

        User reportingUser = userRepository.findById(reportDTO.getReportingUser().getId())
                .orElseThrow(() -> new IllegalStateException("Invalid reporting user ID"));

        if (reportingUser == null) {
            throw new IllegalStateException("Reporting user is null!");
        }

        report.setReportingUser(reportingUser);
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public ReportDTO createReportDTOForCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        DamUserDetails userDetails = (DamUserDetails) authentication.getPrincipal();
        Optional<User> currentUser = userRepository.findByEmail(userDetails.getUsername());

        ReportDTO reportDTO = new ReportDTO();

        if (currentUser.isPresent()) {
            reportDTO.setReportingUser(currentUser.get());
        }else{
            throw new IllegalStateException("Потребителят не може да бъде намерен!");
        }
//        currentUser.ifPresent(reportDTO::setReportingUser);

        return reportDTO;
    }
}
