package learning_api.workout_api;

import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.support.IntegrationTestSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class WorkoutPlanIntegrationTest extends IntegrationTestSupport {

    @ParameterizedTest
    @CsvSource({
            "SEDENTARY, 10",
            "FIT, 15",
            "ATHLETE, 20"
    })
    void shouldGenerateSheetWithRepsBasedOnFitnessLevel(String fitnessLevel, int expectedReps) throws Exception {
        String email = uniqueEmail();
        String token = registerAndLogin(email, "secret123", FitnessLevel.valueOf(fitnessLevel));
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        long exerciseId = createExercise(token, "Agachamento", "Pernas");

        long planId = createWorkoutPlan(token, userId, "Leg Day", List.of(exerciseId));

        mockMvc.perform(get("/workout-plans/" + planId + "/sheet")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.planTitle").value("Leg Day"))
                .andExpect(jsonPath("$.userLevel").value(fitnessLevel))
                .andExpect(jsonPath("$.exercises", hasSize(1)))
                .andExpect(jsonPath("$.exercises[0].repetitions").value(expectedReps + " repetitions"));
    }

    @Test
    void shouldCreateWorkoutPlanAndAddExercises() throws Exception {
        String email = uniqueEmail();
        String token = registerAndLogin(email, "secret123", FitnessLevel.FIT);
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        long exerciseId1 = createExercise(token, "Supino", "Peito");
        long exerciseId2 = createExercise(token, "Remada", "Costas");
        long planId = createWorkoutPlan(token, userId, "Upper Body", List.of(exerciseId1));

        mockMvc.perform(post("/workout-plans/" + planId + "/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(List.of(exerciseId2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.exercises", hasSize(2)));

        mockMvc.perform(get("/workout-plans")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].title").value("Upper Body"));
    }

    @Test
    void shouldDeleteWorkoutPlan() throws Exception {
        String email = uniqueEmail();
        String token = registerAndLogin(email, "secret123", FitnessLevel.FIT);
        Long userId = userRepository.findByEmail(email).orElseThrow().getId();

        long planId = createWorkoutPlan(token, userId, "Cardio Day", List.of());

        mockMvc.perform(delete("/workout-plans/" + planId)
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/workout-plans")
                        .header("Authorization", bearerToken(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    private long createExercise(String token, String name, String description) throws Exception {
        var result = mockMvc.perform(post("/exercises")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Map.of(
                                "name", name,
                                "description", description
                        ))))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    private long createWorkoutPlan(String token, Long userId, String title, List<Long> exerciseIds) throws Exception {
        var body = Map.of(
                "title", title,
                "userId", userId,
                "exerciseIds", exerciseIds
        );

        var result = mockMvc.perform(post("/workout-plans")
                        .header("Authorization", bearerToken(token))
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }
}
