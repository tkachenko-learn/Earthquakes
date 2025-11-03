package ru.utmn.tkachenko.earthquakes.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.service.EarthquakesServiceInterface;

@RestController
@RequestMapping("/api/earthquakes")
public class EarthquakesController {

    private final EarthquakesServiceInterface earthquakesService;

    public EarthquakesController(EarthquakesServiceInterface earthquakesService) {
        this.earthquakesService = earthquakesService;
    }

    @Operation(summary = "Возвращает все записи", description = "Может работать медленно из-за отсутсвия пагинации")
    @GetMapping
    public Iterable<Earthquake> getAll() {
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

    @GetMapping("/avg")
    public Double avgMagnitude() {
        return earthquakesService.avgMagnitude();
    }
}
