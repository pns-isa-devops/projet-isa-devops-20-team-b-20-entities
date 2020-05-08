package fr.polytech.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.GregorianCalendar;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractEntitiesTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.Drone;
import fr.polytech.entities.Parcel;
import fr.polytech.entities.TimeSlot;
import fr.polytech.entities.TimeState;

/**
 * StorageTest
 */
@RunWith(Arquillian.class)
public class DeliveryRelationshipsTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction manual;

    private Delivery delivery;
    private Parcel p;

    @Before
    public void SetUp() throws Exception {
        manual.begin();
        p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        delivery = new Delivery("DEL1234567");
        delivery.setParcel(p);
        entityManager.persist(delivery);
        manual.commit();
    }

    @Test
    public void OneToOneDrone() throws Exception {
        manual.begin();
        Drone drone = new Drone("345");
        entityManager.persist(drone);

        delivery = entityManager.merge(delivery);
        delivery.setDrone(drone);

        Delivery storedDelivery = entityManager.merge(delivery);

        assertEquals(drone, storedDelivery.getDrone());
        manual.commit();
    }

    @Test
    public void OneToOneParcel() throws Exception {
        manual.begin();
        Delivery storedDelivery = entityManager.merge(delivery);
        assertEquals(p, storedDelivery.getParcel());
        manual.commit();
    }

    @Test
    public void OneToOneDroneToManyTimeslot() throws Exception {
        manual.begin();
        delivery = entityManager.find(Delivery.class, delivery.getId());
        Drone drone = new Drone("123");
        entityManager.merge(drone);
        delivery.setDrone(drone);
        manual.commit();

        manual.begin();
        delivery = entityManager.find(Delivery.class, delivery.getId());
        assertTrue(drone.getTimeSlots().isEmpty());
        assertNotNull(delivery.getDrone());
        manual.commit();

        manual.begin();
        TimeSlot ts1 = new TimeSlot(new GregorianCalendar(), TimeState.DELIVERY);
        delivery = entityManager.find(Delivery.class, delivery.getId());
        ts1.setDrone(delivery.getDrone());
        entityManager.persist(ts1);
        delivery.getDrone().add(ts1);
        manual.commit();

        manual.begin();
        delivery = entityManager.find(Delivery.class, delivery.getId());
        drone = delivery.getDrone();
        assertFalse(drone.getTimeSlots().isEmpty());
        manual.commit();
    }
}
