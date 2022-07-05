package com.infosupport.demo3startwithrelations;

import javax.persistence.*;

@Entity
public class Publisher {
    @Id
//    @GeneratedValue
    private int id;
    private String name;
    private String city;

    @OneToOne
    @JoinColumn(name="id")
    @MapsId
    private PublisherInfo publisherInfo;

    public int getId() {
        return id;
    }

    public Publisher()
    {

    }
    public Publisher(String name, String city)
    {
 //       this.id = id;
        this.name = name;
        this.city = city;
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

    public PublisherInfo getPublisherInfo() {
        return publisherInfo;
    }

    public void setPublisherInfo(PublisherInfo publisherInfo) {
        this.publisherInfo = publisherInfo;
    }
}
