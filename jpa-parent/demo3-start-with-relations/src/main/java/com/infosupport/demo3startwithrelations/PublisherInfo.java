package com.infosupport.demo3startwithrelations;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class PublisherInfo {
    @Id
    @GeneratedValue
    private
    int id;
    private byte [] logo;
    private String info;

    @OneToOne(mappedBy = "publisherInfo")
    private Publisher publisher;

    public PublisherInfo()
    {
    }

    public PublisherInfo(String info, byte[] logo)
    {
 //       this.id = id;
        this.info = info;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }
}
