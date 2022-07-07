import com.infosupport.demo6startwithconcurrencycontrol.Account;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Demo6StartWithConcurrencyControlTests {
    private static final Logger logger = LoggerFactory.getLogger(Demo6StartWithConcurrencyControlTests.class);
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

    void addOneAccount()
    {
        Account acc1 = new Account("Willem", new BigDecimal("123"));
        executeInTransaction( em ->
        {
            em.persist(acc1);
        });
    }

    @Test
    void showOptimisticConcurrencyControl() {
        addOneAccount();
        Account asSeenByUser1 = em.find(Account.class, 1);
        em.detach(asSeenByUser1);
        asSeenByUser1.Add(new BigDecimal("200"));
        Account asSeenByUser2 = em.find(Account.class, 1);
        executeInTransaction(em ->
        {
            asSeenByUser2.Add(new BigDecimal("500"));
        });
        assertThatThrownBy(() ->
        {
            executeInTransaction(em ->
            {
                em.merge(asSeenByUser1);
            });
        }
        ).isInstanceOf(RuntimeException.class)
                .hasCauseInstanceOf(OptimisticLockException.class);
        Account tmp = em.find(Account.class,asSeenByUser1.getId());
        asSeenByUser1.setBalance(tmp.getBalance());
        asSeenByUser1.setVersion(tmp.getVersion());
        asSeenByUser1.Add(new BigDecimal(200));
        executeInTransaction( em -> {
            em.merge(asSeenByUser1);
        });
        Account asInDB = em.find(Account.class,1);
        assertThat(asInDB.getBalance()).isEqualTo(new BigDecimal(("823.00")));
    }
}