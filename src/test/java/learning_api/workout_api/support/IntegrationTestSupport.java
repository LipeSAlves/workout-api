package learning_api.workout_api.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import learning_api.workout_api.domain.user.entity.FitnessLevel;
import learning_api.workout_api.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public abstract class IntegrationTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    protected String uniqueEmail() {
        return "user-" + UUID.randomUUID() + "@test.com";
    }

    protected Long registerUser(String name, String email, String password, FitnessLevel fitnessLevel) throws Exception {
        Map<String, Object> body = Map.of(
                "name", name,
                "email", email,
                "password", password,
                "fitnessLevel", fitnessLevel.name()
        );

        MvcResult result = mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(result.getResponse().getContentAsString()).get("id").asLong();
    }

    protected String login(String email, String password) throws Exception {
        Map<String, String> body = Map.of("email", email, "password", password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andReturn();

        JsonNode response = objectMapper.readTree(result.getResponse().getContentAsString());
        return response.get("accessToken").asText();
    }

    protected String registerAndLogin(String email, String password, FitnessLevel fitnessLevel) throws Exception {
        registerUser("Test User", email, password, fitnessLevel);
        return login(email, password);
    }

    protected String bearerToken(String token) {
        return "Bearer " + token;
    }
}
