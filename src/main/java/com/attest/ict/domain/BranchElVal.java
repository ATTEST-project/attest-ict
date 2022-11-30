package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BranchElVal.
 */
@Entity
@Table(name = "branch_el_val")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BranchElVal implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "hour")
    private Integer hour;

    @Column(name = "min")
    private Integer min;

    @Column(name = "p")
    private Double p;

    @Column(name = "q")
    private Double q;

    @Column(name = "status")
    private Integer status;

    @Column(name = "branch_id_on_subst")
    private Long branchIdOnSubst;

    @Column(name = "nominal_voltage")
    private String nominalVoltage;

    @ManyToOne
    @JsonIgnoreProperties(value = { "transfElVals", "branchElVals", "branchExtension", "network" }, allowSetters = true)
    private Branch branch;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFile", "branchElVals", "network" }, allowSetters = true)
    private BranchProfile branchProfile;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BranchElVal id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getHour() {
        return this.hour;
    }

    public BranchElVal hour(Integer hour) {
        this.setHour(hour);
        return this;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMin() {
        return this.min;
    }

    public BranchElVal min(Integer min) {
        this.setMin(min);
        return this;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Double getP() {
        return this.p;
    }

    public BranchElVal p(Double p) {
        this.setP(p);
        return this;
    }

    public void setP(Double p) {
        this.p = p;
    }

    public Double getQ() {
        return this.q;
    }

    public BranchElVal q(Double q) {
        this.setQ(q);
        return this;
    }

    public void setQ(Double q) {
        this.q = q;
    }

    public Integer getStatus() {
        return this.status;
    }

    public BranchElVal status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getBranchIdOnSubst() {
        return this.branchIdOnSubst;
    }

    public BranchElVal branchIdOnSubst(Long branchIdOnSubst) {
        this.setBranchIdOnSubst(branchIdOnSubst);
        return this;
    }

    public void setBranchIdOnSubst(Long branchIdOnSubst) {
        this.branchIdOnSubst = branchIdOnSubst;
    }

    public String getNominalVoltage() {
        return this.nominalVoltage;
    }

    public BranchElVal nominalVoltage(String nominalVoltage) {
        this.setNominalVoltage(nominalVoltage);
        return this;
    }

    public void setNominalVoltage(String nominalVoltage) {
        this.nominalVoltage = nominalVoltage;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BranchElVal branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    public BranchProfile getBranchProfile() {
        return this.branchProfile;
    }

    public void setBranchProfile(BranchProfile branchProfile) {
        this.branchProfile = branchProfile;
    }

    public BranchElVal branchProfile(BranchProfile branchProfile) {
        this.setBranchProfile(branchProfile);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchElVal)) {
            return false;
        }
        return id != null && id.equals(((BranchElVal) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchElVal{" +
            "id=" + getId() +
            ", hour=" + getHour() +
            ", min=" + getMin() +
            ", p=" + getP() +
            ", q=" + getQ() +
            ", status=" + getStatus() +
            ", branchIdOnSubst=" + getBranchIdOnSubst() +
            ", nominalVoltage='" + getNominalVoltage() + "'" +
            "}";
    }
}
