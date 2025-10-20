package com.example.configms.persistence;

import java.util.List;

public interface UserPrefsRepositoryCustom {
  List<UserPrefsEntity> searchByFilters(Boolean darkMode, Boolean showOnlineStatus,
                                        Boolean pushNotifications, Boolean showInfo);
}
