package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Branch.
 */
@Entity
@Table(name = "branch")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Branch implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "fbus")
    private Long fbus;

    @Column(name = "tbus")
    private Long tbus;

    @Column(name = "r")
    private Double r;

    @Column(name = "x")
    private Double x;

    @Column(name = "b")
    private Double b;

    @Column(name = "ratea")
    private Double ratea;

    @Column(name = "rateb")
    private Double rateb;

    @Column(name = "ratec")
    private Double ratec;

    @Column(name = "tap_ratio")
    private Double tapRatio;

    @Column(name = "angle")
    private Double angle;

    @Column(name = "status")
    private Integer status;

    @Column(name = "angmin")
    private Integer angmin;

    @Column(name = "angmax")
    private Integer angmax;

    @OneToMany(mappedBy = "branch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "transfProfile", "branch" }, allowSetters = true)
    private Set<TransfElVal> transfElVals = new HashSet<>();

    @OneToMany(mappedBy = "branch")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "branch", "branchProfile" }, allowSetters = true)
    private Set<BranchElVal> branchElVals = new HashSet<>();

    @JsonIgnoreProperties(value = { "branch" }, allowSetters = true)
    @OneToOne(mappedBy = "branch", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private BranchExtension branchExtension;

    @ManyToOne
    @JsonIgnoreProperties(
        value = {
            "buses",
            "generators",
            "branches",
            "storages",
            "transformers",
            "capacitors",
            "inputFiles",
            "assetUgCables",
            "assetTransformers",
            "billingConsumptions",
            "billingDers",
            "lineCables",
            "genProfiles",
            "loadProfiles",
            "flexProfiles",
            "transfProfiles",
            "branchProfiles",
            "topologyBuses",
            "dsoTsoConnections",
            "baseMVA",
            "voltageLevel",
            "simulations",
        },
        allowSetters = true
    )
    private Network network;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Branch id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFbus() {
        return this.fbus;
    }

    public Branch fbus(Long fbus) {
        this.setFbus(fbus);
        return this;
    }

    public void setFbus(Long fbus) {
        this.fbus = fbus;
    }

    public Long getTbus() {
        return this.tbus;
    }

    public Branch tbus(Long tbus) {
        this.setTbus(tbus);
        return this;
    }

    public void setTbus(Long tbus) {
        this.tbus = tbus;
    }

    public Double getR() {
        return this.r;
    }

    public Branch r(Double r) {
        this.setR(r);
        return this;
    }

    public void setR(Double r) {
        this.r = r;
    }

    public Double getX() {
        return this.x;
    }

    public Branch x(Double x) {
        this.setX(x);
        return this;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getB() {
        return this.b;
    }

    public Branch b(Double b) {
        this.setB(b);
        return this;
    }

    public void setB(Double b) {
        this.b = b;
    }

    public Double getRatea() {
        return this.ratea;
    }

    public Branch ratea(Double ratea) {
        this.setRatea(ratea);
        return this;
    }

    public void setRatea(Double ratea) {
        this.ratea = ratea;
    }

    public Double getRateb() {
        return this.rateb;
    }

    public Branch rateb(Double rateb) {
        this.setRateb(rateb);
        return this;
    }

    public void setRateb(Double rateb) {
        this.rateb = rateb;
    }

    public Double getRatec() {
        return this.ratec;
    }

    public Branch ratec(Double ratec) {
        this.setRatec(ratec);
        return this;
    }

    public void setRatec(Double ratec) {
        this.ratec = ratec;
    }

    public Double getTapRatio() {
        return this.tapRatio;
    }

    public Branch tapRatio(Double tapRatio) {
        this.setTapRatio(tapRatio);
        return this;
    }

    public void setTapRatio(Double tapRatio) {
        this.tapRatio = tapRatio;
    }

    public Double getAngle() {
        return this.angle;
    }

    public Branch angle(Double angle) {
        this.setAngle(angle);
        return this;
    }

    public void setAngle(Double angle) {
        this.angle = angle;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Branch status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getAngmin() {
        return this.angmin;
    }

    public Branch angmin(Integer angmin) {
        this.setAngmin(angmin);
        return this;
    }

    public void setAngmin(Integer angmin) {
        this.angmin = angmin;
    }

    public Integer getAngmax() {
        return this.angmax;
    }

    public Branch angmax(Integer angmax) {
        this.setAngmax(angmax);
        return this;
    }

    public void setAngmax(Integer angmax) {
        this.angmax = angmax;
    }

    public Set<TransfElVal> getTransfElVals() {
        return this.transfElVals;
    }

    public void setTransfElVals(Set<TransfElVal> transfElVals) {
        if (this.transfElVals != null) {
            this.transfElVals.forEach(i -> i.setBranch(null));
        }
        if (transfElVals != null) {
            transfElVals.forEach(i -> i.setBranch(this));
        }
        this.transfElVals = transfElVals;
    }

    public Branch transfElVals(Set<TransfElVal> transfElVals) {
        this.setTransfElVals(transfElVals);
        return this;
    }

    public Branch addTransfElVal(TransfElVal transfElVal) {
        this.transfElVals.add(transfElVal);
        transfElVal.setBranch(this);
        return this;
    }

    public Branch removeTransfElVal(TransfElVal transfElVal) {
        this.transfElVals.remove(transfElVal);
        transfElVal.setBranch(null);
        return this;
    }

    public Set<BranchElVal> getBranchElVals() {
        return this.branchElVals;
    }

    public void setBranchElVals(Set<BranchElVal> branchElVals) {
        if (this.branchElVals != null) {
            this.branchElVals.forEach(i -> i.setBranch(null));
        }
        if (branchElVals != null) {
            branchElVals.forEach(i -> i.setBranch(this));
        }
        this.branchElVals = branchElVals;
    }

    public Branch branchElVals(Set<BranchElVal> branchElVals) {
        this.setBranchElVals(branchElVals);
        return this;
    }

    public Branch addBranchElVal(BranchElVal branchElVal) {
        this.branchElVals.add(branchElVal);
        branchElVal.setBranch(this);
        return this;
    }

    public Branch removeBranchElVal(BranchElVal branchElVal) {
        this.branchElVals.remove(branchElVal);
        branchElVal.setBranch(null);
        return this;
    }

    public BranchExtension getBranchExtension() {
        return this.branchExtension;
    }

    public void setBranchExtension(BranchExtension branchExtension) {
        if (this.branchExtension != null) {
            this.branchExtension.setBranch(null);
        }
        if (branchExtension != null) {
            branchExtension.setBranch(this);
        }
        this.branchExtension = branchExtension;
    }

    public Branch branchExtension(BranchExtension branchExtension) {
        this.setBranchExtension(branchExtension);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Branch network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Branch)) {
            return false;
        }
        return id != null && id.equals(((Branch) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Branch{" +
            "id=" + getId() +
            ", fbus=" + getFbus() +
            ", tbus=" + getTbus() +
            ", r=" + getR() +
            ", x=" + getX() +
            ", b=" + getB() +
            ", ratea=" + getRatea() +
            ", rateb=" + getRateb() +
            ", ratec=" + getRatec() +
            ", tapRatio=" + getTapRatio() +
            ", angle=" + getAngle() +
            ", status=" + getStatus() +
            ", angmin=" + getAngmin() +
            ", angmax=" + getAngmax() +
            "}";
    }
}
