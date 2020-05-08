package fr.polytech.persistence;

import static org.junit.Assert.assertEquals;

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

/**
 * StorageTest
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class DroneRelationshipsTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    private Drone drone;

    @Before
    public void SetUp() {
        drone = new Drone("123");
        entityManager.persist(drone);
    }

    @Test
    public void OneToManyTimeSlots() {

        // Empty timeslots created
        assertEquals(0, drone.getTimeSlots().size());

        // Add a timeslot and check if it's in
        TimeSlot t = new TimeSlot(new GregorianCalendar(2020, 12, 12), TimeState.DELIVERY);
        entityManager.persist(t);
        TimeSlot stored = (TimeSlot) entityManager.find(TimeSlot.class, t.getId());
        drone.getTimeSlots().add(stored);
        entityManager.persist(drone);

        int idDrone = drone.getId();
        Drone storedDrone = (Drone) entityManager.find(Drone.class, idDrone);

        assertEquals(1, storedDrone.getTimeSlots().size());

        // Add a second timeslot
        TimeSlot t2 = new TimeSlot(new GregorianCalendar(2020, 11, 12), TimeState.DELIVERY);
        entityManager.persist(t2);
        TimeSlot stored2 = (TimeSlot) entityManager.find(TimeSlot.class, t2.getId());
        drone.getTimeSlots().add(stored2);
        entityManager.persist(drone);
        storedDrone = (Drone) entityManager.find(Drone.class, idDrone);
        assertEquals(2, storedDrone.getTimeSlots().size());

    }

    @Test
    public void OneToOneDelivery() {

        // Create a delivery
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        // Assign the delivery to the drone
        drone.setCurrentDelivery(de);
        entityManager.persist(drone);

        Delivery storedDe = entityManager.find(Delivery.class, de.getId());

        int idDrone = drone.getId();
        Drone storedDrone = entityManager.find(Drone.class, idDrone);
        assertEquals(storedDe, storedDrone.getCurrentDelivery());

    }
}
