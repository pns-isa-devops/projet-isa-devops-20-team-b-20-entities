package fr.polytech.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Drone implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Pattern(regexp = "([A-Z 0-9]){3}+", message = "Invalid drone id")
    private String droneId;

    @OneToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    private Delivery currentDelivery;

    @ElementCollection
    private List<TimeSlot> timeSlots = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private DroneStatus droneStatus = DroneStatus.AVAILABLE;

    @ElementCollection
    private Set<DroneInformation> droneInformation = new HashSet<>();

    private int flightTime;

    public int getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(int flightTime) {
        this.flightTime = flightTime;
    }

    /**
     * Creates a drone of ID 000.
     */
    public Drone() {
        // Necessary for JPA instantiation process
    }

    /**
     * Creates a drone with the specified ID.
     *
     * @param id
     */
    public Drone(String id) {
        this.droneId = id;
        this.droneStatus = DroneStatus.AVAILABLE;
        this.flightTime = 0;
    }

    public void add(TimeSlot slot) {
        this.timeSlots.add(slot);
    }

    public String getDroneId() {
        return droneId;
    }

    public void setDroneId(String droneId) {
        this.droneId = droneId;
    }

    public Delivery getCurrentDelivery() {
        return currentDelivery;
    }

    public void setCurrentDelivery(Delivery currentDelivery) {
        this.currentDelivery = currentDelivery;
    }

    public int getId() {
        return id;
    }

    public DroneStatus getDroneStatus() {
        return droneStatus;
    }

    public void setDroneStatus(DroneStatus droneStatus) {
        this.droneStatus = droneStatus;
    }

    public Set<DroneInformation> getDroneInformation() {
        return droneInformation;
    }

    public DroneInformation getDroneInformationAtDate(GregorianCalendar gregorianCalendar) {

        for (DroneInformation droneInformation : this.droneInformation) {
            if (droneInformation.getDate().get(Calendar.YEAR) == gregorianCalendar.get(Calendar.YEAR)
                    && droneInformation.getDate().get(Calendar.MONTH) == gregorianCalendar.get(Calendar.MONTH)
                    && droneInformation.getDate().get(Calendar.DAY_OF_MONTH) == gregorianCalendar
                            .get(Calendar.DAY_OF_MONTH)) {
                return droneInformation;
            }
        }
        return null;

    }

    public void setDroneInformation(Set<DroneInformation> droneInformation) {
        this.droneInformation = droneInformation;
    }

    public List<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    public void setTimeSlots(List<TimeSlot> timeSlots) {
        this.timeSlots = timeSlots;
    }

    public TimeSlot getTimeSlot(Delivery delivery) {
        for (TimeSlot timeSlot : this.timeSlots) {
            if (timeSlot.getDelivery() != null) {
                if (timeSlot.getDelivery().equals(delivery)) {
                    return timeSlot;
                }
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getDroneId() != null ? getDroneId().hashCode() : 0);
        result = prime * result + (getDroneStatus() != null ? getDroneStatus().hashCode() : 0);
        result = prime * result + (getTimeSlots() != null ? getTimeSlots().hashCode() : 0);
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
        if (getDroneId() != null ? !getDroneId().equals(other.getDroneId()) : other.getDroneId() != null) {
            return false;
        }
        if (getTimeSlots() != null ? !getTimeSlots().equals(other.getTimeSlots()) : other.getTimeSlots() != null) {
            return false;
        }
        return getDroneStatus() == other.getDroneStatus();
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (droneId != null && !droneId.trim().isEmpty())
            result += "droneId: " + droneId;
        if (droneStatus != null)
            result += ", status: " + droneStatus;
        if (timeSlots != null)
            result += ", timeslots: " + timeSlots;
        if (droneInformation != null)
            result += ", drone information : " + droneInformation;
        result += ", flight time: " + flightTime;
        return result;
    }
}
