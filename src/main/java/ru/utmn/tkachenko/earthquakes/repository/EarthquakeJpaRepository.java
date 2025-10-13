package ru.utmn.tkachenko.earthquakes.repository;

import org.springframework.data.repository.CrudRepository;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;

public interface EarthquakeJpaRepository extends CrudRepository<Earthquake, String> {
}
