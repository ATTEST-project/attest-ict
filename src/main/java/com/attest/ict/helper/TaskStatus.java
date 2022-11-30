package com.attest.ict.helper;

public class TaskStatus {

    // public static final String FAILED = "FAILED";

    // public static final String ONGOING = "ONGOING";

    // public static final String PASSED = "PASSED";

    // private TaskStatus() {}

    private Status status;

    public enum Status {
        FAILED,
        ONGOING,
        PASSED,
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
