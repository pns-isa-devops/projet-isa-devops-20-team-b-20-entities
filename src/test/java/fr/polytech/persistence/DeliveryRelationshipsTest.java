package fr.polytech.persistence;


import java.util.GregorianCalendar;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;


import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractEntitiesTest;
import fr.polytech.entities.Delivery;
import fr.polytech.entities.Drone;

import fr.polytech.entities.Parcel;
import fr.polytech.entities.TimeSlot;
import fr.polytech.entities.TimeState;

import static org.junit.Assert.*;

/**
 * StorageTest
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DeliveryRelationshipsTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Delivery delivery;
    private Parcel p ;

    @Before
    public void SetUp(){
        p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        delivery = new Delivery("DEL1234567");
        delivery.setParcel(p);
        entityManager.persist(delivery);
    }

    @Test
    public void OneToOneDrone(){
        Drone drone = new Drone("345");
        entityManager.persist(drone);

        delivery.setDrone(drone);

        entityManager.persist(delivery);

        Delivery storedDelivery = entityManager.merge(delivery);

        assertEquals(drone,storedDelivery.getDrone());

    }

    @Test
    public void OneToOneParcel(){
        Delivery storedDelivery = entityManager.merge(delivery);
        assertEquals(p,storedDelivery.getParcel());
    }
}
