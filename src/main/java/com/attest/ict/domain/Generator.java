package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Generator.
 */
@Entity
@Table(name = "generator")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Generator implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "bus_num")
    private Long busNum;

    @Column(name = "pg")
    private Double pg;

    @Column(name = "qg")
    private Double qg;

    @Column(name = "qmax")
    private Double qmax;

    @Column(name = "qmin")
    private Double qmin;

    @Column(name = "vg")
    private Double vg;

    @Column(name = "m_base")
    private Double mBase;

    @Column(name = "status")
    private Integer status;

    @Column(name = "pmax")
    private Double pmax;

    @Column(name = "pmin")
    private Double pmin;

    @Column(name = "pc_1")
    private Double pc1;

    @Column(name = "pc_2")
    private Double pc2;

    @Column(name = "qc_1_min")
    private Double qc1min;

    @Column(name = "qc_1_max")
    private Double qc1max;

    @Column(name = "qc_2_min")
    private Double qc2min;

    @Column(name = "qc_2_max")
    private Double qc2max;

    @Column(name = "ramp_agc")
    private Double rampAgc;

    @Column(name = "ramp_10")
    private Double ramp10;

    @Column(name = "ramp_30")
    private Double ramp30;

    @Column(name = "ramp_q")
    private Double rampQ;

    @Column(name = "apf")
    private Long apf;

    @OneToMany(mappedBy = "generator")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "genProfile", "generator" }, allowSetters = true)
    private Set<GenElVal> genElVals = new HashSet<>();

    @JsonIgnoreProperties(value = { "generator" }, allowSetters = true)
    @OneToOne(mappedBy = "generator", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private GeneratorExtension generatorExtension;

    @JsonIgnoreProperties(value = { "generator" }, allowSetters = true)
    @OneToOne(mappedBy = "generator", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private GenTag genTag;

    @JsonIgnoreProperties(value = { "generator" }, allowSetters = true)
    @OneToOne(mappedBy = "generator", cascade = { CascadeType.REMOVE, CascadeType.REFRESH }, orphanRemoval = true)
    private GenCost genCost;

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

    public Generator id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusNum() {
        return this.busNum;
    }

    public Generator busNum(Long busNum) {
        this.setBusNum(busNum);
        return this;
    }

    public void setBusNum(Long busNum) {
        this.busNum = busNum;
    }

    public Double getPg() {
        return this.pg;
    }

    public Generator pg(Double pg) {
        this.setPg(pg);
        return this;
    }

    public void setPg(Double pg) {
        this.pg = pg;
    }

    public Double getQg() {
        return this.qg;
    }

    public Generator qg(Double qg) {
        this.setQg(qg);
        return this;
    }

    public void setQg(Double qg) {
        this.qg = qg;
    }

    public Double getQmax() {
        return this.qmax;
    }

    public Generator qmax(Double qmax) {
        this.setQmax(qmax);
        return this;
    }

    public void setQmax(Double qmax) {
        this.qmax = qmax;
    }

    public Double getQmin() {
        return this.qmin;
    }

    public Generator qmin(Double qmin) {
        this.setQmin(qmin);
        return this;
    }

    public void setQmin(Double qmin) {
        this.qmin = qmin;
    }

    public Double getVg() {
        return this.vg;
    }

    public Generator vg(Double vg) {
        this.setVg(vg);
        return this;
    }

    public void setVg(Double vg) {
        this.vg = vg;
    }

    public Double getmBase() {
        return this.mBase;
    }

    public Generator mBase(Double mBase) {
        this.setmBase(mBase);
        return this;
    }

    public void setmBase(Double mBase) {
        this.mBase = mBase;
    }

    public Integer getStatus() {
        return this.status;
    }

    public Generator status(Integer status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Double getPmax() {
        return this.pmax;
    }

    public Generator pmax(Double pmax) {
        this.setPmax(pmax);
        return this;
    }

    public void setPmax(Double pmax) {
        this.pmax = pmax;
    }

    public Double getPmin() {
        return this.pmin;
    }

    public Generator pmin(Double pmin) {
        this.setPmin(pmin);
        return this;
    }

    public void setPmin(Double pmin) {
        this.pmin = pmin;
    }

    public Double getPc1() {
        return this.pc1;
    }

    public Generator pc1(Double pc1) {
        this.setPc1(pc1);
        return this;
    }

    public void setPc1(Double pc1) {
        this.pc1 = pc1;
    }

    public Double getPc2() {
        return this.pc2;
    }

    public Generator pc2(Double pc2) {
        this.setPc2(pc2);
        return this;
    }

    public void setPc2(Double pc2) {
        this.pc2 = pc2;
    }

    public Double getQc1min() {
        return this.qc1min;
    }

    public Generator qc1min(Double qc1min) {
        this.setQc1min(qc1min);
        return this;
    }

    public void setQc1min(Double qc1min) {
        this.qc1min = qc1min;
    }

    public Double getQc1max() {
        return this.qc1max;
    }

    public Generator qc1max(Double qc1max) {
        this.setQc1max(qc1max);
        return this;
    }

    public void setQc1max(Double qc1max) {
        this.qc1max = qc1max;
    }

    public Double getQc2min() {
        return this.qc2min;
    }

    public Generator qc2min(Double qc2min) {
        this.setQc2min(qc2min);
        return this;
    }

    public void setQc2min(Double qc2min) {
        this.qc2min = qc2min;
    }

    public Double getQc2max() {
        return this.qc2max;
    }

    public Generator qc2max(Double qc2max) {
        this.setQc2max(qc2max);
        return this;
    }

    public void setQc2max(Double qc2max) {
        this.qc2max = qc2max;
    }

    public Double getRampAgc() {
        return this.rampAgc;
    }

    public Generator rampAgc(Double rampAgc) {
        this.setRampAgc(rampAgc);
        return this;
    }

    public void setRampAgc(Double rampAgc) {
        this.rampAgc = rampAgc;
    }

    public Double getRamp10() {
        return this.ramp10;
    }

    public Generator ramp10(Double ramp10) {
        this.setRamp10(ramp10);
        return this;
    }

    public void setRamp10(Double ramp10) {
        this.ramp10 = ramp10;
    }

    public Double getRamp30() {
        return this.ramp30;
    }

    public Generator ramp30(Double ramp30) {
        this.setRamp30(ramp30);
        return this;
    }

    public void setRamp30(Double ramp30) {
        this.ramp30 = ramp30;
    }

    public Double getRampQ() {
        return this.rampQ;
    }

    public Generator rampQ(Double rampQ) {
        this.setRampQ(rampQ);
        return this;
    }

    public void setRampQ(Double rampQ) {
        this.rampQ = rampQ;
    }

    public Long getApf() {
        return this.apf;
    }

    public Generator apf(Long apf) {
        this.setApf(apf);
        return this;
    }

    public void setApf(Long apf) {
        this.apf = apf;
    }

    public Set<GenElVal> getGenElVals() {
        return this.genElVals;
    }

    public void setGenElVals(Set<GenElVal> genElVals) {
        if (this.genElVals != null) {
            this.genElVals.forEach(i -> i.setGenerator(null));
        }
        if (genElVals != null) {
            genElVals.forEach(i -> i.setGenerator(this));
        }
        this.genElVals = genElVals;
    }

    public Generator genElVals(Set<GenElVal> genElVals) {
        this.setGenElVals(genElVals);
        return this;
    }

    public Generator addGenElVal(GenElVal genElVal) {
        this.genElVals.add(genElVal);
        genElVal.setGenerator(this);
        return this;
    }

    public Generator removeGenElVal(GenElVal genElVal) {
        this.genElVals.remove(genElVal);
        genElVal.setGenerator(null);
        return this;
    }

    public GeneratorExtension getGeneratorExtension() {
        return this.generatorExtension;
    }

    public void setGeneratorExtension(GeneratorExtension generatorExtension) {
        if (this.generatorExtension != null) {
            this.generatorExtension.setGenerator(null);
        }
        if (generatorExtension != null) {
            generatorExtension.setGenerator(this);
        }
        this.generatorExtension = generatorExtension;
    }

    public Generator generatorExtension(GeneratorExtension generatorExtension) {
        this.setGeneratorExtension(generatorExtension);
        return this;
    }

    public GenTag getGenTag() {
        return this.genTag;
    }

    public void setGenTag(GenTag genTag) {
        if (this.genTag != null) {
            this.genTag.setGenerator(null);
        }
        if (genTag != null) {
            genTag.setGenerator(this);
        }
        this.genTag = genTag;
    }

    public Generator genTag(GenTag genTag) {
        this.setGenTag(genTag);
        return this;
    }

    public GenCost getGenCost() {
        return this.genCost;
    }

    public void setGenCost(GenCost genCost) {
        if (this.genCost != null) {
            this.genCost.setGenerator(null);
        }
        if (genCost != null) {
            genCost.setGenerator(this);
        }
        this.genCost = genCost;
    }

    public Generator genCost(GenCost genCost) {
        this.setGenCost(genCost);
        return this;
    }

    public Network getNetwork() {
        return this.network;
    }

    public void setNetwork(Network network) {
        this.network = network;
    }

    public Generator network(Network network) {
        this.setNetwork(network);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Generator)) {
            return false;
        }
        return id != null && id.equals(((Generator) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Generator{" +
            "id=" + getId() +
            ", busNum=" + getBusNum() +
            ", pg=" + getPg() +
            ", qg=" + getQg() +
            ", qmax=" + getQmax() +
            ", qmin=" + getQmin() +
            ", vg=" + getVg() +
            ", mBase=" + getmBase() +
            ", status=" + getStatus() +
            ", pmax=" + getPmax() +
            ", pmin=" + getPmin() +
            ", pc1=" + getPc1() +
            ", pc2=" + getPc2() +
            ", qc1min=" + getQc1min() +
            ", qc1max=" + getQc1max() +
            ", qc2min=" + getQc2min() +
            ", qc2max=" + getQc2max() +
            ", rampAgc=" + getRampAgc() +
            ", ramp10=" + getRamp10() +
            ", ramp30=" + getRamp30() +
            ", rampQ=" + getRampQ() +
            ", apf=" + getApf() +
            "}";
    }
}
