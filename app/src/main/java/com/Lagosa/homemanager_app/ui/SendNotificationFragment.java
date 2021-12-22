package com.Lagosa.homemanager_app.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Lagosa.homemanager_app.Database.DatabaseHelper;
import com.Lagosa.homemanager_app.Database.GetFamilyMembersCallback;
import com.Lagosa.homemanager_app.Database.ServerCalls;
import com.Lagosa.homemanager_app.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SendNotificationFragment extends Fragment {

    List<Map<String,Object>> familyMemberList;
    List<String> familyMemberNames;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.create_notification,container,false);

        EditText title = view.findViewById(R.id.notificationSendTitle);
        EditText message = view.findViewById(R.id.notificationSendMessage);

        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        Cursor data = dbHelper.getUserId();
        data.moveToFirst();
        UUID userId  = UUID.fromString(data.getString(0));

        Spinner familyMembersSpinner = view.findViewById(R.id.notificationSendReceiver);

        ServerCalls serverCalls = new ServerCalls(getContext());
        serverCalls.getFamilyMembers(new GetFamilyMembersCallback() {
            @Override
            public void gotFamilyMembers(List<Map<String, Object>> familyMembers) {
                List<String> memberName = new ArrayList<>();
                for(Map<String,Object> memberMap: familyMembers){
                    memberName.add(memberMap.get("nickname").toString());
                }

                familyMemberList = familyMembers;
                familyMemberNames = memberName;

                ArrayAdapter<String> myAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, memberName);
                myAdapter.setDropDownViewResource(R.layout.spinner_item);
                familyMembersSpinner.setAdapter(myAdapter);
            }
        },userId);

        Button send = view.findViewById(R.id.notificationSendSendBtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID familyMemberId = (UUID) familyMemberList.get(familyMemberNames.indexOf(familyMembersSpinner.getSelectedItem().toString())).get("id");

                serverCalls.sendNotification(userId,familyMemberId,title.getText().toString(),message.getText().toString());

                Bundle bundle = new Bundle();
                bundle.putString("userId",userId.toString());
                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(bundle);

                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, mainFragment);
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
