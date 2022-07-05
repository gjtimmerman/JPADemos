package com.infosupport.demo4startwithinheritance;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Teacher extends Person{
    @ElementCollection
    private List<String> subject = new ArrayList<>();
    public Teacher()
    {

    }
    public Teacher(String name, String subject)
    {
        super(name);
        this.subject.add(subject);
    }

    public List<String> getSubject() {
        return subject;
    }

    public void setSubject(List<String> subject) {
        this.subject = subject;
    }
}
