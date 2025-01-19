package Interfaces;

import Domain.ExerciseType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IExerciseTypeRepository extends JpaRepository<ExerciseType,Integer> {
}
