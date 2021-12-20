package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface IngredientCallback {
    void gotIngredients(List<Map<String,String>> ingredients);
}
