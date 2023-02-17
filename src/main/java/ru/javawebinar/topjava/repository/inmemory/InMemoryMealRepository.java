package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    public InMemoryMealRepository() {
        for (Meal m : MealsUtil.meals) {
            save(m, SecurityUtil.authUserId());
        }
    }


    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meal.setUserId(userId);
            repository.put(meal.getId(), meal);
            return meal;
        } if (!MealsUtil.isUserOwnMeal(meal,userId)){
            return null;
        }
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        return MealsUtil.isUserOwnMeal(repository.get(id),userId)
                &&repository.remove(id) != null;
    }

    @Override
    public Meal get(int id,int userId) {
        Meal out = repository.get(id);
        if (out.getUserId()==SecurityUtil.authUserId())
        {
            return out;
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.values().stream().
                filter(e->MealsUtil.isUserOwnMeal(e,userId)).
                sorted((e1,e2)->e2.getDateTime().compareTo(e1.getDateTime())).
                collect(Collectors.toList());
    }

    public List<Meal> getFilteredByDate(LocalDateTime startDate, LocalDateTime endDate, int userId)
    {
        return getAll(userId).stream().
                filter(meal -> DateTimeUtil.isBetweenHalfOpen(meal.getDateTime(),startDate,endDate)).
                collect(Collectors.toList());
    }
}

