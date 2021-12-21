package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface PollListCallback {
    void gotOpenPolls(List<Map<String,Object>> openPollsList);
    void gotClosedPolls(List<Map<String,Object>> closedPollsList);
}
