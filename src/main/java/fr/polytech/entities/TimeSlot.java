package fr.polytech.entities;

import java.io.Serializable;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
public class TimeSlot implements Comparable<TimeSlot>, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private GregorianCalendar date;

    @ManyToOne
    private Drone drone;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TimeState state;

    @OneToOne
    private Delivery delivery;

    public TimeSlot() {
        // Necessary for JPA instantiation process
    }

    public TimeSlot(GregorianCalendar date, TimeState timeState) {
        this.date = date;
        this.state = timeState;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
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
        result = prime * result + (getDrone() == null ? 0 : getDrone().getDroneId().hashCode());
        result = prime * result + (getDate() == null ? 0 : getDate().hashCode());
        result = prime * result + (getState() == null ? 0 : getState().hashCode());
        result = prime * result + (getDelivery() == null ? 0 : getDelivery().hashCode());
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
        if (getDate() != null ? !getDate().equals(other.getDate()) : other.getDate() != null) {
            return false;
        }
        if (getDrone() != null ? !getDrone().getDroneId().equals(other.getDrone().getDroneId())
                : other.getDrone() != null) {
            return false;
        }
        if (getDelivery() != null ? !getDelivery().getDeliveryId().equals(other.getDelivery().getDeliveryId())
                : other.getDelivery() != null) {
            return false;
        }
        return getState() == other.getState();
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (date != null)
            result += "date: " + date.getTime().toString();
        if (state != null)
            result += ", state: " + state;
        if (drone != null)
            result += "drone: " + drone.getDroneId();
        if (delivery != null)
            result += ", delivery: " + delivery.getDeliveryId();
        return result;
    }

}
