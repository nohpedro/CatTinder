package com.example.configms.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserPrefsRepositoryImpl implements UserPrefsRepositoryCustom {

  @PersistenceContext
  private EntityManager em;

  @Override
  public List<UserPrefsEntity> searchByFilters(Boolean darkMode, Boolean showOnlineStatus,
                                               Boolean pushNotifications, Boolean showInfo) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UserPrefsEntity> cq = cb.createQuery(UserPrefsEntity.class);
    Root<UserPrefsEntity> root = cq.from(UserPrefsEntity.class);

    List<Predicate> preds = new ArrayList<>();
    if (darkMode != null) preds.add(cb.equal(root.get("darkMode"), darkMode));
    if (showOnlineStatus != null) preds.add(cb.equal(root.get("showOnlineStatus"), showOnlineStatus));
    if (pushNotifications != null) preds.add(cb.equal(root.get("pushNotifications"), pushNotifications));
    if (showInfo != null) preds.add(cb.equal(root.get("showInfo"), showInfo));

    cq.where(preds.toArray(Predicate[]::new));
    return em.createQuery(cq).getResultList();
  }
}
