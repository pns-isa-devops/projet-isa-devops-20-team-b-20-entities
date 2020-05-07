package fr.polytech.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Invoice implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NotNull
    @Pattern(regexp = "([A-Z 0-9]){15}+", message = "Invalid invoice id")
    private String invoiceId;

    @OneToMany(cascade = { CascadeType.MERGE }, fetch = FetchType.LAZY)
    private List<Delivery> deliveries;

    @NotNull
    private float price;

    @NotNull
    @Enumerated(EnumType.STRING)
    private InvoiceStatus status;

    public Invoice() {
        deliveries = new ArrayList<>();
        price = 0;
        status = InvoiceStatus.NOT_PAID;
    }

    public String getInvoiceId() {
        return this.invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<Delivery> getDeliveries() {
        return this.deliveries;
    }

    public void setDeliveries(List<Delivery> deliveries) {
        this.deliveries = deliveries;
    }

    public float getPrice() {
        return this.price;
    }

    public float getPriceTTC() {
        return this.price * 1.2f;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public InvoiceStatus getStatus() {
        return this.status;
    }

    public void setStatus(InvoiceStatus status) {
        this.status = status;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        StringBuilder msg = new StringBuilder(
                String.format("[ Invoice N°%d\n" + "Price HT : %f\n" + "Price TTC : %f\n", id, price, getPriceTTC()));

        msg.append("List of deliveries : \n");

        for (Delivery delivery : this.getDeliveries()) {
            msg.append(delivery.toString());
            msg.append("\n");
        }

        return msg.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Invoice))
            return false;
        if (this == other)
            return true;

        Invoice otherInvoice = (Invoice) other;

        if (getInvoiceId() != null ? !getInvoiceId().equals(otherInvoice.getInvoiceId())
                : otherInvoice.getInvoiceId() != null) {
            return false;
        }

        if (getDeliveries() != null ? !getDeliveries().equals(otherInvoice.getDeliveries())
                : otherInvoice.getDeliveries() != null) {
            return false;
        }

        return this.price == otherInvoice.price && this.status == otherInvoice.status;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (getInvoiceId() != null ? getInvoiceId().hashCode() : 0);
        result = prime * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = prime * result + (getDeliveries() != null ? getDeliveries().hashCode() : 0);
        return result;
    }

}
