package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Task.
 */
@Entity
@Table(name = "task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Task implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "info")
    private String info;

    @Column(name = "date_time_start")
    private Instant dateTimeStart;

    @Column(name = "date_time_end")
    private Instant dateTimeEnd;

    @JsonIgnoreProperties(value = { "task" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private ToolLogFile toolLogFile;

    @JsonIgnoreProperties(value = { "network", "inputFiles", "task", "outputFiles" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Simulation simulation;

    @ManyToOne
    @JsonIgnoreProperties(value = { "inputFiles", "outputFiles", "tasks", "parameters" }, allowSetters = true)
    private Tool tool;

    @ManyToOne
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Task id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskStatus() {
        return this.taskStatus;
    }

    public Task taskStatus(String taskStatus) {
        this.setTaskStatus(taskStatus);
        return this;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getInfo() {
        return this.info;
    }

    public Task info(String info) {
        this.setInfo(info);
        return this;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public Instant getDateTimeStart() {
        return this.dateTimeStart;
    }

    public Task dateTimeStart(Instant dateTimeStart) {
        this.setDateTimeStart(dateTimeStart);
        return this;
    }

    public void setDateTimeStart(Instant dateTimeStart) {
        this.dateTimeStart = dateTimeStart;
    }

    public Instant getDateTimeEnd() {
        return this.dateTimeEnd;
    }

    public Task dateTimeEnd(Instant dateTimeEnd) {
        this.setDateTimeEnd(dateTimeEnd);
        return this;
    }

    public void setDateTimeEnd(Instant dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    public ToolLogFile getToolLogFile() {
        return this.toolLogFile;
    }

    public void setToolLogFile(ToolLogFile toolLogFile) {
        this.toolLogFile = toolLogFile;
    }

    public Task toolLogFile(ToolLogFile toolLogFile) {
        this.setToolLogFile(toolLogFile);
        return this;
    }

    public Simulation getSimulation() {
        return this.simulation;
    }

    public void setSimulation(Simulation simulation) {
        this.simulation = simulation;
    }

    public Task simulation(Simulation simulation) {
        this.setSimulation(simulation);
        return this;
    }

    public Tool getTool() {
        return this.tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    public Task tool(Tool tool) {
        this.setTool(tool);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Task user(User user) {
        this.setUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Task)) {
            return false;
        }
        return id != null && id.equals(((Task) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Task{" +
            "id=" + getId() +
            ", taskStatus='" + getTaskStatus() + "'" +
            ", info='" + getInfo() + "'" +
            ", dateTimeStart='" + getDateTimeStart() + "'" +
            ", dateTimeEnd='" + getDateTimeEnd() + "'" +
            "}";
    }
}
