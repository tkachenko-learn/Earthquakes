package ru.utmn.tkachenko.earthquakes.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;

public interface EarthquakeJpaRepository extends CrudRepository<Earthquake, String> {

    // SQL
    @Query(value = "SELECT AVG(e.magnitude) FROM earthquake e", nativeQuery = true)
    Double avgMagnitude();

    // JPQL
    @Query("SELECT AVG(e.magnitude) FROM Earthquake e")
    Double getAvgMagnitude();
}
