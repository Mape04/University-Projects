package Repository.Interfaces;

import Domain.Referee;

public interface IRefereeRepository extends IRepository<Referee,Integer> {
    Referee getRefereeByName(String name);
    Referee findByNameAndPassword(String name, String password);
}
