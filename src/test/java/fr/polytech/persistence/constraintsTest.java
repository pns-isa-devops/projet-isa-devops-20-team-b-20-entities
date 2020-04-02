package fr.polytech.persistence;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractEntitiesTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.Parcel;

import static org.junit.Assert.assertNotNull;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import javax.validation.ConstraintViolationException;

@RunWith(Arquillian.class)
public class constraintsTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Resource
    private UserTransaction manual;

    @Test(expected = ConstraintViolationException.class)
    @Ignore
    public void updateDelivery() throws Exception {
        manual.begin();
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);
        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));
        try {
            entityManager.remove(p);
            manual.commit();
        } catch (Exception e) {
            manual.rollback();
            throw e;
        }
    }
}
