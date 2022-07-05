package com.infosupport.demo3startwithrelations;

import javax.persistence.*;

@Entity
public class EmployeeManyToOneCascadeRemove {
    @Id
    @GeneratedValue
    private int id;
    private String firstName;
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    private PublisherOneToManyCascadeRemove publisher;

    public EmployeeManyToOneCascadeRemove()
    {

    }
    public EmployeeManyToOneCascadeRemove(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public PublisherOneToManyCascadeRemove getPublisher() {
        return publisher;
    }

    public void setPublisher(PublisherOneToManyCascadeRemove publisher) {
        this.publisher = publisher;
    }

}
