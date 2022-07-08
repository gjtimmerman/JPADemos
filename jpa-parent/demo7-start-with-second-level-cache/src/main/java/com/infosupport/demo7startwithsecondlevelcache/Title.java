package com.infosupport.demo7startwithsecondlevelcache;

import javax.persistence.*;

@Entity
@Cacheable
public class Title {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @Version
    private Long version;
    public Title()
    {

    }
    public Title(String name)
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

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
