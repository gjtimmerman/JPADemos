package com.infosupport.demo5startwithqueries;

import com.infosupport.demo5startwithqueries.domain.mapping03tables.Author;

import com.infosupport.demo5startwithqueries.domain.mapping01table.FullName;
import org.apache.logging.log4j.core.pattern.FullLocationPatternConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;


public class Demo5StartWithQueriesTests {
    private static final Logger logger = LoggerFactory.getLogger(Demo5StartWithQueriesTests.class);
    EntityTransaction etx;
    EntityManager em;
    EntityManagerFactory emf;

    @BeforeEach
    void setUp()
    {
        String persistenceUnitName = "jpa-hiber-postgres-pubs-mapping3";
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
    }@Test
    void selectAllAuthorsUsingJPQL()
    {
        String queryString = "select a from Author a";
        TypedQuery<Author> query = em.createQuery(queryString,Author.class);
        List<Author> results = query.getResultList();
        assertThat(results.size()).isEqualTo(23);
        for (Author a : results)
            logger.info(a.toString());
    }
    @Test
    void selectAllAuthorsHavingContractOf1UsingJPQL()
    {
        String queryString = "select a from Author a where a.contract = 1.0";
        TypedQuery<Author> query = em.createQuery(queryString,Author.class);
        List<Author> results = query.getResultList();
        assertThat(results.size()).isEqualTo(19);
        for (Author a : results)
            logger.info(a.toString());
    }
    @Test
    void selectFirstAndLastNamesOfAllAuthorsUsingJPQL()
    {
        String queryString = "select a.firstname,a.lastname from Author a";
        Query query = em.createQuery(queryString);
        List<Object[]> results = query.getResultList();
        assertThat(results.get(0)[0].toString()).isEqualTo("Johnson");
        assertThat(results.get(0)[1].toString()).isEqualTo("White");
        for (Object [] o : results)
            logger.info(o[0].toString() + " " + o[1].toString());
    }

    @Test
    void selectFullNameOfAllAuthorsUsingJPQL()
    {
        String queryString = "select new com.infosupport.demo5startwithqueries.domain.mapping01table.FullName(a.firstname,a.lastname) from Author a";
        TypedQuery<FullName> query = em.createQuery(queryString,FullName.class);
        List<FullName> results = query.getResultList();
        assertThat(results.get(0).toString()).isEqualTo("Johnson White");
        for (FullName f : results)
            logger.info(f.toString());
    }

    @Test
    void selectAuthorsByRoyaltyPercentage()
    {
        String queryString = "select distinct a from Author a join Royalty r on a.id = r.authorId where r.royaltyPercentage > 50";
        TypedQuery<Author> query = em.createQuery(queryString,Author.class);
        List<Author> results = query.getResultList();
        assertThat(results.size()).isEqualTo(13);
        for (Author a: results)
            logger.info(a.getFirstname());
    }

}
