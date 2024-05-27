package Repository.Interfaces;

import Domain.Participant;

public interface IParticipantRepository extends IRepository<Participant, Integer> {
    int getTotalPointsById(Integer id);
    Participant findBySSN(String ssn);
}
