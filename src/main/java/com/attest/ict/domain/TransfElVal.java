package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A TransfElVal.
 */
@Entity
@Table(name = "transf_el_val")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TransfElVal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "min")
    private Integer min;

    @Column(name = "tap_ratio")
    private Double tapRatio;

    @Column(name = "status")
    private Integer status;

    @Column(name = "trasf_id_on_subst")
    private Long trasfIdOnSubst;

    @Column(name = "nominal_voltage")
    private String nominalVoltage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "transfElVals", "network" }, allowSetters = true)
    private TransfProfile transfProfile;

    @ManyToOne
    @JsonIgnoreProperties(value = { "transfElVals", "branchElVals", "branchExtension", "network" }, allowSetters = true)
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public TransfElVal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return this.hour;
    }

    public TransfElVal hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return this.min;
    }

    public TransfElVal min(Integer min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getTapRatio() {
        return this.tapRatio;
    }

    public TransfElVal tapRatio(Double tapRatio) {
        this.setTapRatio(tapRatio);
        return this;
    }

    public void setTapRatio(Double tapRatio) {
        this.tapRatio = tapRatio;
    }

    public Integer getStatus() {
        return this.status;
    }

    public TransfElVal status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getTrasfIdOnSubst() {
        return this.trasfIdOnSubst;
    }

    public TransfElVal trasfIdOnSubst(Long trasfIdOnSubst) {
        this.setTrasfIdOnSubst(trasfIdOnSubst);
        return this;
    }

    public void setTrasfIdOnSubst(Long trasfIdOnSubst) {
        this.trasfIdOnSubst = trasfIdOnSubst;
    }

    public String getNominalVoltage() {
        return this.nominalVoltage;
    }

    public TransfElVal nominalVoltage(String nominalVoltage) {
        this.setNominalVoltage(nominalVoltage);
        return this;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public TransfProfile getTransfProfile() {
        return this.transfProfile;
    }

    public void setTransfProfile(TransfProfile transfProfile) {
        this.transfProfile = transfProfile;
    }

    public TransfElVal transfProfile(TransfProfile transfProfile) {
        this.setTransfProfile(transfProfile);
        return this;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public TransfElVal branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TransfElVal)) {
            return false;
        }
        return id != null && id.equals(((TransfElVal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "TransfElVal{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", tapRatio=" + getTapRatio() +
            ", status=" + getStatus() +
            ", trasfIdOnSubst=" + getTrasfIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            "}";
    }
}
