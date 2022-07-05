package com.infosupport.demo3startwithrelations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PublisherOneToMany {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    private String city;

    @OneToMany(mappedBy = "publisher")
    private List<EmployeeManyToOne> employees = new ArrayList<>();

    public PublisherOneToMany()
    {
    }
    public PublisherOneToMany(String name, String city)
    {
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<EmployeeManyToOne> getEmployees() {
        return employees;
    }

    public void setEmployees(List<EmployeeManyToOne> employees) {
        this.employees = employees;
    }
}
