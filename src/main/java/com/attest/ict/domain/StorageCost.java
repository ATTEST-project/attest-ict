package com.attest.ict.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StorageCost.
 */
@Entity
@Table(name = "storage_cost")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StorageCost implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "cost_a")
    private Double costA;

    @Column(name = "cost_b")
    private Double costB;

    @Column(name = "cost_c")
    private Double costC;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StorageCost id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public StorageCost busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Double getCostA() {
        return this.costA;
    }

    public StorageCost costA(Double costA) {
        this.setCostA(costA);
        return this;
    }

    public void setCostA(Double costA) {
        this.costA = costA;
    }

    public Double getCostB() {
        return this.costB;
    }

    public StorageCost costB(Double costB) {
        this.setCostB(costB);
        return this;
    }

    public void setCostB(Double costB) {
        this.costB = costB;
    }

    public Double getCostC() {
        return this.costC;
    }

    public StorageCost costC(Double costC) {
        this.setCostC(costC);
        return this;
    }

    public void setCostC(Double costC) {
        this.costC = costC;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StorageCost)) {
            return false;
        }
        return id != null && id.equals(((StorageCost) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StorageCost{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", costA=" + getCostA() +
            ", costB=" + getCostB() +
            ", costC=" + getCostC() +
            "}";
    }
}
