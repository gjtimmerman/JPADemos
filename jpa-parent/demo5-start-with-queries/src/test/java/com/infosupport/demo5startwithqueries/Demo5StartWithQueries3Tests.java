package com.infosupport.demo5startwithqueries;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

import com.infosupport.demo5startwithqueries.domain.mappingalltables.Author;

import static org.assertj.core.api.Assertions.assertThat;

public class Demo5StartWithQueries3Tests {
    private static final Logger logger = LoggerFactory.getLogger(Demo5StartWithQueries3Tests.class);
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
    @Test
    public void selectStoreNamesPerAuthor()
    {
        String queryString ="select st.name from Author a join Royalty r on a = r.author join Title t on r.title = t , in (t.sales) s join Store st on s.store = st where a.lastname ='Green' ";
        TypedQuery<String> query = em.createQuery(queryString,String.class);
        List<String> result = query.getResultList();
        assertThat(result.size()).isEqualTo(3);
        assertThat(result.get(0)).isEqualTo("Bookbeat");
        for(String s : result)
            logger.info(s);
    }
    @Test
    public void selectAuthorsForPublishersWithMoreThanXEmployees()
    {
        String queryString ="select a from Author a where a in ( select r.author from  Royalty r join Title t on t = r.title join Publisher p on t.publisher = p where (select count(e) from Employee e where e.publisher = p) = 10)";
        TypedQuery<Author> query = em.createQuery(queryString,Author.class);
        List<Author> result = query.getResultList();
        assertThat(result.size()).isEqualTo(19);
        for(Author a : result)
            logger.info(a.getLastname());
    }
    public List<String> selectAuthorPropertyBasedOnGivenProperty(String projection, String selection, Object value)
    {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Author> root = query.from(Author.class);
        List<String> result = em.createQuery(
                query.select(root.get(projection))
                        .where(cb.equal(root.get(selection),value))
        ).getResultList();
        return result;
    }

    @Test
    void selectLastNameOfAuthorsOnFirstName()
    {
        List<String> result = selectAuthorPropertyBasedOnGivenProperty("lastname","firstname","Johnson");
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualTo("White");
    }

    @Test
    void selectFirstNameOfAuthorsOnContract()
    {
        List<String> result = selectAuthorPropertyBasedOnGivenProperty("firstname","contract",new BigDecimal("1.0"));
        assertThat(result.size()).isEqualTo(19);
        assertThat(result.get(0)).isEqualTo("Johnson");
    }

}
