package fr.polytech.persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import fr.polytech.entities.*;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import arquillian.AbstractEntitiesTest;

/**
 * StorageTest
 */
@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class StorageTest extends AbstractEntitiesTest {

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void storingParcelTestRegex() {
        Validator validator;
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        Set<ConstraintViolation<Parcel>> violations;

        Parcel p0 = new Parcel("abcdefgera", "add1", "car1", "cust1");
        violations = validator.validate(p0);
        assertFalse(violations.isEmpty());
        Parcel p1 = new Parcel("abacdefge4", "add1", "car1", "cust1");
        violations = validator.validate(p1);
        assertFalse(violations.isEmpty());
        Parcel p2 = new Parcel("abaefge4", "add1", "car1", "cust1");
        violations = validator.validate(p2);
        assertFalse(violations.isEmpty());
        Parcel p3 = new Parcel("AAAA1122CC", "add1", "car1", "cust1");
        violations = validator.validate(p3);
        assertTrue(violations.isEmpty());
        Parcel p4 = new Parcel("ABCDEFGTHY", "add1", "car1", "cust1");
        violations = validator.validate(p4);
        assertTrue(violations.isEmpty());
        Parcel p5 = new Parcel("1245789635", "add1", "car1", "cust1");
        violations = validator.validate(p5);
        assertTrue(violations.isEmpty());
        Parcel p6 = new Parcel("AAABB@B", "add1", "car1", "cust1");
        violations = validator.validate(p6);
        assertFalse(violations.isEmpty());
        Parcel p7 = new Parcel("#11222", "add1", "car1", "cust1");
        violations = validator.validate(p7);
        assertFalse(violations.isEmpty());
        Parcel p8 = new Parcel("AAAA;22", "add1", "car1", "cust1");
        violations = validator.validate(p8);
        assertFalse(violations.isEmpty());
        Parcel p9 = new Parcel("AAA_1122C-", "add1", "car1", "cust1");
        violations = validator.validate(p9);
        assertFalse(violations.isEmpty());
    }

    @Test
    public void storingParcelGoodRegex() {
        Parcel p1 = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p1);
        Parcel p2 = new Parcel("AAAA1122CC", "add1", "car1", "cust1");
        entityManager.persist(p2);
    }

    @Test
    public void storingParcel() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        assertEquals(0, p.getId());

        entityManager.persist(p);
        int id = p.getId();
        assertNotEquals(0, id);

        Parcel stored = (Parcel) entityManager.find(Parcel.class, id);
        assertEquals(p, stored);
    }

    @Test
    public void updateParcel() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        // Before Update
        Parcel stored = (Parcel) entityManager.find(Parcel.class, p.getId());
        assertEquals(p, stored);

        p.setAddress("modification address");
        assertEquals(p, stored);

        entityManager.persist(p);
        stored = (Parcel) entityManager.find(Parcel.class, p.getId());
        assertEquals(p, stored);
    }

    @Test
    public void removeParcel() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        Parcel p2 = new Parcel("AAAABBBBDD", "add2", "car2", "cust2");
        entityManager.persist(p);
        entityManager.persist(p2);

        assertNotNull((Parcel) entityManager.find(Parcel.class, p.getId()));
        assertNotNull((Parcel) entityManager.find(Parcel.class, p2.getId()));

        entityManager.remove(p);

        assertNull((Parcel) entityManager.find(Parcel.class, p.getId()));
        assertNotNull((Parcel) entityManager.find(Parcel.class, p2.getId()));
    }

    @Test
    public void storingDrone() {
        Drone dr = new Drone("123");
        assertEquals(0, dr.getId());

        dr.setDroneStatus(DroneStatus.ON_CHARGE);
        entityManager.persist(dr);
        int id = dr.getId();

        Drone stored = (Drone) entityManager.find(Drone.class, id);
        assertEquals(dr, stored);
        assertNotEquals(new Drone("124"), stored);
    }

    @Test
    public void updateDrone() {
        TimeSlot t = new TimeSlot(new GregorianCalendar(), TimeState.REVIEW);
        Drone dr = new Drone("123");
        dr.add(t);
        entityManager.persist(t);
        entityManager.persist(dr);

        TimeSlot storedT = (TimeSlot) entityManager.find(TimeSlot.class, t.getId());
        Drone storedD = (Drone) entityManager.find(Drone.class, dr.getId());

        assertEquals(1, storedD.getTimeSlots().size());
        assertEquals(TimeState.REVIEW, storedT.getState());
        assertEquals(TimeState.REVIEW, storedD.getTimeSlots().iterator().next().getState());

        storedT.setState(TimeState.DELIVERY);
        assertEquals(TimeState.DELIVERY, storedT.getState());
        assertEquals(TimeState.DELIVERY, storedD.getTimeSlots().iterator().next().getState());
    }

    @Test
    public void removeDrone() {
        TimeSlot t = new TimeSlot(new GregorianCalendar(), TimeState.REVIEW);
        Drone dr = new Drone("123");
        dr.add(t);
        entityManager.persist(t);
        entityManager.persist(dr);

        assertEquals(1, dr.getTimeSlots().size());

        assertNotNull(entityManager.find(TimeSlot.class, t.getId()));
        assertNotNull(entityManager.find(Drone.class, dr.getId()));

        entityManager.remove(dr);

        assertNull(entityManager.find(TimeSlot.class, t.getId()));
        assertNull(entityManager.find(Drone.class, dr.getId()));
    }

    @Test
    public void storingDelivery() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);

        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));

        int id = de.getId();

        Delivery stored = (Delivery) entityManager.find(Delivery.class, id);
        assertEquals(de, stored);
    }

    @Test
    public void updateDelivery() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);
        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));

        Parcel p2 = new Parcel("AAAABBBBCE", "add1", "car1", "cust1");
        entityManager.persist(p2);

        de.setParcel(p2);

        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNotNull(entityManager.find(Parcel.class, p2.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));
    }

    @Test
    public void removeDelivery() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Delivery de = new Delivery("1234567890");
        de.setParcel(p);
        entityManager.persist(de);
        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));

        entityManager.remove(de);

        assertNotNull(entityManager.find(Parcel.class, p.getId()));
        assertNull(entityManager.find(Delivery.class, de.getId()));
    }

    @Test
    public void storingTimeSlot() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Drone dr = new Drone("123");
        // Flush Drone
        dr.setDroneStatus(DroneStatus.ON_CHARGE);
        entityManager.persist(dr);

        Delivery de = new Delivery("1234567890");
        // Flush Delivery
        de.setParcel(p);
        entityManager.persist(de);

        de.setDrone(dr);
        TimeSlot t = new TimeSlot(new GregorianCalendar(2020, 12, 12), TimeState.DELIVERY);
        // Flush Timeslot
        assertEquals(0, t.getId());
        entityManager.persist(t);
        int id = t.getId();
        assertNotEquals(0, id);

        TimeSlot stored = (TimeSlot) entityManager.find(TimeSlot.class, id);
        assertEquals(t, stored);
        // even after the persistance they are the same reference
        t.setDate(new GregorianCalendar(2020, 12, 10));
        assertEquals(t, stored);
    }

    @Test
    public void updateTimeSlot() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Drone dr = new Drone("123");
        // Flush Drone
        dr.setDroneStatus(DroneStatus.ON_CHARGE);
        entityManager.persist(dr);

        Delivery de = new Delivery("1234567890");
        // Flush Delivery
        de.setParcel(p);
        entityManager.persist(de);

        de.setDrone(dr);
        TimeSlot t = new TimeSlot(new GregorianCalendar(2020, 12, 12), TimeState.DELIVERY);
        // Flush Timeslot
        entityManager.persist(t);
        int id = t.getId();

        TimeSlot stored = (TimeSlot) entityManager.find(TimeSlot.class, id);
        assertEquals(t, stored);

        Drone dr2 = new Drone("124");
        entityManager.persist(dr2);

        stored.setDrone(dr2);

        assertNotNull(entityManager.find(Drone.class, dr.getId()));
        assertNotNull(entityManager.find(Drone.class, dr2.getId()));
    }

    @Test
    public void removeTimeSlot() {
        Parcel p = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p);

        Drone dr = new Drone("123");
        // Flush Drone
        dr.setDroneStatus(DroneStatus.ON_CHARGE);
        entityManager.persist(dr);

        Delivery de = new Delivery("1234567890");
        // Flush Delivery
        de.setParcel(p);
        entityManager.persist(de);

        de.setDrone(dr);
        TimeSlot t = new TimeSlot(new GregorianCalendar(2020, 12, 12), TimeState.DELIVERY);
        // Flush Timeslot
        entityManager.persist(t);

        assertNotNull(entityManager.find(TimeSlot.class, t.getId()));
        assertNotNull(entityManager.find(Drone.class, dr.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));

        entityManager.remove(t);

        assertNull(entityManager.find(TimeSlot.class, t.getId()));
        assertNotNull(entityManager.find(Drone.class, dr.getId()));
        assertNotNull(entityManager.find(Delivery.class, de.getId()));
    }

    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // - - -  - - - -- - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    @Test
    public void storingDroneInformation() {
        Drone dr = new Drone("123");
        entityManager.persist(dr);
        dr = entityManager.merge(dr);

        DroneInformation droneInformation = new DroneInformation(new GregorianCalendar(2020, 11, 27),dr);
        assertEquals(0, droneInformation.getId());
        entityManager.persist(droneInformation);
        int id = droneInformation.getId();
        assertNotEquals(0, id);

        DroneInformation stored = (DroneInformation) entityManager.find(DroneInformation.class, id);
        assertEquals(droneInformation, stored);

        stored.setOccupationRate(10.0);
        entityManager.persist(stored);

        DroneInformation stored2 = entityManager.merge(stored);
        assertEquals(10.0,stored2.getOccupationRate(),0.001);
    }

    @Test
    public void updateDroneInformation() {
        Drone dr = new Drone("123");
        entityManager.persist(dr);
        dr = entityManager.merge(dr);

        DroneInformation droneInformation = new DroneInformation(new GregorianCalendar(2020, 11, 27),dr);
        entityManager.persist(droneInformation);

        // Occupation rate
        droneInformation.setOccupationRate(10.0);
        entityManager.persist(droneInformation);

        DroneInformation stored2 = entityManager.merge(droneInformation);
        assertEquals(10.0,stored2.getOccupationRate(),0.001);

        // Date
        stored2.setDate(new GregorianCalendar(2020, 11, 28));
        entityManager.persist(stored2);

        DroneInformation stored3 = entityManager.merge(droneInformation);
        assertEquals(new GregorianCalendar(2020, 11, 28),stored3.getDate());

        // Drone
        Drone dr2 = new Drone("234");
        entityManager.persist(dr2);
        dr2 = entityManager.merge(dr2);

        assertNotEquals(dr2, stored3.getDrone());
        stored3.setDrone(dr2);
        entityManager.persist(stored3);
        DroneInformation stored4 = entityManager.merge(stored3);

        assertEquals(dr2,stored4.getDrone());

    }

    @Test
    public void removeDroneInformation() {
        Drone dr = new Drone("123");
        entityManager.persist(dr);
        dr = entityManager.merge(dr);

        DroneInformation droneInformation = new DroneInformation(new GregorianCalendar(2020, 11, 27),dr);
        entityManager.persist(droneInformation);
        droneInformation = entityManager.merge(droneInformation);
        entityManager.remove(droneInformation);

        dr = entityManager.merge(dr);
        assertNotNull(dr);

        assertNull(entityManager.find(DroneInformation.class, droneInformation.getId()));

    }

    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------
    //--------------------

    @Test
    public void storingInvoice() {
        Parcel p1 = new Parcel("AAAABBBBCA", "add1", "car1", "cust1");
        entityManager.persist(p1);
        Delivery d1 = new Delivery("AAAABBBBCD");
        d1.setParcel(p1);
        Parcel p2 = new Parcel("AAAABBBBCB", "add1", "car1", "cust1");
        entityManager.persist(p2);
        Delivery d2 = new Delivery("AAAABBBBCE");
        d2.setParcel(p2);
        Parcel p3 = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p3);
        Delivery d3 = new Delivery("AAAABBBBCF");
        d3.setParcel(p3);
        entityManager.persist(d1);
        entityManager.persist(d2);
        entityManager.persist(d3);

        Invoice invoice = new Invoice();
        invoice.setDeliveries(Arrays.asList(d1, d2, d3));
        invoice.setPrice(10f);
        invoice.setInvoiceId("0123456789ABDE1");
        invoice.setStatus(InvoiceStatus.NOT_PAID);
        entityManager.persist(invoice);

        assertNotNull(entityManager.find(Invoice.class, invoice.getId()));

        int id = (int) invoice.getId();

        Invoice stored = (Invoice) entityManager.find(Invoice.class, id);
        assertEquals(invoice, stored);
    }

    @Test
    public void updateInvoice() {
        Parcel p1 = new Parcel("AAAABBBBCA", "add1", "car1", "cust1");
        entityManager.persist(p1);
        Delivery d1 = new Delivery("AAAABBBBCD");
        d1.setParcel(p1);
        Parcel p2 = new Parcel("AAAABBBBCB", "add1", "car1", "cust1");
        entityManager.persist(p2);
        Delivery d2 = new Delivery("AAAABBBBCE");
        d2.setParcel(p2);
        Parcel p3 = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p3);
        Delivery d3 = new Delivery("AAAABBBBCF");
        d3.setParcel(p3);
        entityManager.persist(d1);
        entityManager.persist(d2);
        entityManager.persist(d3);

        Invoice invoice = new Invoice();
        invoice.setDeliveries(new ArrayList<>(Arrays.asList(d1, d2, d3)));
        invoice.setPrice(10f);
        invoice.setInvoiceId("0123456789ABDE2");
        invoice.setStatus(InvoiceStatus.NOT_PAID);
        entityManager.persist(invoice);

        assertNotNull(entityManager.find(Invoice.class, invoice.getId()));

        int id = (int) invoice.getId();

        Invoice stored = (Invoice) entityManager.find(Invoice.class, id);
        assertEquals(InvoiceStatus.NOT_PAID, invoice.getStatus());
        assertEquals(10, (int)invoice.getPrice());
        assertEquals(3, (int)invoice.getDeliveries().size());

        invoice.setStatus(InvoiceStatus.PAID);
        invoice.setPrice(20f);
        Parcel p4 = new Parcel("AAAABBBBCA", "add1", "car1", "cust1");
        entityManager.persist(p4);
        Delivery d4 = new Delivery("AAAABBBBCH");
        d4.setParcel(p4);
        entityManager.persist(d4);
        invoice.getDeliveries().add(d4);
        entityManager.persist(invoice);
        stored = entityManager.merge(stored);
        assertEquals(InvoiceStatus.PAID, stored.getStatus());
        assertEquals(20, (int)invoice.getPrice());
        assertEquals(4, (int)invoice.getDeliveries().size());
    }

    @Test
    public void removeInvoice() {
        Parcel p1 = new Parcel("AAAABBBBCA", "add1", "car1", "cust1");
        entityManager.persist(p1);
        Delivery d1 = new Delivery("AAAABBBBCD");
        d1.setParcel(p1);
        Parcel p2 = new Parcel("AAAABBBBCB", "add1", "car1", "cust1");
        entityManager.persist(p2);
        Delivery d2 = new Delivery("AAAABBBBCE");
        d2.setParcel(p2);
        Parcel p3 = new Parcel("AAAABBBBCC", "add1", "car1", "cust1");
        entityManager.persist(p3);
        Delivery d3 = new Delivery("AAAABBBBCF");
        d3.setParcel(p3);
        entityManager.persist(d1);
        entityManager.persist(d2);
        entityManager.persist(d3);

        Invoice invoice = new Invoice();
        invoice.setDeliveries(Arrays.asList(d1, d2, d3));
        invoice.setPrice(10f);
        invoice.setInvoiceId("0123456789ABDE3");
        invoice.setStatus(InvoiceStatus.NOT_PAID);
        entityManager.persist(invoice);

        assertNotNull(entityManager.find(Invoice.class, invoice.getId()));


        invoice = entityManager.merge(invoice);
        entityManager.remove(invoice);

        d1 = entityManager.merge(d1);
        assertNotNull(d1);

        assertNull(entityManager.find(DroneInformation.class, invoice.getId()));

    }
}
