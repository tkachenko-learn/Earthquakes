package ru.utmn.tkachenko.earthquakes.service;

import jakarta.annotation.PostConstruct;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.CommonRepository;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("CsvEngine")
class EarthquakesServiceTest {

    // Arrange
    @Autowired
    @Qualifier("EarthquakesServiceTest")
    EarthquakesService service;

    @TestConfiguration
    static class EarthquakesServiceImplTestContextConfiguration {

        static class MockRepository implements CommonRepository<Earthquake> {

            @Override
            public Earthquake save(Earthquake domain) {
                return null;
            }

            @Override
            public Iterable<Earthquake> save(Collection<Earthquake> domains) {
                return null;
            }

            @Override
            public void delete(String id) {}

            @Override
            public void delete(Earthquake domain) { }

            @Override
            public Earthquake findById(String id) {
                return null;
            }

            @Override
            public Iterable<Earthquake> findAll() {
                var e1 = new Earthquake();
                e1.setMagnitude(5.0);

                var e2 = new Earthquake();
                e2.setMagnitude(7.0);

                return List.of(e1, e2);
            }

            @Override
            public boolean exists(String id) {
                return false;
            }

            @Override
            public long count() {
                return 0;
            }
        }

        @Bean
        @Qualifier("EarthquakesServiceTest")
        public EarthquakesService getEarthquakesService() {
            return new EarthquakesService(null, null) {
                public void init(CommonRepository<Earthquake> repository2) {
                    repository = new MockRepository();
                }
            };
        }
    }

    @Test
    void avgMagnitudeImplementTest() {
        // Act
        Double result = service.avgMagnitude();

        // Assert
        assertEquals(result, 6.0, 0.00000001);
    }
}