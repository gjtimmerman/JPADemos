package com.infosupport.demo3startwithrelations;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PublisherOneToManyCascadeRemove {
        @Id
        @GeneratedValue
        private int id;
        private String name;
        private String city;

        @OneToMany(mappedBy = "publisher",cascade = CascadeType.ALL,orphanRemoval = true)
        private List<EmployeeManyToOneCascadeRemove> employees = new ArrayList<>();

        public PublisherOneToManyCascadeRemove()
        {
        }
        public PublisherOneToManyCascadeRemove(String name, String city)
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

        public List<EmployeeManyToOneCascadeRemove> getEmployees() {
            return employees;
        }

        public void setEmployees(List<EmployeeManyToOneCascadeRemove> employees) {
            this.employees = employees;
        }

}
