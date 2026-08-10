package learning_api.workout_api.repository.user;

import learning_api.workout_api.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE TRIM(u.resetPasswordToken) = TRIM(:token)")
    Optional<User> findByResetPasswordToken(@Param("token") String token);

}