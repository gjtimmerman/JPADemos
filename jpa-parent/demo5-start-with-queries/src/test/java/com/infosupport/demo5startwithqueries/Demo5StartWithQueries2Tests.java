package com.infosupport.demo5startwithqueries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;


import com.infosupport.demo5startwithqueries.domain.mapping03tables.Author;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AuthorNumberOfRoyaltyPercentages
{
    private String firstName;
    private String lastName;
    private Long numberOfRoyaltyPercentages;
    public AuthorNumberOfRoyaltyPercentages(String firstName, String lastName, Long numberOfRoyaltyPercentages)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.numberOfRoyaltyPercentages = numberOfRoyaltyPercentages;
    }
    public String toString()
    {
        return firstName + " " + lastName + " has " + numberOfRoyaltyPercentages + " royalties";
    }
}


public class Demo5StartWithQueries2Tests {
    private static final Logger logger = LoggerFactory.getLogger(Demo5StartWithQueries2Tests.class);
    EntityTransaction etx;
    EntityManager em;
    EntityManagerFactory emf;


    @BeforeEach
    void setUp()
    {
        String persistenceUnitName = "jpa-hiber-postgres-pubs-mapping2";
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        etx = em.getTransaction();

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

    @Test
    void selectNumberOfRoyaltyPercentagesOfAuthors()
    {
        String queryString = "select new com.infosupport.demo5startwithqueries.AuthorNumberOfRoyaltyPercentages(r.author.firstname, r.author.lastname, count(r.royaltyPercentage)) from Royalty r  group by r.author, r.author.firstname, r.author.lastname";
        TypedQuery<AuthorNumberOfRoyaltyPercentages> query = em.createQuery(queryString, AuthorNumberOfRoyaltyPercentages.class);
        List<AuthorNumberOfRoyaltyPercentages> results = query.getResultList();
        assertThat(results.size()).isEqualTo(19);
        for (AuthorNumberOfRoyaltyPercentages anr: results)
            logger.info(anr.toString());
    }


    @Test
    void selectRoyaltyPercentageByAuthor()
    {
        String queryString = "select sum(r.royaltyPercentage) from Royalty r  where r.author.lastname = 'Green'";
        TypedQuery<BigDecimal> query = em.createQuery(queryString, BigDecimal.class);
        List<BigDecimal> results = query.getResultList();
        assertThat(results.get(0)).isEqualTo(new BigDecimal("140.00"));
        for (BigDecimal d: results)
            logger.info(d.toString());
    }

    @Test
    void selectSumOfTitlePriceByAuthor()
    {
        String queryString = "select sum(t.price) from Title t  join Royalty r on t = r.title join Author a on r.author = a where a.lastname = 'Green'";
        TypedQuery<BigDecimal> query = em.createQuery(queryString, BigDecimal.class);
        List<BigDecimal> results = query.getResultList();
        assertThat(results.get(0)).isEqualTo(new BigDecimal("22.98"));
        for (BigDecimal d: results)
            logger.info(d.toString());
    }

    @Test
    void selectSumOfTitlePriceByAuthor2()
    {
        String queryString = "select sum(r.title.price) from Author a, in (a.royaltiesPerAuthor) r where a.lastname = 'Green'";
        TypedQuery<BigDecimal> query = em.createQuery(queryString, BigDecimal.class);
        List<BigDecimal> results = query.getResultList();
        assertThat(results.get(0)).isEqualTo(new BigDecimal("22.98"));
        for (BigDecimal d: results)
            logger.info(d.toString());
    }

}
