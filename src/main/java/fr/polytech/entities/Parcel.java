package fr.polytech.entities;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Parcel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Pattern(regexp = "([A-Z 0-9]){10}+", message = "Invalid delivery id")
    private String parcelId;

    @NotNull
    private String address;

    @NotNull
    private String carrier;

    @NotNull
    private String customerName;

    public Parcel() {
        // Necessary for JPA instantiation process
    }

    public Parcel(String id, String address, String carrier, String customerName) {
        this.parcelId = id;
        this.address = address;
        this.carrier = carrier;
        this.customerName = customerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParcelId() {
        return parcelId;
    }

    public void setParcelId(String parcelId) {
        this.parcelId = parcelId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Parcel)) {
            return false;
        }
        Parcel other = (Parcel) obj;
        if (getAddress() != null ? !getAddress().equals(other.getAddress()) : other.getAddress() != null) {
            return false;
        }
        if (getCarrier() != null ? !getCarrier().equals(other.getCarrier()) : other.getCarrier() != null) {
            return false;
        }
        return getCustomerName() != null ? !getCustomerName().equals(other.getCustomerName())
                : other.getCustomerName() == null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getParcelId() != null ? getParcelId().hashCode() : 0);
        result = prime * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = prime * result + (getCarrier() != null ? getCarrier().hashCode() : 0);
        result = prime * result + (getCustomerName() != null ? getCustomerName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (parcelId != null && !parcelId.trim().isEmpty())
            result += ", parcelId: " + parcelId;
        if (address != null && !address.trim().isEmpty())
            result += ", address: " + address;
        if (carrier != null && !carrier.trim().isEmpty())
            result += ", carrier: " + carrier;
        if (customerName != null && !customerName.trim().isEmpty())
            result += ", customerName: " + customerName;
        return result;
    }
}
