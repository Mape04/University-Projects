package Interfaces;

import Domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<AppUser,Integer> {
    Optional<AppUser> findByUsername(String username);

    Optional<AppUser> findByEmail(String email);
}
