package fr.polytech.entities;

import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class TimeSlot implements Comparable<TimeSlot> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private GregorianCalendar date;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TimeState state;

    @OneToOne
    private Delivery delivery;

    public TimeSlot() {
        // Necessary for JPA instantiation process
    }

    public TimeSlot(GregorianCalendar date, TimeState state, Delivery delivery) {
        this.date = date;
        this.state = state;
        this.delivery = delivery;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public GregorianCalendar getDate() {
        return date;
    }

    public void setDate(GregorianCalendar date) {
        this.date = date;
    }

    public TimeState getState() {
        return state;
    }

    public void setState(TimeState state) {
        this.state = state;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    @Override
    public int compareTo(TimeSlot o) {
        return date.compareTo((o.getDate()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id + ((date == null) ? 0 : date.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof TimeSlot)) {
            return false;
        }
        TimeSlot other = (TimeSlot) obj;
        if (date != null) {
            if (!date.equals(other.date) && this.id == other.id) {
                return false;
            }
        }
        return this.id == other.id;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (date != null)
            result += "date: " + date;
        if (state != null)
            result += ", state: " + state;
        if (delivery != null)
            result += ", delivery: " + delivery;
        return result;
    }

}
