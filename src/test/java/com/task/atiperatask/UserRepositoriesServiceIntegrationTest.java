package com.task.atiperatask;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.task.atiperatask.api.UserRepositoriesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@WireMockTest(httpPort = 8080)
public class UserRepositoriesServiceIntegrationTest {
    @Autowired
    private UserRepositoriesService service;

    @Value("${api.github.url}")
    private String apiUrl;


    @Test
    void shouldFetchGithubRepositoriesWithBranches_whenUserExist() {
        stubFor(get(urlEqualTo("/users/jankowalski/repos"))
                .willReturn(okJson(
                        """
                                [
                                    {
                                        "name": "NoForkRepo",
                                        "owner": {
                                                    "login": "JanKowalski"
                                                 },
                                        "fork": false,
                                        "branches_url": "%s/repos/jankowalski/noforkrepo/branches"
                                    },
                                    {
                                        "name": "ForkRepo",
                                        "owner": {
                                                    "login": "JanKowalski"
                                                 },
                                        "fork": true,
                                        "branches_url": "%s/repos/jankowalski/forkrepo/branches"
                                    }
                                ]
                                """.formatted(apiUrl, apiUrl)
                )));

        stubFor(get(urlEqualTo("/repos/jankowalski/noforkrepo/branches"))
                .willReturn(okJson(
                        """
                              [
                                {
                                  "name": "main",
                                  "commit": {
                                    "sha": "9f7497153c7bc04daeb8d7ebb7e1e772809f8b35"
                                  }
                                }
                              ]
                              """
                )));

        var results = service.getUserRepositories("JanKowalski");

        assertEquals(1, results.size());

        assertEquals("NoForkRepo", results.getFirst().name());
        assertEquals("JanKowalski", results.getFirst().owner());

        assertEquals(1, results.getFirst().branches().size());
        assertEquals("main", results.getFirst().branches().getFirst().name());
        assertEquals("9f7497153c7bc04daeb8d7ebb7e1e772809f8b35", results.getFirst().branches().getFirst().sha());
    }

    @Test
    void shouldReturnNotFound_whenUserDoesntExist() {
        stubFor(get(urlEqualTo("/users/joedoe/repos"))
                .willReturn(notFound().withBody(
                        """
                           {
                              "message": "Not Found",
                              "documentation_url": "https://docs.github.com/rest/repos/repos#list-repositories-for-a-user",
                              "status": "404"
                           }
                        """
                )));

        assertThrows(HttpClientErrorException.class, () -> service.getUserRepositories("joedoe"));
    }
}
