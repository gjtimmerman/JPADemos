package com.infosupport.demo8startwithbeanvalidation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.util.function.Consumer;

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
    }
}
