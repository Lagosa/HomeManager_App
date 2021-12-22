package com.Lagosa.homemanager_app.Database;

import java.util.List;
import java.util.Map;

public interface GetFamilyMembersCallback {
    void gotFamilyMembers(List<Map<String,Object>> familyMembers);
}
