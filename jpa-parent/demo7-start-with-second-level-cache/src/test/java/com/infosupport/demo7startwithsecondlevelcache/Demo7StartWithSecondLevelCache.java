package com.infosupport.demo7startwithsecondlevelcache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class Demo7StartWithSecondLevelCache {
    private static final Logger logger = LoggerFactory.getLogger(Demo7StartWithSecondLevelCache.class);
    EntityTransaction etx;
    EntityManager em;
    EntityManagerFactory emf;

    @BeforeEach
    void setUp() {
        String persistenceUnitName = "jpa-hiber-postgres-pu";
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
 //       em = emf.createEntityManager();
 //       etx = em.getTransaction();

    }

    void executeInTransaction(Consumer<EntityManager> consumer) {
        try {
            em = emf.createEntityManager();
            etx = em.getTransaction();
            etx.begin();
            consumer.accept(em);
            etx.commit();
        } catch (Exception e) {
            if (etx != null)
                etx.rollback();
            throw new RuntimeException("Something unexpected went wrong", e);
        }
        finally {
            em.close();
        }

    }
    void insertHundredTitles()
    {
        executeInTransaction(em ->
        {
            for (int i = 0; i < 100; i++)
            {
                Title t = new Title("This is a very interesting book! part " + i);
                em.persist(t);
            }
        });
    }
    @Test
    void readingFromCache()
    {
//        insertHundredTitles();
        executeInTransaction(em ->
        {
            for (int i = 0; i < 10; i++) {
                Title t = em.find(Title.class,i+1);
                assertThat(t.getName()).isEqualTo("This is a very interesting book! part " + i);
            }
        });
        logger.info("Now reading from cache");
        executeInTransaction(em ->
        {
            for (int i = 0; i < 10; i++) {
                Title t = em.find(Title.class,i+1);
                assertThat(t.getName()).isEqualTo("This is a very interesting book! part " + i);
            }
        });
    }
}
