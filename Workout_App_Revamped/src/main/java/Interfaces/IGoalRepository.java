package Interfaces;

import Domain.Goal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IGoalRepository extends JpaRepository<Goal,Integer> {
}
