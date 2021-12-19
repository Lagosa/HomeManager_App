package com.Lagosa.homemanager_app.ui.ViewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

public class ReportViewModel extends ViewModel {
    private MutableLiveData<RecyclerView> reportRecyclerView = new MutableLiveData<>();
    private MutableLiveData<RecyclerView> reportDoneListRecyclerView = new MutableLiveData<>();
    private MutableLiveData<RecyclerView> reportNotDoneListRecyclerView = new MutableLiveData<>();

    public LiveData<RecyclerView> getReportRecyclerView() {
        return reportRecyclerView;
    }

    public void setReportRecyclerView(RecyclerView reportRecyclerView) {
        this.reportRecyclerView.setValue(reportRecyclerView);
    }

    public LiveData<RecyclerView> getReportDoneListRecyclerView() {
        return reportDoneListRecyclerView;
    }

    public void setReportDoneListRecyclerView(RecyclerView reportDoneListRecyclerView) {
        this.reportDoneListRecyclerView.setValue(reportDoneListRecyclerView);
    }

    public LiveData<RecyclerView> getReportNotDoneListRecyclerView() {
        return reportNotDoneListRecyclerView;
    }

    public void setReportNotDoneListRecyclerView(RecyclerView reportNotDoneListRecyclerView) {
        this.reportNotDoneListRecyclerView.setValue(reportNotDoneListRecyclerView);
    }
}
