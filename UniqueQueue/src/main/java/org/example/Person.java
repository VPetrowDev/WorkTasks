package org.example;

import java.util.Objects;

public class Person {
    public int age;
    public String name;
    public Person(int age, String name) {
        this.age = age;
        this.name = name;
    }

    @Override
    public String toString(){
        return age + ": " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(age, name);
    }
}
