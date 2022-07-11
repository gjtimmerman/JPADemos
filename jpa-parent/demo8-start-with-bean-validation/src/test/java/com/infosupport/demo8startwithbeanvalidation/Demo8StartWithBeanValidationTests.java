package com.infosupport.demo8startwithbeanvalidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.persistence.*;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.*;

public class Demo8StartWithBeanValidationTests {
    private static final Logger logger = LoggerFactory.getLogger(Demo8StartWithBeanValidationTests.class);
    EntityTransaction etx;
    EntityManager em;
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        String persistenceUnitName = "jpa-hiber-postgres-pu";
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        etx = em.getTransaction();

    }

    void executeInTransaction(Consumer<EntityManager> consumer) {
        try {
            etx.begin();
            consumer.accept(em);
            etx.commit();
        } catch (Exception e) {
            if (etx != null)
                etx.rollback();
            throw new RuntimeException("Something unexpected went wrong", e);
        }

    }

    @Test
    void createPersonWithValidProperties()
    {
        Person p = new Person("Jan","Jansen", LocalDate.of(2020,10,12),3);
        executeInTransaction(em ->
        {
            em.persist(p);
        });
        Person p2 = em.find(Person.class,1);
        assertThat(p2.getFirstname()).isEqualTo("Jan");
        assertThat(p2.getLastname()).isEqualTo("Jansen");
        assertThat(p2.getBirthdate()).isEqualTo(LocalDate.of(2020,10,12));
        assertThat(p2.getNumberOfChildren()).isEqualTo(3);
    }
    @Test
    void createPersonWithInvalidFirstName()
    {
        Person p = new Person("","Jansen",LocalDate.of(1990,2,14),0);
        assertThatThrownBy(()->
        {
            executeInTransaction(em ->
            {
                em.persist(p);
            });
        }).hasCause(new RollbackException("Error while committing the transaction"));
    }
    @Test
    void createPersonWithInvalidLastName()
    {
        Person p = new Person("Jan","",LocalDate.of(1990,2,14),0);
        assertThatThrownBy(()->
        {
            executeInTransaction(em ->
            {
                em.persist(p);
            });
        }).hasCause(new RollbackException("Error while committing the transaction"));
    }
    @Test
    void createPersonWithInvalidBirthDate()
    {
        Person p = new Person("Jan","Jansen",LocalDate.now().plusYears(5),0);
        assertThatThrownBy(()->
        {
            executeInTransaction(em ->
            {
                em.persist(p);
            });
        }).hasCause(new RollbackException("Error while committing the transaction"));
    }
    @Test
    void createPersonWithInvalidNumberOfChildren()
    {
        Person p = new Person("","Jansen",LocalDate.of(1990,2,14),200);
        assertThatThrownBy(()->
        {
            executeInTransaction(em ->
            {
                em.persist(p);
            });
        }).hasCause(new RollbackException("Error while committing the transaction"));
    }

}
