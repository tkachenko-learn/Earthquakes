package ru.utmn.tkachenko.earthquakes.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.EarthquakeCsvRepository;
import ru.utmn.tkachenko.earthquakes.repository.EarthquakeJdbcRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;

//@Service
public class EarthquakesService {

    EarthquakeCsvRepository repository;
    EarthquakeJdbcRepository repository2;

    public EarthquakesService(EarthquakeCsvRepository repository, EarthquakeJdbcRepository repository2) {
        this.repository = repository;
        this.repository2 = repository2;
        if (repository2.count() == 0 && repository.count() > 0) {
            Iterable<Earthquake> all = repository.findAll();
            Collection<Earthquake> collection = StreamSupport.stream(all.spliterator(), false).toList();
            repository2.save(collection);
        }
    }

    public Iterable<Earthquake> getAll() {
        return repository2.findAll();
    }

    public Earthquake getOne(String id) {
        if (!repository2.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        return repository2.findById(id);
    }

    public Earthquake add(Earthquake earthquake) {
        if (repository2.exists(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запись создана ранее");
        return repository2.save(earthquake);
    }

    public void update(Earthquake earthquake) {
        if (!repository2.exists(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository2.save(earthquake);
    }

    public void delete(String id) {
        if (!repository2.exists(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        repository2.delete(id);
    }
}
