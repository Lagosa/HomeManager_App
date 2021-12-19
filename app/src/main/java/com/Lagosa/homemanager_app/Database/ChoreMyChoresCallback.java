package com.Lagosa.homemanager_app.Database;

import com.Lagosa.homemanager_app.ui.Chores.Chore;

import java.util.List;

public interface ChoreMyChoresCallback {
    void gotMyChores(List<Chore> myChores);
}
