package fr.polytech.entities;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

public class Drone implements Serializable {

    private static final long serialVersionUID = -6254437064440335306L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int droneId;

    @NotNull
    private DroneStatus droneStatus;

    private Set<TimeSlot> timeSlots;

    public Drone() {
        // Default : the drone newly created is available
        // Necessary for JPA instantiation process
        this.droneStatus = DroneStatus.AVAILABLE;
        this.timeSlots = new TreeSet<>();
    }

    public int getDroneId() {
        return droneId;
    }

    public void setDroneId(int droneId) {
        this.droneId = droneId;
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
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;

        if (obj != null)
            return (obj.hashCode() == Objects.hash(this.droneId, this.droneStatus));

        return false;
    }

}
