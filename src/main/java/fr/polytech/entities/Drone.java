package fr.polytech.entities;

import java.io.Serializable;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Drone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    private DroneStatus droneStatus = DroneStatus.AVAILABLE;

    @OneToMany(cascade = { CascadeType.MERGE })
    private Set<TimeSlot> timeSlots = new TreeSet<>();

    public Drone() {
        // Necessary for JPA instantiation process
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DroneStatus getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(DroneStatus droneStatus) {
        this.droneStatus = droneStatus;
    }

    public Set<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(Set<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public TimeSlot getTimeSlot(Delivery delivery) {
        for (TimeSlot timeSlot : this.timeSlots) {
            if (timeSlot.getDelivery().equals(delivery)) {
                return timeSlot;
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Drone)) {
            return false;
        }
        Drone other = (Drone) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        result += "id: " + id;
        if (droneStatus != null)
            result += ", drone: " + droneStatus;
        if (timeSlots != null)
            result += ", parcel: " + timeSlots;
        return result;
    }
}
