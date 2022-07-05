package com.infosupport.demo4startwithinheritance;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
public class Student extends Person{
    private LocalDate enrollmentDate;
    public Student()
    {

    }
    public Student(String name, LocalDate enrollmentDate)
    {
        super(name);
        this.enrollmentDate = enrollmentDate;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}
