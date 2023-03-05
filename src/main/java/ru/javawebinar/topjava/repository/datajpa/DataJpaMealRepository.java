package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private static final Sort SORT_BY_DATE_TIME = Sort.by(Sort.Direction.DESC,"dateTime");
    private final CrudMealRepository crudRepository;

    private final EntityManager em;

    public DataJpaMealRepository(CrudMealRepository crudRepository, EntityManager em) {
        this.crudRepository = crudRepository;
        this.em = em;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (!meal.isNew())
        {
            Meal ref = crudRepository.findById(meal.getId()).get();
            if (ref.getUser().getId()!=userId)
            {
                return null;
            }
        }
        meal.setUser(em.getReference(User.class,userId));
        crudRepository.save(meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudRepository.delete(id,userId)!=0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudRepository.findMealByIdAndUserId(id,userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudRepository.getAllSortedByUserId(userId, SORT_BY_DATE_TIME);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return crudRepository.getFiltered(startDateTime,endDateTime,userId);
    }
}
