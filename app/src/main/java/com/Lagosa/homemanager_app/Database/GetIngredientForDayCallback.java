package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface GetIngredientForDayCallback {
    void gotIngredients(List<Map<String,Object>> ingredientList);
}
