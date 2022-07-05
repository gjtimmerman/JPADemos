package com.infosupport.demo3startwithrelations.manytomany.unidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class TitleWithList {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(name = "title_author_uni", joinColumns = {@JoinColumn(name="title_id")}, inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private List<Author> authors = new ArrayList<>();

    public TitleWithList()
    {

    }

    public TitleWithList(String name)
    {
        this.name = name;
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

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }
}
