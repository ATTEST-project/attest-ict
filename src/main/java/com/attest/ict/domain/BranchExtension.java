package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A BranchExtension.
 */
@Entity
@Table(name = "branch_extension")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class BranchExtension implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "step_size")
    private Double stepSize;

    @Column(name = "act_tap")
    private Double actTap;

    @Column(name = "min_tap")
    private Double minTap;

    @Column(name = "max_tap")
    private Double maxTap;

    @Column(name = "normal_tap")
    private Double normalTap;

    @Column(name = "nominal_ratio")
    private Double nominalRatio;

    @Column(name = "r_ip")
    private Double rIp;

    @Column(name = "r_n")
    private Double rN;

    @Column(name = "r_0")
    private Double r0;

    @Column(name = "x_0")
    private Double x0;

    @Column(name = "b_0")
    private Double b0;

    @Column(name = "length")
    private Double length;

    @Column(name = "norm_stat")
    private Integer normStat;

    @Column(name = "g")
    private Double g;

    @Column(name = "m_rid")
    private String mRid;

    @JsonIgnoreProperties(value = { "transfElVals", "branchElVals", "branchExtension", "network" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Branch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BranchExtension id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getStepSize() {
        return this.stepSize;
    }

    public BranchExtension stepSize(Double stepSize) {
        this.setStepSize(stepSize);
        return this;
    }

    public void setStepSize(Double stepSize) {
        this.stepSize = stepSize;
    }

    public Double getActTap() {
        return this.actTap;
    }

    public BranchExtension actTap(Double actTap) {
        this.setActTap(actTap);
        return this;
    }

    public void setActTap(Double actTap) {
        this.actTap = actTap;
    }

    public Double getMinTap() {
        return this.minTap;
    }

    public BranchExtension minTap(Double minTap) {
        this.setMinTap(minTap);
        return this;
    }

    public void setMinTap(Double minTap) {
        this.minTap = minTap;
    }

    public Double getMaxTap() {
        return this.maxTap;
    }

    public BranchExtension maxTap(Double maxTap) {
        this.setMaxTap(maxTap);
        return this;
    }

    public void setMaxTap(Double maxTap) {
        this.maxTap = maxTap;
    }

    public Double getNormalTap() {
        return this.normalTap;
    }

    public BranchExtension normalTap(Double normalTap) {
        this.setNormalTap(normalTap);
        return this;
    }

    public void setNormalTap(Double normalTap) {
        this.normalTap = normalTap;
    }

    public Double getNominalRatio() {
        return this.nominalRatio;
    }

    public BranchExtension nominalRatio(Double nominalRatio) {
        this.setNominalRatio(nominalRatio);
        return this;
    }

    public void setNominalRatio(Double nominalRatio) {
        this.nominalRatio = nominalRatio;
    }

    public Double getrIp() {
        return this.rIp;
    }

    public BranchExtension rIp(Double rIp) {
        this.setrIp(rIp);
        return this;
    }

    public void setrIp(Double rIp) {
        this.rIp = rIp;
    }

    public Double getrN() {
        return this.rN;
    }

    public BranchExtension rN(Double rN) {
        this.setrN(rN);
        return this;
    }

    public void setrN(Double rN) {
        this.rN = rN;
    }

    public Double getr0() {
        return this.r0;
    }

    public BranchExtension r0(Double r0) {
        this.setr0(r0);
        return this;
    }

    public void setr0(Double r0) {
        this.r0 = r0;
    }

    public Double getx0() {
        return this.x0;
    }

    public BranchExtension x0(Double x0) {
        this.setx0(x0);
        return this;
    }

    public void setx0(Double x0) {
        this.x0 = x0;
    }

    public Double getb0() {
        return this.b0;
    }

    public BranchExtension b0(Double b0) {
        this.setb0(b0);
        return this;
    }

    public void setb0(Double b0) {
        this.b0 = b0;
    }

    public Double getLength() {
        return this.length;
    }

    public BranchExtension length(Double length) {
        this.setLength(length);
        return this;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getNormStat() {
        return this.normStat;
    }

    public BranchExtension normStat(Integer normStat) {
        this.setNormStat(normStat);
        return this;
    }

    public void setNormStat(Integer normStat) {
        this.normStat = normStat;
    }

    public Double getG() {
        return this.g;
    }

    public BranchExtension g(Double g) {
        this.setG(g);
        return this;
    }

    public void setG(Double g) {
        this.g = g;
    }

    public String getmRid() {
        return this.mRid;
    }

    public BranchExtension mRid(String mRid) {
        this.setmRid(mRid);
        return this;
    }

    public void setmRid(String mRid) {
        this.mRid = mRid;
    }

    public Branch getBranch() {
        return this.branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public BranchExtension branch(Branch branch) {
        this.setBranch(branch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BranchExtension)) {
            return false;
        }
        return id != null && id.equals(((BranchExtension) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BranchExtension{" +
            "id=" + getId() +
            ", stepSize=" + getStepSize() +
            ", actTap=" + getActTap() +
            ", minTap=" + getMinTap() +
            ", maxTap=" + getMaxTap() +
            ", normalTap=" + getNormalTap() +
            ", nominalRatio=" + getNominalRatio() +
            ", rIp=" + getrIp() +
            ", rN=" + getrN() +
            ", r0=" + getr0() +
            ", x0=" + getx0() +
            ", b0=" + getb0() +
            ", length=" + getLength() +
            ", normStat=" + getNormStat() +
            ", g=" + getG() +
            ", mRid='" + getmRid() + "'" +
            "}";
    }
}
