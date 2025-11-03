package ru.utmn.tkachenko.earthquakes.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("CsvEngine")
class EarthquakesServiceTest {

    // Arrange
    @Autowired
    EarthquakesService service;

    @Test
    void avgMagnitudeImplementTest() {
        // Act
        Double result = service.avgMagnitude();

        // Assert
        assertTrue(result > 0);
    }
}