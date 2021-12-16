package com.Lagosa.homemanager_app.Database;

import com.Lagosa.homemanager_app.ui.Chores.Chore;

import java.util.List;

public interface ChoreListCallback {
    void setNotDoneChoreList(List<Chore> chores);
}
