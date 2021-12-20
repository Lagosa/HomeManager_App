package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface DishCallback {
    void gotAllDishes(List<Map<String,Object>> dishesList);
}
