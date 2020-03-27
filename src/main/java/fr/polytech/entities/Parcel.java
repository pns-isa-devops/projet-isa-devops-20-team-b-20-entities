package fr.polytech.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

public class Parcel implements Serializable {

    private static final long serialVersionUID = -4748724748697106351L;

    @NotNull
    private String parcelNumber;

    @NotNull
    private String address;

    @NotNull
    private String carrier;

    @NotNull
    private String customerName;

    public String getParcelNumber() {
        return parcelNumber;
    }

    public void setParcelNumber(String parcelNumber) {
        this.parcelNumber = parcelNumber;
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
        if (parcelNumber != null) {
            if (!parcelNumber.equals(other.parcelNumber)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((parcelNumber == null) ? 0 : parcelNumber.hashCode());
        return result;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (parcelNumber != null && !parcelNumber.trim().isEmpty())
            result += ", parcelNumber: " + parcelNumber;
        if (address != null && !address.trim().isEmpty())
            result += ", address: " + address;
        if (carrier != null && !carrier.trim().isEmpty())
            result += ", carrier: " + carrier;
        if (customerName != null && !customerName.trim().isEmpty())
            result += ", customerName: " + customerName;
        return result;
    }
}
