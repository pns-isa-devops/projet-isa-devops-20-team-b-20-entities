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
public class TimeSlotRelationshipsTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    private TimeSlot timeSlot;


    @Before
    public void SetUp(){
        timeSlot = new TimeSlot(new GregorianCalendar(2020, 11, 11));
        timeSlot.setState(TimeState.DELIVERY);
        entityManager.persist(timeSlot);
    }

    @Test
    public void ManyToOneDrone(){
        Drone drone = new Drone("123");
        entityManager.persist(drone);
        timeSlot.setDrone(drone);

     //   entityManager.persist(timeSlot);

        TimeSlot timeSlotStored = entityManager.merge(timeSlot);
        assertEquals(drone, timeSlotStored.getDrone());
    }

    @Test
    public void OneToOneDelivery(){
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        timeSlot.setDelivery(de);

        TimeSlot tsStored = entityManager.merge(timeSlot);

        assertEquals(de,tsStored.getDelivery());

    }
}
