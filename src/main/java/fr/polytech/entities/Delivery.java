package fr.polytech.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
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
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @ManyToOne(cascade = CascadeType.MERGE)
    private Drone drone;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    private Parcel parcel;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.NOT_DELIVERED;

    public Delivery() {
        // Necessary for JPA instantiation process
    }

    /**
     *
     * @param drone to be assigned to the delivery
     */
    public Delivery(Drone drone) {
        this.drone = drone;
    }

    public Drone getDrone() {
        return drone;
    }

    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    public Parcel getParcel() {
        return parcel;
    }

    public void setParcel(Parcel parcel) {
        this.parcel = parcel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        result += "id: " + id;
        if (drone != null)
            result += ", drone: " + drone;
        if (parcel != null)
            result += ", parcel: " + parcel;
        if (status != null)
            result += ", status: " + status;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Delivery)) {
            return false;
        }
        Delivery other = (Delivery) obj;
        return this.id == other.id;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        return result;
    }

}
