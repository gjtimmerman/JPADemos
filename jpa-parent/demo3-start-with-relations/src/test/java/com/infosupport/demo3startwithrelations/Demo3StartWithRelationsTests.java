package com.infosupport.demo3startwithrelations;

import com.infosupport.demo3startwithrelations.manytomany.unidirectional.Author;
import com.infosupport.demo3startwithrelations.manytomany.unidirectional.TitleWithList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

public class Demo3StartWithRelationsTests {
    private static final Logger logger = LoggerFactory.getLogger(Demo3StartWithRelationsTests.class);
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
    @Test
    void insertPublisherAndPublisherInfo()
    {
        Publisher p = new Publisher("Jan","Amsterdam");
        PublisherInfo pi = new PublisherInfo("Hele goede uitgeverij",null);
        pi.setPublisher(p);
        p.setPublisherInfo(pi);
        executeInTransaction(
                em ->{
                    em.persist(p);
                    em.persist(pi);
                }
        );
        PublisherInfo pi2 = em.find(PublisherInfo.class,1);
        Publisher p2 = pi2.getPublisher();
        assertThat(p2.getId()).isEqualTo(1 );
    }
    void insertPublisherOneToManyAndEmployeeManyToOne()
    {
        PublisherOneToMany pom1 = new PublisherOneToMany("Bruna","Utrecht");
        PublisherOneToMany pom2 = new PublisherOneToMany("De Bezige Bij","Amsterdam");
        PublisherOneToMany pom3 = new PublisherOneToMany("Broekhuis","Zwolle");
        PublisherOneToMany pom4 = new PublisherOneToMany("Lemniscaat","Den Haag");
        PublisherOneToMany pom5 = new PublisherOneToMany("Querido","Zoetermeer");
        PublisherOneToMany pom6 = new PublisherOneToMany("Kluitman","Leeuwarden");
        EmployeeManyToOne emp1 = new EmployeeManyToOne("Jan","Willemsen");
        pom1.getEmployees().add(emp1);
        emp1.setPublisher(pom1);
        EmployeeManyToOne emp2 = new EmployeeManyToOne("Piet","Jansen");
        pom1.getEmployees().add(emp2);
        emp2.setPublisher(pom1);
        EmployeeManyToOne emp3 = new EmployeeManyToOne("Marie","van den Berg");
        pom2.getEmployees().add(emp3);
        emp3.setPublisher(pom2);
        EmployeeManyToOne emp4 = new EmployeeManyToOne("Marie","van den Berg");
        emp4.setPublisher(pom3);
        EmployeeManyToOne emp5 = new EmployeeManyToOne("Marie","van den Berg");
        emp5.setPublisher(pom3);
        EmployeeManyToOne emp6 = new EmployeeManyToOne("Marie","van den Berg");
        emp6.setPublisher(pom3);
        EmployeeManyToOne emp7 = new EmployeeManyToOne("Marie","van den Berg");
        emp7.setPublisher(pom3);
        EmployeeManyToOne emp8 = new EmployeeManyToOne("Marie","van den Berg");
        emp8.setPublisher(pom3);
        EmployeeManyToOne emp9 = new EmployeeManyToOne("Marie","van den Berg");
        emp9.setPublisher(pom3);
        EmployeeManyToOne emp10 = new EmployeeManyToOne("Marie","van den Berg");
        emp10.setPublisher(pom3);
        EmployeeManyToOne emp11= new EmployeeManyToOne("Marie","van den Berg");
        emp11.setPublisher(pom4);
        EmployeeManyToOne emp12 = new EmployeeManyToOne("Marie","van den Berg");
        emp12.setPublisher(pom6);
        EmployeeManyToOne emp13 = new EmployeeManyToOne("Marie","van den Berg");
        emp13.setPublisher(pom6);

        executeInTransaction(
                em ->{
                    em.persist(pom1);
                    em.persist(pom2);
                    em.persist(pom3);
                    em.persist(pom4);
                    em.persist(pom5);
                    em.persist(pom6);
                    em.persist(emp1);
                    em.persist(emp2);
                    em.persist(emp3);
                    em.persist(emp4);
                    em.persist(emp5);
                    em.persist(emp6);
                    em.persist(emp7);
                    em.persist(emp8);
                    em.persist(emp9);
                    em.persist(emp10);
                    em.persist(emp11);
                    em.persist(emp12);
                    em.persist(emp13);
                }
        );
    }
    @Test
    void insertPublisherOneToManyAndEmployeeManyToOneTest()
    {
        insertPublisherOneToManyAndEmployeeManyToOne();
        PublisherOneToMany pomret = em.find(PublisherOneToMany.class,1);
        assertThat(pomret.getId()).isEqualTo(1);
        assertThat(pomret.getEmployees().size()).isEqualTo(2);
    }
    @Test
    void queryEmployeesWithPublishers()
    {
        insertPublisherOneToManyAndEmployeeManyToOne();
        em.clear();
        String queryString = "select e from EmployeeManyToOne e join fetch e.publisher";
        Query query = em.createQuery(queryString,EmployeeManyToOne.class);
        List<EmployeeManyToOne> result = query.getResultList();
        assertThat(result.get(0).getPublisher().getName()).isEqualTo("Bruna");
        assertThat(result.get(1).getPublisher().getName())
                .isEqualTo("Bruna");
        assertThat(result.get(2).getPublisher().getName()).isEqualTo("De Bezige Bij");
    }
    @Test
    void queryEmployeesAndPublishersWithPaginationOnBoth()
    {
        insertPublisherOneToManyAndEmployeeManyToOne();
        int publisherPageSize = 2;
        int employeePageSize = 5;
        int publisherStartRow = 0;
        String queryStringPublisher = "select p from PublisherOneToMany p";
        String queryStringEmployee = "select e from EmployeeManyToOne e where e.publisher= ?1";
        List<PublisherOneToMany> partialResults = null;
        do {
            Query query = em.createQuery(queryStringPublisher,PublisherOneToMany.class);
            query.setFirstResult(publisherStartRow);
            query.setMaxResults(publisherPageSize);
            partialResults = query.getResultList();
            for (PublisherOneToMany p : partialResults)
            {
                int employeeStartRow = 0;
                List<EmployeeManyToOne> partialEmpResults = null;
                do {
                    Query queryEmp = em.createQuery(queryStringEmployee, EmployeeManyToOne.class);
                    queryEmp.setParameter(1, p);
                    queryEmp.setFirstResult(employeeStartRow);
                    queryEmp.setMaxResults(employeePageSize);
                    partialEmpResults = queryEmp.getResultList();
                    logger.info("Number of Employees: " + partialEmpResults.size());
                    assertThat(partialEmpResults.size() <= employeePageSize);
                    employeeStartRow += employeePageSize;
                } while(partialEmpResults.size() == employeePageSize);
            }
            publisherStartRow += publisherPageSize;
            logger.info("Number of Publishers: " + partialResults.size());
            assertThat(partialResults.size() <= publisherPageSize);
        } while (partialResults.size() == publisherPageSize);
    }
    void insertPublisherOneToManyCascadeRemoveAndEmployeeManyToOneCascadeRemove()
    {
        PublisherOneToManyCascadeRemove pom1 = new PublisherOneToManyCascadeRemove("Bruna","Utrecht");
        PublisherOneToManyCascadeRemove pom2 = new PublisherOneToManyCascadeRemove("De Bezige Bij","Amsterdam");
        PublisherOneToManyCascadeRemove pom3 = new PublisherOneToManyCascadeRemove("Broekhuis","Zwolle");
        PublisherOneToManyCascadeRemove pom4 = new PublisherOneToManyCascadeRemove("Lemniscaat","Den Haag");
        PublisherOneToManyCascadeRemove pom5 = new PublisherOneToManyCascadeRemove("Querido","Zoetermeer");
        PublisherOneToManyCascadeRemove pom6 = new PublisherOneToManyCascadeRemove("Kluitman","Leeuwarden");
        EmployeeManyToOneCascadeRemove emp1 = new EmployeeManyToOneCascadeRemove("Jan","Willemsen");
        pom1.getEmployees().add(emp1);
        emp1.setPublisher(pom1);
        EmployeeManyToOneCascadeRemove emp2 = new EmployeeManyToOneCascadeRemove("Piet","Jansen");
        pom1.getEmployees().add(emp2);

        emp2.setPublisher(pom1);
        EmployeeManyToOneCascadeRemove emp3 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        pom2.getEmployees().add(emp3);
        emp3.setPublisher(pom2);
        EmployeeManyToOneCascadeRemove emp4 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp4.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp5 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp5.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp6 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp6.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp7 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp7.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp8 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp8.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp9 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp9.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp10 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp10.setPublisher(pom3);
        EmployeeManyToOneCascadeRemove emp11= new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp11.setPublisher(pom4);
        EmployeeManyToOneCascadeRemove emp12 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp12.setPublisher(pom6);
        EmployeeManyToOneCascadeRemove emp13 = new EmployeeManyToOneCascadeRemove("Marie","van den Berg");
        emp13.setPublisher(pom6);

        executeInTransaction(
                em ->{
                    em.persist(pom1);
                    em.persist(pom2);
                    em.persist(pom3);
                    em.persist(pom4);
                    em.persist(pom5);
                    em.persist(pom6);
                    em.persist(emp1);
                    em.persist(emp2);
                    em.persist(emp3);
                    em.persist(emp4);
                    em.persist(emp5);
                    em.persist(emp6);
                    em.persist(emp7);
                    em.persist(emp8);
                    em.persist(emp9);
                    em.persist(emp10);
                    em.persist(emp11);
                    em.persist(emp12);
                    em.persist(emp13);
                }
        );
    }

    @Test
    void removePublisherWithEmployeesCascadeRemoveTest()
    {
        insertPublisherOneToManyCascadeRemoveAndEmployeeManyToOneCascadeRemove();
        PublisherOneToManyCascadeRemove publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        etx.begin();
        em.remove(publisher);
        etx.commit();
        publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        assertThat(publisher).isNull();
    }
    @Test
    void deletePublisherWithEmployeesCascadeRemoveTest()
    {
        insertPublisherOneToManyCascadeRemoveAndEmployeeManyToOneCascadeRemove();
        em.clear();
        String queryString = "delete from EmployeeManyToOneCascadeRemove e where e.publisher.id = ?1";
        Query query = em.createQuery(queryString);
        etx.begin();
        PublisherOneToManyCascadeRemove publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        int numberOfEmployees = publisher.getEmployees().size();
        logger.info("Number of employees of publisher nr 1: " + numberOfEmployees);
        query.setParameter(1, 1);
        query.executeUpdate();
        etx.commit();
        etx.begin();
        em.clear();
        publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        assertThat(publisher.getEmployees().size()).isEqualTo(0);
        etx.commit();
    }
    @Test
    void orphanRemovalTest()
    {
        insertPublisherOneToManyCascadeRemoveAndEmployeeManyToOneCascadeRemove();
        etx.begin();
        PublisherOneToManyCascadeRemove publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        EmployeeManyToOneCascadeRemove employee = publisher.getEmployees().get(0);
        publisher.getEmployees().remove(employee);
        etx.commit();
        em.clear();
        publisher = em.find(PublisherOneToManyCascadeRemove.class,1);
        assertThat(publisher.getEmployees().size()).isEqualTo(1);
    }
    void addTwoAuthorsWithThreeTitles()
    {
        Author a1 = new Author("Peter");
        Author a2 = new Author("John");
        List<TitleWithList> titles1 = List.of(new TitleWithList("Java Programming"), new TitleWithList("Introduction to JPA"),new TitleWithList("Origin of Java"));
        List<TitleWithList> titles2 = List.of(new TitleWithList("Python Programming"), new TitleWithList("Introduction to Python"),new TitleWithList("Origin of Python"));
        etx.begin();
        em.persist(a1);
        for(TitleWithList t : titles1)
        {
            t.getAuthors().add(a1);
            em.persist(t);
        }
        em.persist(a2);
        for(TitleWithList t : titles2)
        {
            t.getAuthors().add(a2);
            em.persist(t);
        }
        etx.commit();
    }
    @Test
    void authorsWithTitlesWithListTest()
    {
        addTwoAuthorsWithThreeTitles();
        TitleWithList title1 = em.find(TitleWithList.class,2);
        assertThat(title1.getAuthors().size()).isEqualTo(1);
        TitleWithList title2 = em.find(TitleWithList.class,3);
        assertThat(title1.getAuthors().get(0)).isEqualTo(title2.getAuthors().get(0));
    }

}
