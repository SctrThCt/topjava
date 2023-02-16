package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;


import java.util.List;

@Controller
public class MealRestController {
    @Autowired
    private MealService service;
    private final Logger log = LoggerFactory.getLogger(getClass());

    public List<MealTo> getAll()
    {
        log.info("get all for user {}", authUserId());
        return MealsUtil.getTos(service.getAll(authUserId()),authUserCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get meal {} for user {}", id, authUserId());
        return service.get(id,authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {} for user {}", meal, authUserId());
        checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) {
        log.info("delete meal {} for user {}", id, authUserId());
        service.delete(id,authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {} with id={} for user {}", meal, id, authUserId());
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

}