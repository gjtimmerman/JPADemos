package com.infosupport.demo3startwithrelations.manytomany.bidirectional;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Title {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "title_author_bi", joinColumns = {@JoinColumn(name="title_id")}, inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private List<AuthorBi> authors = new ArrayList<>();

    public Title()
    {

    }

    public Title(String name)
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

    public List<AuthorBi> getAuthors() {
        return authors;
    }

    public void setAuthors(List<AuthorBi> authors) {
        this.authors = authors;
    }
}
