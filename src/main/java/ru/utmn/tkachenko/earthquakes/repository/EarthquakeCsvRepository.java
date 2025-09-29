package ru.utmn.tkachenko.earthquakes.repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Repository;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.service.EarthquakesService;

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

@Repository
public class EarthquakeCsvRepository implements CommonRepository<Earthquake> {

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

    @Override
    public Earthquake save(Earthquake domain) {
        earthquakes.put(domain.getId(), domain);
        return earthquakes.get(domain.getId());
    }

    @Override
    public Iterable<Earthquake> save(Collection<Earthquake> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(String id) {
        earthquakes.remove(id);
    }

    @Override
    public void delete(Earthquake domain) {
        delete(domain.getId());
    }

    @Override
    public Earthquake findById(String id) {
        return earthquakes.get(id);
    }

    @Override
    public Iterable<Earthquake> findAll() {
        return earthquakes.values();
    }

    @Override
    public boolean exists(String id) {
        return earthquakes.containsKey(id);
    }

    @Override
    public long count() {
        return earthquakes.size();
    }
}
