package fr.polytech.persistence;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractEntitiesTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.Drone;
import fr.polytech.entities.Parcel;
import fr.polytech.entities.TimeSlot;
import fr.polytech.entities.TimeState;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class CircularEntitiesTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Parcel parcel;

    @Resource
    private UserTransaction utx;

    private List<Drone> drones;

    private Delivery delivery1;
    private Delivery delivery2;
    private Delivery delivery3;

    @Before
    public void init() throws Exception {

        this.drones = new ArrayList<>();
        this.drones.add(new Drone("000"));
        entityManager.persist(drones.get(0));

        parcel = new Parcel("AAAAAAAAA1", "address", "carrier", "Dupond");
        entityManager.persist(parcel);
        delivery1 = new Delivery("DDDDDDDDD1");
        delivery1.setParcel(parcel);
        delivery2 = new Delivery("DDDDDDDDD2");
        delivery2.setParcel(parcel);
        delivery3 = new Delivery("DDDDDDDDD3");
        delivery3.setParcel(parcel);
        entityManager.persist(delivery1);
        entityManager.persist(delivery2);
        entityManager.persist(delivery3);
    }

    @After
    public void cleaningUp() throws Exception {
        utx.begin();

        Drone d;
        for (int i = 0; i < this.drones.size(); i++) {
            d = entityManager.merge(this.drones.get(i));
            entityManager.remove(d);
        }

        delivery1 = entityManager.merge(delivery1);
        entityManager.remove(delivery1);
        delivery2 = entityManager.merge(delivery2);
        entityManager.remove(delivery2);
        delivery3 = entityManager.merge(delivery3);
        entityManager.remove(delivery3);
        parcel = entityManager.merge(parcel);
        entityManager.remove(parcel);

        utx.commit();
        System.out.println("commited");
    }

    @Test
    public void test1() {
        TimeSlot t = new TimeSlot(new GregorianCalendar(), TimeState.DELIVERY);
        delivery1 = entityManager.merge(delivery1);
        t.setDelivery(delivery1);
        Drone d = entityManager.merge(drones.get(0));
        d.add(t);
        delivery1.setDrone(d);
        assertTrue("shoudn't rollback", true);
    }

    @Test
    public void test2() {
        TimeSlot t = new TimeSlot(new GregorianCalendar(), TimeState.DELIVERY);
        delivery2 = entityManager.merge(delivery2);
        t.setDelivery(delivery2);
        Drone d = entityManager.merge(drones.get(0));
        d.add(t);
        delivery2.setDrone(d);
        assertTrue("shoudn't rollback", true);
    }

}
