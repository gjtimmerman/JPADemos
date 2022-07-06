package com.infosupport.demo5startwithqueries.domain.mapping01table;

public class FullName
{
    public FullName(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }
    private String firstName;
    private String lastName;

    public String toString()
    {
        return firstName + ' ' + lastName;
    }
}
