package com.infosupport.demo2startwithjpa;

import javax.persistence.*;

@Entity
public class Book {
    public int getId() {
        return Id;
    }
    public void setId(int Id)
    {
        this.Id = Id;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "my_sequence")
    @SequenceGenerator(name="my_sequence")
    private int Id;
    private String Author;
    private String Title;
    public Book()
    {}
    public Book(String Author, String Title)
    {
        this.Author = Author;
        this.Title = Title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
