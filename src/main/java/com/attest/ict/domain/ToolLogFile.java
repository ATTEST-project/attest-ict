package com.attest.ict.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ToolLogFile.
 */
@Entity
@Table(name = "tool_log_file")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ToolLogFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "description")
    private String description;

    @Lob
    @Column(name = "data")
    private byte[] data;

    @Column(name = "data_content_type")
    private String dataContentType;

    @Column(name = "upload_time")
    private Instant uploadTime;

    @JsonIgnoreProperties(value = { "toolLogFile", "simulation", "tool", "user" }, allowSetters = true)
    @OneToOne(mappedBy = "toolLogFile")
    private Task task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ToolLogFile id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return this.fileName;
    }

    public ToolLogFile fileName(String fileName) {
        this.setFileName(fileName);
        return this;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDescription() {
        return this.description;
    }

    public ToolLogFile description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getData() {
        return this.data;
    }

    public ToolLogFile data(byte[] data) {
        this.setData(data);
        return this;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public String getDataContentType() {
        return this.dataContentType;
    }

    public ToolLogFile dataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
        return this;
    }

    public void setDataContentType(String dataContentType) {
        this.dataContentType = dataContentType;
    }

    public Instant getUploadTime() {
        return this.uploadTime;
    }

    public ToolLogFile uploadTime(Instant uploadTime) {
        this.setUploadTime(uploadTime);
        return this;
    }

    public void setUploadTime(Instant uploadTime) {
        this.uploadTime = uploadTime;
    }

    public Task getTask() {
        return this.task;
    }

    public void setTask(Task task) {
        if (this.task != null) {
            this.task.setToolLogFile(null);
        }
        if (task != null) {
            task.setToolLogFile(this);
        }
        this.task = task;
    }

    public ToolLogFile task(Task task) {
        this.setTask(task);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ToolLogFile)) {
            return false;
        }
        return id != null && id.equals(((ToolLogFile) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ToolLogFile{" +
            "id=" + getId() +
            ", fileName='" + getFileName() + "'" +
            ", description='" + getDescription() + "'" +
            ", data='" + getData() + "'" +
            ", dataContentType='" + getDataContentType() + "'" +
            ", uploadTime='" + getUploadTime() + "'" +
            "}";
    }
}
