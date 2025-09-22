package ru.utmn.tkachenko.earthquakes.service;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;

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


@Service
public class EarthquakesService {

    private final Map<String, Earthquake> earthquakes = new HashMap<>();

    @PostConstruct
    private void readAllLines() {
        try (
                InputStream is = EarthquakesService.class.getResourceAsStream("/Землетрясения.csv");
                InputStreamReader streamReader = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(streamReader);
                CSVReader csvReader = new CSVReaderBuilder(reader)
                                    .withSkipLines(1)
                                    .build();
        ) {
            List<String[]> lines = csvReader.readAll();

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("[yyyy-MM-dd'T'HH:mm:ss.SSS'Z'][yyyy-MM-dd HH:mm:ss][yyyy-MM-dd H:mm:ss]");
            for (String[] line : lines) {
                String id = line[0];
                if (id == null)
                    continue;
                Earthquake earthquake = new Earthquake();
                earthquake.setId(id);
                earthquake.setDeep(line[1] != null ? Integer.parseInt(line[1]) : null);
                earthquake.setType(line[2]);
                earthquake.setMagnitude(line[3] != null ? Double.parseDouble(line[3]) : null);
                earthquake.setState(line[4]);
                LocalDateTime dateTime = line[5] != null
                    ? LocalDateTime.parse(line[5], formatter)
                    : null;
                earthquake.setDateTime(dateTime);

                earthquakes.put(id, earthquake);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public Collection<Earthquake> getAll() {
        return earthquakes.values();
    }

    public Earthquake getOne(String id) {
        if (!earthquakes.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        return earthquakes.get(id);
    }

    public Earthquake add(Earthquake earthquake) {
        if (earthquakes.containsKey(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Запись создана ранее");
        earthquakes.put(earthquake.getId(), earthquake);
        return earthquake;
    }

    public void update(Earthquake earthquake) {
        if (!earthquakes.containsKey(earthquake.getId()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        earthquakes.put(earthquake.getId(), earthquake);
    }

    public void delete(String id) {
        if (!earthquakes.containsKey(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Запись не существует");
        earthquakes.remove(id);
    }
}
