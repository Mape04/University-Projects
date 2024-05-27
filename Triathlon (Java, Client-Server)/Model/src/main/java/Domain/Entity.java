package Domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.io.Serial;
import java.io.Serializable;

@MappedSuperclass
public class Entity<Tid extends Serializable> implements Identifiable<Tid>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Tid Id;

    public Tid getId() {
        return Id;
    }

    public void setId(Tid id) {
        this.Id = id;
    }
}
