package fr.polytech.entities;

import java.io.Serializable;
import java.util.GregorianCalendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class DroneInformation implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    private GregorianCalendar date;

    @NotNull
    private double occupationRate;

    public DroneInformation() {

    }

    public DroneInformation(GregorianCalendar date) {
        this.date = date;
        this.occupationRate = 0.0;
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

    public double getOccupationRate() {
        return occupationRate;
    }

    public void setOccupationRate(double occupationRate) {
        this.occupationRate = occupationRate;
    }

    @Override
    public int hashCode() {
        final int prime = 42;
        int result = 1;
        result = prime * result + (getDate() != null ? getDate().hashCode() : 0);
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
        DroneInformation other = (DroneInformation) obj;
        if (getDate() != null ? !getDate().equals(other.getDate()) : other.getDate() != null) {
            return false;
        }
        return getOccupationRate() == other.getOccupationRate();

    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " ";
        if (date != null)
            result += "date: " + date.getTime().toString();
        result += ", occupationRate: " + occupationRate;
        return result;
    }

}
