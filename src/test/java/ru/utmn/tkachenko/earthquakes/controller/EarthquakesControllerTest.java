package ru.utmn.tkachenko.earthquakes.controller;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.utmn.tkachenko.earthquakes.model.Earthquake;
import ru.utmn.tkachenko.earthquakes.repository.EarthquakeJpaRepository;
import ru.utmn.tkachenko.earthquakes.service.EarthquakesJpaService;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.core.Is;

@WebMvcTest(controllers = EarthquakesController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class
)
@Import(EarthquakesJpaService.class)
@ActiveProfiles("JpaEngine")
public class EarthquakesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    EarthquakeJpaRepository repository;

    @Test
    void addTest() throws Exception {
        mvc.perform(
                post("/api/earthquakes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": "0001",
                                    "deep": 5000,
                                    "type": "mblg",
                                    "magnitude": 5,
                                    "state": "West Virginia"
                                }
                                """
                        ))
                .andExpect(status().isCreated());
    }

    @Test
    void getOneTest() throws Exception {
        Earthquake e1 = new Earthquake();
        e1.setId("0001");
        e1.setState("West Virginia");
        e1.setMagnitude(5.0);
        given(repository.findById("0001")).willReturn(Optional.of(e1));

        // Act + Assert
        mvc.perform(
                get("/api/earthquakes/0001")
                        .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.magnitude", Is.is(e1.getMagnitude())))
        .andExpect(jsonPath("$.state", Is.is(e1.getState())));
    }
}
