package com.infosupport.demo4startwithinheritance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class Demo4StartWithInheritanceTests {
    private static final Logger logger = LoggerFactory.getLogger(Demo4StartWithInheritanceTests.class);
    EntityTransaction etx;
    EntityManager em;
    EntityManagerFactory emf;

    @BeforeEach
    void setUp()
    {
        String persistenceUnitName = "jpa-hiber-postgres-pu";
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        etx = em.getTransaction();

    }
    void executeInTransaction(Consumer<EntityManager> consumer)
    {
        try {
            etx.begin();
            consumer.accept(em);
            etx.commit();
        }
        catch (Exception e)
        {
            if (etx != null)
                etx.rollback();
            throw new RuntimeException("Something unexpected went wrong",e);
        }
    }

    void addSomeStudentsAndTeachers()
    {
        Student st1 = new Student("Hans", LocalDate.of(2021,8,20));
        Student st2 = new Student("Marie", LocalDate.of(2020,2,14));
        Teacher t1 = new Teacher("Willem","Mathematics");
        t1.getSubject().add("Physics");
        Teacher t2 = new Teacher("Els","English");
        t2.getSubject().add("Literature");
        executeInTransaction(em -> {
            em.persist(st1);
            em.persist(st2);
            em.persist(t1);
            em.persist(t2);
        });
    }
    @Test
    void listAllPersons()
    {
        addSomeStudentsAndTeachers();
        String queryString = "select p from Person p";
        Query query = em.createQuery(queryString,Person.class);
        List<Person> result = query.getResultList();
        assertThat(result.size()).isEqualTo(4);
    }

}
