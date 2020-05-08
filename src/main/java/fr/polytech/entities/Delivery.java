package fr.polytech.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Pattern(regexp = "([A-Z 0-9]){10}+", message = "Invalid delivery id")
    private String deliveryId;

    @OneToOne(cascade = { CascadeType.MERGE, CascadeType.PERSIST }, fetch = FetchType.LAZY)
    private Drone drone;

    @NotNull
    @OneToOne(cascade = CascadeType.MERGE)
    private Parcel parcel;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status = DeliveryStatus.NOT_DELIVERED;

    @ManyToOne
    private Invoice invoice;

    public Delivery() {
        // Necessary for JPA instantiation process
    }

    public Delivery(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
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

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
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
        if (getDeliveryId() != null ? !getDeliveryId().equals(other.getDeliveryId()) : other.getDeliveryId() != null) {
            return false;
        }
        if (getDrone() != null ? !getDrone().getDroneId().equals(other.getDrone().getDroneId())
                : other.getDrone() != null) {
            return false;
        }
        if (getParcel() != null ? !getParcel().equals(other.getParcel()) : other.getParcel() != null) {
            return false;
        }
        return getStatus() == other.getStatus();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getDeliveryId() != null ? getDeliveryId().hashCode() : 0);
        result = prime * result + (getDrone() != null ? getDrone().getDroneId().hashCode() : 0);
        result = prime * result + (getDeliveryId() != null ? getDeliveryId().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (deliveryId != null && !deliveryId.trim().isEmpty())
            result += "deliveryId: " + deliveryId;
        if (drone != null)
            result += ", drone: " + drone.getDroneId();
        if (parcel != null)
            result += ", parcel: " + parcel;
        if (status != null)
            result += ", status: " + status;
        return result;
    }

    public Invoice getInvoice() {
        return this.invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }
}
