package com.concurrency.util;

import lombok.Data;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

@Data
public class Person implements Comparable<Person> {

    private int id;
    private String firstName;
    private String lastName;
    private Date birthDate;
    private int salary;
    private double coeficient;

    private static Comparator<Person> comparator = Comparator.comparing(Person::getLastName)
            .thenComparing(Person::getFirstName);

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    @Override
    public int compareTo(Person otherPerson) {

        return Comparator.comparing(Person::getId)
                .thenComparing(Person::getFirstName)
                .thenComparing(Person::getLastName)
                .compare(this, otherPerson);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        int compareLastNames = this.getLastName().compareTo(person.getLastName());
        int compareFirstNames = this.getFirstName().compareTo(person.getFirstName());

        return id == person.id &&
                Objects.equals(lastName, person.lastName) &&
                (compareLastNames==0) && (compareFirstNames==0);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName);
    }
}
