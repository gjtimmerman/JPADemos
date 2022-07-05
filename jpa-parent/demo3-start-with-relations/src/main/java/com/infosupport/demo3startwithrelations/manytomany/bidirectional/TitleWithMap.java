package com.infosupport.demo3startwithrelations.manytomany.bidirectional;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class TitleWithMap {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "titlewithmap_authorwithmap_bi", joinColumns = {@JoinColumn(name="title_id")}, inverseJoinColumns = {@JoinColumn(name = "author_id")})
    @MapKey
    private Map<Integer,AuthorWithMap> authors = new HashMap();

    public TitleWithMap()
    {

    }

    public TitleWithMap(String name)
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

    public Map<Integer, AuthorWithMap> getAuthors() {
        return authors;
    }

    public void setAuthors(Map<Integer, AuthorWithMap> authors) {
        this.authors = authors;
    }
}
