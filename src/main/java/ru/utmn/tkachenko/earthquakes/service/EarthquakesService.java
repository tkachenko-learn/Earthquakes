package ru.utmn.tkachenko.earthquakes.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.CommonRepository;

import java.util.Collection;
import java.util.stream.StreamSupport;

@Service
@Profile({"CsvEngine", "JdbcEngine"})
public class EarthquakesService implements EarthquakesServiceInterface {

    CommonRepository<Earthquake> repository;

    public EarthquakesService(
            CommonRepository<Earthquake> repository,
            @Qualifier("CsvRepository") CommonRepository<Earthquake> repository2
    ) {
        this.repository = repository;

        if (repository2.getClass().equals(repository.getClass())) {
            return;
        }

        if (repository2.count() > 0 && repository.count() == 0) {
            Iterable<Earthquake> all = repository2.findAll();
            Collection<Earthquake> collection = StreamSupport.stream(all.spliterator(), false).toList();
            repository.save(collection);
        }
    }

    public Iterable<Earthquake> getAll() {
        return repository.findAll();
    }

    public Earthquake getOne(String id) {
        if (!repository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        return repository.findById(id);
    }

    public Earthquake add(Earthquake earthquake) {
        if (repository.exists(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запись создана ранее");
        return repository.save(earthquake);
    }

    public void update(Earthquake earthquake) {
        if (!repository.exists(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository.save(earthquake);
    }

    public void delete(String id) {
        if (!repository.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository.delete(id);
    }
}
