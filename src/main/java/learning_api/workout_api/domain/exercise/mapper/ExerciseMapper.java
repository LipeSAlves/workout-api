package learning_api.workout_api.domain.exercise.mapper;

import learning_api.workout_api.domain.exercise.dto.ExerciseRequestDTO;
import learning_api.workout_api.domain.exercise.dto.ExerciseResponseDTO;
import learning_api.workout_api.domain.exercise.entity.Exercise;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ExerciseMapper {

    // vai transformar a Entity no DTO de resposta
    public ExerciseResponseDTO toResponseDTO (Exercise entity) {
        return new ExerciseResponseDTO(
                entity.getId(),
                entity.getName(),
                entity.getDescription()
        );
    }

    // Transforma o DTO de requisição na Entity (para salvar)
    public Exercise toEntity(ExerciseRequestDTO request) {
        Exercise entity = new Exercise();
        entity.setName(request.name());
        entity.setDescription(request.description());
        return entity;
    }
}
