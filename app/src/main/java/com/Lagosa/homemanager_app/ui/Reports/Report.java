package com.Lagosa.homemanager_app.ui.Reports;

import java.util.List;
import java.util.Map;

public class Report {
    private final String userName;
    private final int nrDone;
    private final int nrNotDone;
    private final List<Map<String,Object>> listDone;
    private final List<Map<String,Object>> listNotDone;


    public Report(String userName, int nrDone, int nrNotDone, List<Map<String, Object>> listDone, List<Map<String, Object>> listNotDone) {
        this.userName = userName;
        this.nrDone = nrDone;
        this.nrNotDone = nrNotDone;
        this.listDone = listDone;
        this.listNotDone = listNotDone;
    }

    public String getUserName() {
        return userName;
    }

    public int getNrDone() {
        return nrDone;
    }

    public int getNrNotDone() {
        return nrNotDone;
    }

    public List<Map<String, Object>> getListDone() {
        return listDone;
    }

    public List<Map<String, Object>> getListNotDone() {
        return listNotDone;
    }
}
