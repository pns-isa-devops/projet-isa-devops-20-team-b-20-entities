package fr.polytech.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Time;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
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

    @Resource
    private UserTransaction manual;

    private Drone drone;

    @Test
    public void oneToOneDelivery() {

        // Create a delivery
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        drone = new Drone("123");
        // Assign the delivery to the drone
        drone.setCurrentDelivery(de);

        entityManager.persist(drone);

        Delivery storedDe = entityManager.find(Delivery.class, de.getId());

        int idDrone = drone.getId();
        Drone storedDrone = entityManager.find(Drone.class, idDrone);
        assertEquals(storedDe, storedDrone.getCurrentDelivery());
    }

    @Test
    public void listTimeSlotOneToOneDelivery() {
        // Create a delivery
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        drone = new Drone("123");
        entityManager.persist(drone);

        // timestate are not usefull, just here to have different timeslots
        TimeSlot t1 = new TimeSlot(new GregorianCalendar(), TimeState.AVAILABLE);
        TimeSlot t2 = new TimeSlot(new GregorianCalendar(), TimeState.CHARGING);
        TimeSlot t3 = new TimeSlot(new GregorianCalendar(), TimeState.REVIEW);
        TimeSlot t4 = new TimeSlot(new GregorianCalendar(), TimeState.RESERVED_FOR_CHARGE);

        drone.add(t1);
        drone.add(t2);
        drone.add(t3);
        drone.add(t4);

        assertEquals(4, drone.getTimeSlots().size());

        // Assign the delivery to the drone
        t1.setDelivery(de);
        drone.setCurrentDelivery(de);

        assertEquals(t1, drone.getTimeSlot(de));
        assertNull(drone.getTimeSlot(null));

        Delivery storedDe = entityManager.find(Delivery.class, de.getId());
        assertNotNull(storedDe);

        assertEquals(true, drone.getTimeSlots().remove(t1));
        assertEquals(3, drone.getTimeSlots().size());

        Drone storedDr = entityManager.find(Drone.class, drone.getId());
        assertNotNull(storedDr);

        assertEquals(3, storedDr.getTimeSlots().size());
        assertNull(drone.getTimeSlot(de));

        storedDe = entityManager.find(Delivery.class, de.getId());
        assertNotNull(storedDe);
    }

    @Test
    @Transactional(TransactionMode.DISABLED)
    public void listTimeSlotOneToOneDeliveryMultipleTransaction() throws Exception {
        // Create a delivery
        manual.begin();
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        drone = new Drone("123");
        entityManager.persist(drone);

        // timestate are not usefull, just here to have different timeslots
        TimeSlot t1 = new TimeSlot(new GregorianCalendar(), TimeState.AVAILABLE);
        TimeSlot t2 = new TimeSlot(new GregorianCalendar(), TimeState.CHARGING);
        TimeSlot t3 = new TimeSlot(new GregorianCalendar(), TimeState.REVIEW);
        TimeSlot t4 = new TimeSlot(new GregorianCalendar(), TimeState.RESERVED_FOR_CHARGE);

        drone.add(t1);
        drone.add(t2);
        drone.add(t3);
        drone.add(t4);

        assertEquals(4, drone.getTimeSlots().size());

        // Assign the delivery to the drone
        t1.setDelivery(de);
        drone.setCurrentDelivery(de);
        manual.commit();
        manual.begin();

        Delivery storedDe = entityManager.find(Delivery.class, de.getId());
        Drone storedDr = entityManager.find(Drone.class, drone.getId());

        assertNotNull(storedDe);
        assertNotNull(storedDr);
        assertEquals(storedDe, entityManager.merge(storedDr.getCurrentDelivery()));

        List<TimeSlot> storedTs = storedDr.getTimeSlots();
        assertEquals(4, storedTs.size());

        TimeSlot tsWithDel = storedDr.getTimeSlot(storedDe);
        assertEquals(t1, tsWithDel);

        assertEquals(storedDe, tsWithDel.getDelivery());
        manual.commit();

        manual.begin();
        storedDr = entityManager.find(Drone.class, drone.getId());
        assertNotNull(storedDr);

        t1 = entityManager.merge(t1);
        assertEquals(true, storedDr.getTimeSlots().remove(t1));
        assertEquals(3, storedDr.getTimeSlots().size());
        assertFalse(storedDr.getTimeSlots().contains(t1));

        storedDe = entityManager.find(Delivery.class, de.getId());
        assertNotNull(storedDe);

        manual.commit();
    }
}
