package com.infosupport.demo3startwithrelations.manytomany.unidirectional;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class TitleWithSet {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToMany
    @JoinTable(name = "titleSet_author_uni", joinColumns = {@JoinColumn(name="title_id")}, inverseJoinColumns = {@JoinColumn(name = "author_id")})
    private Set<Author> authors = new HashSet<>();

    public TitleWithSet()
    {

    }

    public TitleWithSet(String name)
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

    public Set<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<Author> authors) {
        this.authors = authors;
    }
}
