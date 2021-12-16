package com.Lagosa.homemanager_app.ui.Chores;

import java.sql.Date;

public class Chore {
    private int id;
    private final String submittedByName;
    private final Date submissionDate;
    private final Date deadline;
    private final String typeName;
    private final String description;
    private final String title;
    private String doneByName;
    private String status;

    public Chore(String submittedByName, Date submissionDate, Date deadline, String typeName, String description, String title) {
        this.submittedByName = submittedByName;
        this.submissionDate = submissionDate;
        this.deadline = deadline;
        this.typeName = typeName;
        this.description = description;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubmittedByName() {
        return submittedByName;
    }

    public Date getSubmissionDate() {
        return submissionDate;
    }

    public Date getDeadline() {
        return deadline;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public String getDoneByName() {
        return doneByName;
    }

    public void setDoneByName(String doneByName) {
        this.doneByName = doneByName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
