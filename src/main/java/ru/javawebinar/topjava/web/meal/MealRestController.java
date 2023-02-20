package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.getAuthUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll()
    {
        log.info("get all for user {}", getAuthUserId());
        return MealsUtil.getTos(service.getAll(getAuthUserId()), authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get meal {} for user {} meal:{}", id, getAuthUserId(), service.get(id, getAuthUserId()));
        return service.get(id, getAuthUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} for user {}", meal, getAuthUserId());
        checkNew(meal);
        return service.create(meal, getAuthUserId());
    }

    public void delete(int id) {
        log.info("delete meal {} for user {}", id, getAuthUserId());
        service.delete(id, getAuthUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} for user {}", meal, id, getAuthUserId());
        assureIdConsistent(meal, id);
        service.update(meal, getAuthUserId());
    }

    public List<MealTo> getBetween(@Nullable LocalDate startDate,@Nullable LocalTime startTime,@Nullable LocalDate endDate,@Nullable LocalTime endTime)
    {
        log.info("getBetween dates ({} - {}) time ({} - {}) for user {}", startDate,endDate,startTime,endTime, getAuthUserId());
        List<Meal> dateFilter = service.getFilteredByDate(startDate,endDate, getAuthUserId());
        return MealsUtil.getFilteredTos(dateFilter,authUserCaloriesPerDay(),startTime,endTime);
    }

}