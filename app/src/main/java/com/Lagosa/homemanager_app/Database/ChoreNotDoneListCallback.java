package com.Lagosa.homemanager_app.Database;

import com.Lagosa.homemanager_app.ui.Chores.Chore;

import java.util.List;

public interface ChoreNotDoneListCallback {
    void setNotDoneChoreList(List<Chore> chores);
}
