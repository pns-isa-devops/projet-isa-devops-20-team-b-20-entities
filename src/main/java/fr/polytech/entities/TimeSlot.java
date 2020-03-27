package fr.polytech.entities;

import java.util.GregorianCalendar;

import javax.validation.constraints.NotNull;

public class TimeSlot implements Comparable<TimeSlot> {
    @NotNull
    private GregorianCalendar date;

    @NotNull
    private TimeState state;

    private Delivery delivery;

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
        return date.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof TimeSlot))
            return false;

        if (obj == this)
            return true;

        return date.equals(((TimeSlot) obj).getDate()) && delivery.equals(((TimeSlot) obj).getDelivery())
                && state.equals(((TimeSlot) obj).getState());

    }

}
