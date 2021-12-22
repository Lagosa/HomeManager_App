package com.Lagosa.homemanager_app.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.Lagosa.homemanager_app.Database.MainActivityCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_main_fragment,container,false);

        UUID userId = UUID.fromString(getArguments().get("userId").toString());
        ServerCalls serverCalls = new ServerCalls(inflater.getContext());

        serverCalls.getNotifications(new MainActivityCallback() {
            @Override
            public void gotNotificationsList(List<Map<String, String>> notificationsList) {
                if(notificationsList.size() != 0){
                    view.findViewById(R.id.mainNoNotifications).setVisibility(View.GONE);
                }
                RecyclerView notificationContainer = view.findViewById(R.id.mainNotificationsContainer);
                notificationContainer.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
                NotificationAdapter adapter = new NotificationAdapter(inflater.getContext(),notificationsList);
                notificationContainer.setAdapter(adapter);
            }

            @Override
            public void gotWhoIsHomeList(List<String> whoIsHomeList) {

            }
        },userId);

        Button sendNotification = view.findViewById(R.id.mainSendNotification);
        sendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, new SendNotificationFragment());
                fragmentTransaction.commit();
            }
        });

        serverCalls.getWhoIsHome(new MainActivityCallback() {
            @Override
            public void gotNotificationsList(List<Map<String, String>> notificationsList) {

            }

            @Override
            public void gotWhoIsHomeList(List<String> whoIsHomeList) {
                if(whoIsHomeList.size() > 0){
                    view.findViewById(R.id.mainNoHome).setVisibility(View.GONE);
                }

                RecyclerView whoIsHomeContainer = view.findViewById(R.id.mainWhoIsHomeContainer);
                WhoIsHomeCardAdapter adapter = new WhoIsHomeCardAdapter(getContext(),whoIsHomeList);
                whoIsHomeContainer.setLayoutManager(new LinearLayoutManager(getContext()));
                whoIsHomeContainer.setAdapter(adapter);
            }
        },userId);

        Button arrivedHome = view.findViewById(R.id.mainIamHomeBtn);
        arrivedHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverCalls.setArrivedHome(userId);
            }
        });

        Button leftHome = view.findViewById(R.id.mainILeftBtn);
        leftHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverCalls.setLeftHome(userId);
            }
        });

        return view;
    }
}
