package com.Lagosa.homemanager_app.Database;

import com.Lagosa.homemanager_app.ui.Reports.Report;

import java.util.List;

public interface ReportCallback {
    void gotReport(List<Report> reportList);
}
