package Domain;

import java.util.Objects;

public class Patient extends Entity{
    private static final long serialVersionUID = 1000L;
    private String lastName;
    private String firstName;
    private int age;

    public Patient(int id, String lastName, String firstName, int age) {
        super(id);
        this.lastName = lastName;
        this.firstName = firstName;
        this.age = age;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age=" + age +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return this.getId() == patient.getId() && Objects.equals(this.getLastName(), patient.getLastName()) && Objects.equals(this.getFirstName(), patient.getFirstName()) && this.age == patient.getAge();
    }
}
