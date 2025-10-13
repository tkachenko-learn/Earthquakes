package ru.utmn.tkachenko.earthquakes.service;

import ru.utmn.tkachenko.earthquakes.model.Earthquake;

public interface EarthquakesServiceInterface {

    Iterable<Earthquake> getAll();

    Earthquake getOne(String id);

    Earthquake add(Earthquake earthquake);

    void update(Earthquake earthquake);

    void delete(String id);
}
