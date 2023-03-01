package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepository implements MealRepository {

    @PersistenceContext
    EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User ref = em.getReference(User.class,userId);
        meal.setUser(ref);
        Meal temp;
        if (meal.isNew())
        {
            em.persist(meal);
            return meal;
        }
        else if ((temp = em.find(Meal.class,meal.getId()))!=null)
        {
            if (temp.getUser().id()==userId) {
                return em.merge(meal);
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        return em.createNamedQuery(Meal.DELETE)
                .setParameter("id",id)
                .setParameter("user_id",userId)
                .executeUpdate()!=0;
    }

    @Override
    public Meal get(int id, int userId) {

        List <Meal> result = em.createNamedQuery(Meal.GET,Meal.class)
                .setParameter("id",id)
                .setParameter("user_id",userId)
                .getResultList();
        return DataAccessUtils.singleResult(result);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return em.createNamedQuery(Meal.ALL_SORTED,Meal.class)
                .setParameter("user_id",userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return em.createNamedQuery(Meal.ALL_FILTERED,Meal.class)
                .setParameter("startDateTime", startDateTime)
                .setParameter("endDateTime", endDateTime)
                .setParameter("user_id", userId)
                .getResultList();
    }
}