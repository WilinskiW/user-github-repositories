package com.task.atiperatask;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
class UserRepositoriesIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${api.github.url}")
    private String apiUrl;

    @Test
    void shouldReturnSuccessResponse_whenUserExists() throws Exception {
        stubFor(get(urlEqualTo("/users/jankowalski/repos"))
                .willReturn(okJson("""
                    [
                      {
                        "name": "NoForkRepo",
                        "owner": { "login": "JanKowalski" },
                        "fork": false,
                        "branches_url": "%s/repos/jankowalski/noforkrepo/branches"
                      },
                      {
                        "name": "Forked",
                        "owner": { "login": "JanKowalski" },
                        "fork": true,
                        "branches_url": "any"
                      }
                    ]
                    """.formatted(apiUrl))));

        stubFor(get(urlEqualTo("/repos/jankowalski/noforkrepo/branches"))
                .willReturn(okJson("""
                    [
                      { "name": "main", "commit": { "sha": "abc123sha" } }
                    ]
                    """)));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/JanKowalski/repositories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1)) // Sprawdza czy fork został odfiltrowany
                .andExpect(jsonPath("$[0].name").value("NoForkRepo"))
                .andExpect(jsonPath("$[0].branches[0].sha").value("abc123sha"));
    }

    @Test
    void shouldReturn404_whenGithubUserNotFound() throws Exception {
        stubFor(get(urlEqualTo("/users/unknown/repos"))
                .willReturn(notFound().withBody(
                        """
                             {
                                    "message": "Not Found",
                                    "status": 404
                             }
                        """
                )));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/unknown/repositories"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Not Found"));
    }
}