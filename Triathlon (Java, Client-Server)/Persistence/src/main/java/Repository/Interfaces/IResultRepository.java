package Repository.Interfaces;

import Domain.Challenge;
import Domain.Participant;
import Domain.Result;

public interface IResultRepository extends IRepository<Result, Integer> {
    Result getResult(int participantId, int challengeId);
    boolean hasResult(Participant participant, Challenge challenge);
    int getTotalPointsBySsn(String ssn);
}
