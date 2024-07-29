package bg.softuni.damapp.service.impl;

import bg.softuni.damapp.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportingServiceImplTest {

    private ReportingServiceImpl toTest;
    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void setUp() {
        toTest = new ReportingServiceImpl(
                mockUserRepository
        );
    }

    @Test
    void testReportUserRegistrations() {
        // Arrange
        long userCount = 10;
        when(mockUserRepository.count()).thenReturn(userCount);

        // Act
        toTest.reportUserRegistrations();

        // Assert
        verify(mockUserRepository).count();
    }

}
