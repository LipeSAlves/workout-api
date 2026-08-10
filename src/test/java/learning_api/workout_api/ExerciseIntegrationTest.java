package learning_api.workout_api;

import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.support.IntegrationTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ExerciseIntegrationTest extends IntegrationTestSupport {

    private String token;

    @BeforeEach
    void setUp() throws Exception {
        token = registerAndLogin(uniqueEmail(), "secret123", FitnessLevel.FIT);
    }

    @Test
    void shouldCreateExercise() throws Exception {
        mockMvc.perform(post("/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Agachamento",
                                "description", "Exercício para pernas"
                        ))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Agachamento"))
                .andExpect(jsonPath("$.description").value("Exercício para pernas"));
    }

    @Test
    void shouldListExercises() throws Exception {
        mockMvc.perform(post("/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Supino",
                                "description", "Exercício para peito"
                        ))))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/exercises")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Supino"));
    }

    @Test
    void shouldUpdateExercise() throws Exception {
        var createResult = mockMvc.perform(post("/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Rosca direta",
                                "description", "Exercício para bíceps"
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long exerciseId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(put("/exercises/" + exerciseId)
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Rosca alternada",
                                "description", "Variação para bíceps"
                        ))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Rosca alternada"))
                .andExpect(jsonPath("$.description").value("Variação para bíceps"));
    }

    @Test
    void shouldDeleteExercise() throws Exception {
        var createResult = mockMvc.perform(post("/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", "Leg press",
                                "description", "Exercício para pernas"
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        long exerciseId = objectMapper.readTree(createResult.getResponse().getContentAsString()).get("id").asLong();

        mockMvc.perform(delete("/exercises/" + exerciseId)
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/exercises")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
