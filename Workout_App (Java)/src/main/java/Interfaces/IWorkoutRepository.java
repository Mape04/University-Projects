package Interfaces;

import Domain.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IWorkoutRepository extends JpaRepository<Workout, Integer> {
    // Custom query methods (if needed) can be defined here
    // Example: List<Workout> findByName(String name);

    @Query("SELECT w FROM Workout w JOIN w.users u WHERE u.id = :userId")
    List<Workout> findByUsers_Id(@Param("userId") Integer userId);

    @Query("SELECT w FROM Workout w JOIN w.exercises e WHERE e.id = :exerciseId")
    Optional<Workout> findByExercisesContaining(@Param("exerciseId") Integer exerciseId);
}