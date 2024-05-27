package Repository.Interfaces;

import Domain.Challenge;

public interface IChallengeRepository extends IRepository<Challenge, Integer> {
    Challenge findByName(String name);
}
