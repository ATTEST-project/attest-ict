package com.attest.ict.helper;

public class TaskStatus {

    private Status status;

    public enum Status {
        FAILED,
        ONGOING,
        PASSED,
        KILLED,
    }

    public boolean isFailed() {
        if (getStatus() == Status.FAILED) {
            return true;
        }
        return false;
    }

    public boolean isPassed() {
        if (getStatus() == Status.PASSED) {
            return true;
        }
        return false;
    }

    public boolean isOngoing() {
        if (getStatus() == Status.ONGOING) {
            return true;
        }
        return false;
    }

    public boolean isKilled() {
        if (getStatus() == Status.KILLED) {
            return true;
        }
        return false;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
