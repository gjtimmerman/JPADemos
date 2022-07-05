package com.infosupport.demo3startwithrelations.manytomany.bidirectional;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
public class AuthorWithMap {
    @Id
    @GeneratedValue
    private int id;
    private String name;

    @ManyToMany(mappedBy="authors", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapKey
    private Map<Integer,TitleWithMap> titles = new HashMap();

    public AuthorWithMap()
    {

    }

    public AuthorWithMap(String name)
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

    public Map<Integer, TitleWithMap> getTitles() {
        return titles;
    }

    public void setTitles(Map<Integer, TitleWithMap> titles) {
        this.titles = titles;
    }
}
