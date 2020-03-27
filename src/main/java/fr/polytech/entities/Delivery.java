package fr.polytech.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class Delivery implements Serializable {

    private static final long serialVersionUID = -5281177622173992243L;

    @NotNull
    private Drone drone;

    @NotNull
    private Parcel parcel;

    @NotNull
    private String deliveryNumber;

    @NotNull
    private DeliveryStatus status;

    public Delivery() {
        this.status = DeliveryStatus.NOT_DELIVERED;
    }

    /**
     *
     * @param drone to be assigned to the delivery
     */
    public Delivery(Drone drone) {
        this.status = DeliveryStatus.NOT_DELIVERED;
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

    public String getDeliveryNumber() {
        return deliveryNumber;
    }

    public void setDeliveryNumber(String deliveryNumber) {
        this.deliveryNumber = deliveryNumber;
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
        if (drone != null)
            result += ", drone: " + drone;
        if (parcel != null)
            result += ", parcel: " + parcel;
        if (deliveryNumber != null && !deliveryNumber.trim().isEmpty())
            result += ", deliveryNumber: " + deliveryNumber;
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
        if (deliveryNumber != null) {
            if (!deliveryNumber.equals(other.deliveryNumber)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        return prime * result + ((deliveryNumber == null) ? 0 : deliveryNumber.hashCode());
    }

}
