package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface MainActivityCallback {
    void gotNotificationsList(List<Map<String,String>> notificationsList);
    void gotWhoIsHomeList(List<String> whoIsHomeList);
}
