package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {


    static {
        // Only for postgres driver logging
        // It uses java.util.logging and logged via jul-to-slf4j bridge
        SLF4JBridgeHandler.install();
    }

    @Autowired
    MealService service;

    @Test
    public void get() {
        Meal meal = service.get(ADMIN_BREAKFAST_ID,ADMIN_ID);
        assertMatch(meal,admin_breakfast);
    }
    @Test
    public void getNotFound()
    {
       assertThrows(NotFoundException.class,()->service.get(ADMIN_ID,ADMIN_ID));
    }

    @Test
    public void getNotOwn()
    {
        assertThrows(NotFoundException.class,()->service.get(ADMIN_BREAKFAST_ID,USER_ID));
    }

    @Test
    public void delete() {
        service.delete(USER_DINNER_ID,USER_ID);
        assertThrows(NotFoundException.class,()->service.get(USER_DINNER_ID,USER_ID));
    }

    @Test
    public void getBetweenInclusive() {

        List<Meal> meals = service.getBetweenInclusive(null, LocalDate.from(LUNCH_TIME.minusDays(1)),ADMIN_ID);
        assertMatch(meals, Collections.EMPTY_LIST);
    }

    @Test
    public void getAll() {
        List<Meal> all = service.getAll(USER_ID);
        assertMatch(all,user_lunch,user_dinner);
    }

    @Test
    public void update() {
        Meal updated = getUpdated();
        service.update(updated,ADMIN_ID);
        assertMatch(service.get(ADMIN_BREAKFAST_ID,ADMIN_ID),getUpdated());
    }

    @Test
    public void updateNotOwn()
    {
        Meal updated = getUpdated();
        assertThrows(NotFoundException.class, ()->service.update(updated,USER_ID));
    }

    @Test
    public void create() {
        Meal created = service.create(getNew(),USER_ID);
        Integer newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        assertMatch(created,newMeal);
        assertMatch(service.get(newId,USER_ID),newMeal);
    }

    @Test
    public void createDuplicateDate()
    {
        assertThrows(DataAccessException.class,()->
                service.create(new Meal(null,DINNER_TIME,"Duplicate description",1000),ADMIN_ID));
    }


}