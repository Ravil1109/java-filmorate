package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
public class FilmControllerTest extends BaseControllerTest {

    private static final String PATH = "/films";

    @Autowired
    private FilmController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createStatusOk() throws Exception {
        String requestBody = getContentFromFile("create/request/filmOk.json");
        String responseBody = getContentFromFile("create/response/filmOk.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseBody));
    }

    @Test
    void createStatusBad() throws Exception {
        String requestBody = getContentFromFile("create/request/filmBadForReleaseDate.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void createStatusBadForName() throws Exception {
        String requestBody = getContentFromFile("create/request/filmBadForName.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStatusBadForDuration() throws Exception {
        String requestBody = getContentFromFile("create/request/filmBadForDuration.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStatusBadForDescription() throws Exception {
        String requestBody = getContentFromFile("create/request/filmBadForDescription.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
