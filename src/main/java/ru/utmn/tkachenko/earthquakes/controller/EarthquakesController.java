package ru.utmn.tkachenko.earthquakes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.service.EarthquakesService;

import java.util.Collection;

@RestController
@RequestMapping("/api/earthquakes")
public class EarthquakesController {

    private final EarthquakesService earthquakesService;

    public EarthquakesController(EarthquakesService earthquakesService) {
        this.earthquakesService = earthquakesService;
    }

    @GetMapping
    public Collection<Earthquake> getAll() {
        return earthquakesService.getAll();
    }

    @GetMapping("/{id}")
    public Earthquake getOne(
             @PathVariable("id")  String id
    ) {
        return earthquakesService.getOne(id);
    }

    @PostMapping
    public ResponseEntity<Earthquake> add(
            @RequestBody Earthquake earthquake
    ) {
        Earthquake e = earthquakesService.add(earthquake);
        return new ResponseEntity<>(e, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    public void update(
            @RequestBody Earthquake earthquake
    ) {
        earthquakesService.update(earthquake);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id")  String id
    ) {
        earthquakesService.delete(id);
    }
}
