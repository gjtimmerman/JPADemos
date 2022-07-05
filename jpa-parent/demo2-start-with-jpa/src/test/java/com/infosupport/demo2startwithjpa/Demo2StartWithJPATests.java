package com.infosupport.demo2startwithjpa;

import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class Demo2StartWithJPATests {
    private static final Logger logger = LoggerFactory.getLogger(Demo2StartWithJPATests.class);
    private EntityManagerFactory emf;
    private EntityManager em;
    private EntityTransaction etx;

    @BeforeEach
    void setUp()
    {
        String persistenceUnitName = "jpa-hiber-postgres-pu";
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
        etx = em.getTransaction();

    }
    @Test
    @Order(1)
    void persistBookWithoutAtGeneratedValue()
    {
        logger.info("start persistBookWithoutAtGeneratedValue");
        etx.begin();
        em.persist(new Book("John", "Surviving in the Java logging Hell"));
        em.persist(new Book("Mary", "Surviving in the jungle"));
        etx.commit();
        logger.info("finish persistBookWithoutAtGeneratedValue");
    }
    @Test
    @Order(2)
    @DisplayName("Select all entities of type Book")
    void createJPQLQueryOnEntityBook()
    {
        int numberOfBooksSaved = 2;
        addBooksToTable(numberOfBooksSaved);
        String queryString ="select count(b) from Book b";
        Query jpqlQuery = em.createQuery(queryString,Long.class);
        List<Long> result = jpqlQuery.getResultList();
        int numberOfBooksRetrieved = (int)((Long)result.get(0)).longValue();
        assertThat(numberOfBooksRetrieved).isEqualTo(numberOfBooksSaved);
    }
    @Test
    @Order(3)
    @DisplayName("Select titles of books")
    void selectBookTitles()
    {
        addBooksToTable(3);
        String queryString = "select b.Title from Book b";
        Query jpqlQuery = em.createQuery(queryString,String.class);
        List<String> result = jpqlQuery.getResultList();
        int numberOfBooksRetrieved = result.size();
        assertThat(numberOfBooksRetrieved).isEqualTo(3);
    }
    @Test
    @Order(4)
    @DisplayName("Select top favorite book")
    void selectTopFavoriteBook()
    {
        addBooksToTable(3);
        String queryString = "select b.Title from Book b";
        Query jpqlQuery = em.createQuery(queryString,String.class);
        jpqlQuery.setMaxResults(1);
        List<String> result = jpqlQuery.getResultList();
        int numberOfBooksRetrieved = result.size();
        assertThat(numberOfBooksRetrieved).isEqualTo(1 );

    }
    @Test
    @Order(5)
    @DisplayName("Select book by id")
    void selectBookById()
    {
        addBooksToTable(5);
        String queryString = "select b from Book b where b. id = :id";
        Query jpqlQuery = em.createQuery(queryString,Book.class);
        jpqlQuery.setParameter("id", 3);
        List<Book> result = jpqlQuery.getResultList();
        int bookId = result.get(0).getId();
        assertThat(bookId).isEqualTo(3);
    }

    void addBooksToTable(int numberOfBooks)
    {
        logger.info("Saving books to table");
        etx.begin();
        for (int i = 0; i < numberOfBooks; i++)
        {
            Book b = new Book("John","The survival guide for programmers");
            em.persist(b);
        }
        etx.commit();
        logger.info("finished saving books to table");
    }
    @Nested
    @DisplayName("Experiment with different strategies")
    class ExperimentWithDifferentStrategies
    {
        @Test
        void insertSomeBooks() {
            logger.info("Insert some books");
            Book b = new Book("William", "Turn even numbers into odds");
            etx.begin();
            em.persist(b);
            b = new Book("Bruce","Change summer into winter");
            em.persist(b);
            etx.commit();
            logger.info("finished inserting some books");
            String queryString = "select b.Id from Book b";
            Query jpqlQuery = em.createQuery(queryString,Integer.class);
            List<Integer> result = jpqlQuery.getResultList();
            assertThat(result.get(0)).isEqualTo(1);
            Integer myInt = result.get(1);
            logger.info(myInt.toString());
            assertThat(result.get(1)).isEqualTo(2);
        }
        @Test
        void insertBooksAndRollbackSome()
        {
            for (int i = 0; i < 6; i++) {
                etx.begin();
                Book b = new Book("John", "Nice card games");
                em.persist(b);
                if (i % 2 == 0)
                    etx.commit();
                else
                    etx.rollback();
            }
            String queryString = "select b.Id from Book b";
            Query jpqlQuery = em.createQuery(queryString,Integer.class);
            List<Integer> result = jpqlQuery.getResultList();
            assertThat(result.get(0)).isEqualTo(1);
            assertThat(result.get(1)).isEqualTo(3);
            assertThat(result.get(2)).isEqualTo(5);


        }
    }
}
