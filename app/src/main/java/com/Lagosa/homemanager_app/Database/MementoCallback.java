package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface MementoCallback {
    void gotMementos(List<Map<String,Object>> mementoList);
}
