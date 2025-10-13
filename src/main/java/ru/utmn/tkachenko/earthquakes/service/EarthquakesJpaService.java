package ru.utmn.tkachenko.earthquakes.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.EarthquakeJpaRepository;

@Service
public class EarthquakesJpaService {

    private final EarthquakeJpaRepository repository;

    public EarthquakesJpaService(EarthquakeJpaRepository repository) {
        this.repository = repository;
    }

    public Iterable<Earthquake> getAll() {
        return repository.findAll();
    }

    public Earthquake getOne(String id) {
        return repository.findById(id)
                .orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует")
                );
    }

    public Earthquake add(Earthquake earthquake) {
        earthquake.setId(null);
        return repository.save(earthquake);
    }

    public void update(Earthquake earthquake) {
        if (!repository.existsById(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository.save(earthquake);
    }

    public void delete(String id) {
        if (!repository.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository.deleteById(id);
    }
}
