package com.infosupport.demo8startwithbeanvalidation;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

@Entity
public class Person {
    @Id
    @GeneratedValue
    private int id;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @Past
    private LocalDate birthdate;
    @Min(0)
    @Max(100)
    private int numberOfChildren;

    public Person()
    {

    }
    public Person(String firstname, String lastname, LocalDate birthdate, int numberOfChildren)
    {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.numberOfChildren = numberOfChildren;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public int getNumberOfChildren() {
        return numberOfChildren;
    }

    public void setNumberOfChildren(int numberOfChildren) {
        this.numberOfChildren = numberOfChildren;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
