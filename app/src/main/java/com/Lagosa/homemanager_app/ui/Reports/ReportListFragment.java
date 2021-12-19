package com.Lagosa.homemanager_app.ui.Reports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.Lagosa.homemanager_app.R;
import com.Lagosa.homemanager_app.ui.ViewModels.ReportViewModel;

public class ReportListFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View myView = inflater.inflate(R.layout.view_report_fragment,container,false);

        ReportViewModel viewModel = new ViewModelProvider(requireActivity()).get(ReportViewModel.class);
        viewModel.setReportRecyclerView(myView.findViewById(R.id.reportList));

        return myView;
    }
}
