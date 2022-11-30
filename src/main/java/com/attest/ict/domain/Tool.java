package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tool.
 */
@Entity
@Table(name = "tool")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Tool implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "work_package", nullable = false)
    private String workPackage;

    @NotNull
    @Column(name = "num", nullable = false)
    private String num;

    @NotNull
    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "tool")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(
        value = { "tool", "genProfile", "flexProfile", "loadProfile", "transfProfile", "branchProfile", "network", "simulations" },
        allowSetters = true
    )
    private Set<InputFile> inputFiles = new HashSet<>();

    @OneToMany(mappedBy = "tool")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tool", "network", "simulation" }, allowSetters = true)
    private Set<OutputFile> outputFiles = new HashSet<>();

    @OneToMany(mappedBy = "tool")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "toolLogFile", "simulation", "tool", "user" }, allowSetters = true)
    private Set<Task> tasks = new HashSet<>();

    @OneToMany(mappedBy = "tool")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "tool" }, allowSetters = true)
    private Set<ToolParameter> parameters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tool id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkPackage() {
        return this.workPackage;
    }

    public Tool workPackage(String workPackage) {
        this.setWorkPackage(workPackage);
        return this;
    }

    public void setWorkPackage(String workPackage) {
        this.workPackage = workPackage;
    }

    public String getNum() {
        return this.num;
    }

    public Tool num(String num) {
        this.setNum(num);
        return this;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getName() {
        return this.name;
    }

    public Tool name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }

    public Tool path(String path) {
        this.setPath(path);
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return this.description;
    }

    public Tool description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<InputFile> getInputFiles() {
        return this.inputFiles;
    }

    public void setInputFiles(Set<InputFile> inputFiles) {
        if (this.inputFiles != null) {
            this.inputFiles.forEach(i -> i.setTool(null));
        }
        if (inputFiles != null) {
            inputFiles.forEach(i -> i.setTool(this));
        }
        this.inputFiles = inputFiles;
    }

    public Tool inputFiles(Set<InputFile> inputFiles) {
        this.setInputFiles(inputFiles);
        return this;
    }

    public Tool addInputFile(InputFile inputFile) {
        this.inputFiles.add(inputFile);
        inputFile.setTool(this);
        return this;
    }

    public Tool removeInputFile(InputFile inputFile) {
        this.inputFiles.remove(inputFile);
        inputFile.setTool(null);
        return this;
    }

    public Set<OutputFile> getOutputFiles() {
        return this.outputFiles;
    }

    public void setOutputFiles(Set<OutputFile> outputFiles) {
        if (this.outputFiles != null) {
            this.outputFiles.forEach(i -> i.setTool(null));
        }
        if (outputFiles != null) {
            outputFiles.forEach(i -> i.setTool(this));
        }
        this.outputFiles = outputFiles;
    }

    public Tool outputFiles(Set<OutputFile> outputFiles) {
        this.setOutputFiles(outputFiles);
        return this;
    }

    public Tool addOutputFile(OutputFile outputFile) {
        this.outputFiles.add(outputFile);
        outputFile.setTool(this);
        return this;
    }

    public Tool removeOutputFile(OutputFile outputFile) {
        this.outputFiles.remove(outputFile);
        outputFile.setTool(null);
        return this;
    }

    public Set<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<Task> tasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setTool(null));
        }
        if (tasks != null) {
            tasks.forEach(i -> i.setTool(this));
        }
        this.tasks = tasks;
    }

    public Tool tasks(Set<Task> tasks) {
        this.setTasks(tasks);
        return this;
    }

    public Tool addTask(Task task) {
        this.tasks.add(task);
        task.setTool(this);
        return this;
    }

    public Tool removeTask(Task task) {
        this.tasks.remove(task);
        task.setTool(null);
        return this;
    }

    public Set<ToolParameter> getParameters() {
        return this.parameters;
    }

    public void setParameters(Set<ToolParameter> toolParameters) {
        if (this.parameters != null) {
            this.parameters.forEach(i -> i.setTool(null));
        }
        if (toolParameters != null) {
            toolParameters.forEach(i -> i.setTool(this));
        }
        this.parameters = toolParameters;
    }

    public Tool parameters(Set<ToolParameter> toolParameters) {
        this.setParameters(toolParameters);
        return this;
    }

    public Tool addParameter(ToolParameter toolParameter) {
        this.parameters.add(toolParameter);
        toolParameter.setTool(this);
        return this;
    }

    public Tool removeParameter(ToolParameter toolParameter) {
        this.parameters.remove(toolParameter);
        toolParameter.setTool(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tool)) {
            return false;
        }
        return id != null && id.equals(((Tool) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tool{" +
            "id=" + getId() +
            ", workPackage='" + getWorkPackage() + "'" +
            ", num='" + getNum() + "'" +
            ", name='" + getName() + "'" +
            ", path='" + getPath() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
