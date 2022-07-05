package com.infosupport.demo3startwithrelations.manytomany.bidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class AuthorBi {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToMany(mappedBy="authors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Title> titles = new ArrayList<>();

    public AuthorBi()
    {

    }

    public AuthorBi(String name)
    {
        this.setName(name);
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

    public List<Title> getTitles() {
        return titles;
    }

    public void setTitles(List<Title> titles) {
        this.titles = titles;
    }
}
