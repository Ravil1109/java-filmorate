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
public class UserControllerTest extends BaseControllerTest {

    private static final String PATH = "/users";

    @Autowired
    private UserController controller;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createStatusOk() throws Exception {
        String requestBody = getContentFromFile("create/request/userOk.json");
        String responseBody = getContentFromFile("create/response/userOk.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(responseBody));
    }

    @Test
    void createStatusBadForBirthday() throws Exception {
        String requestBody = getContentFromFile("create/request/userBadForBirthday.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    void createStatusBadForName() throws Exception {
        String requestBody = getContentFromFile("create/request/userBadForName.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void createStatusBadForEmail() throws Exception {
        String requestBody = getContentFromFile("create/request/userBadForEmail.json");

        mockMvc.perform(MockMvcRequestBuilders.request(HttpMethod.POST, PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
