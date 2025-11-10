package ru.utmn.tkachenko.earthquakes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.EarthquakeJpaRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EarthquakesJpaServiceTest {

    @Mock
    private EarthquakeJpaRepository repository;

    @InjectMocks
    private EarthquakesJpaService service;

    /*@BeforeEach
    void setUp() {
        repository = Mockito.mock(EarthquakeJpaRepository.class);
        service = new EarthquakesJpaService(repository);
    }*/

    Earthquake e1, e2;

    @BeforeEach
    void setUp() {
        e1 = new Earthquake();
        e1.setMagnitude(5.0);

        e2 = new Earthquake();
        e2.setMagnitude(7.0);
    }

    @Test
    void getAllTest() {
        // Arrange
        when(repository.findAll()).thenReturn(List.of(e1, e2));

        // Act
        List<Earthquake> result = (List<Earthquake>) service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        Earthquake e = result.get(0);
        assertEquals(5.0, e.getMagnitude());
    }

    @Test
    void deleteTest() {
        when(repository.existsById("1")).thenReturn(true);
        doNothing().when(repository).deleteById("1");

        // Act
        service.delete("1");

        verify(repository, times(1)).existsById("1");
        verify(repository, times(1)).deleteById("1");
        verify(repository, never()).findById("1");

        verifyNoMoreInteractions(repository);
    }

    @Test
    void updateNotFoundTest() {
        when(repository.existsById("999")).thenReturn(false);

        var e = new Earthquake();
        e.setId("999");

        ResponseStatusException ex = assertThrows(
                        ResponseStatusException.class,
                        () -> service.update(e));

        assertEquals(404, ex.getStatusCode().value());
        assertEquals("Запись не существует", ex.getReason());
        verify(repository, times(1)).existsById("999");
    }
}