package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MockDB {

    private static MockDB db;
    private final ConcurrentHashMap<Integer,Meal> mealList;

    private MockDB()
    {
        mealList = new ConcurrentHashMap<>();
        Meal m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500);
        mealList.put(m.getId(),m);
        m = new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000);
        mealList.put(m.getId(),m);
        m =new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500);
        mealList.put(m.getId(),m);
        m =new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100);
        mealList.put(m.getId(),m);
        m =new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000);
        mealList.put(m.getId(),m);
        m =new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 4400);
        mealList.put(m.getId(),m);
        m =new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 420);
        mealList.put(m.getId(),m);
    }
    public static MockDB init()
    {
        if (db==null)
        {
            db = new MockDB();
        }

        return db;
    }

    private static AtomicInteger counter = new AtomicInteger(0);

    public static Integer generateID()
    {
        return counter.getAndIncrement();
    }

    public List<Meal> getMealList()
    {
        return mealList.values().stream().collect(Collectors.toList());
    }

    public void remove(Integer id) {
        mealList.remove(id);
    }

    public void update(Meal meal)
    {
        mealList.replace(meal.getId(), meal);
    }

    public void add(Meal meal)
    {
        mealList.put(meal.getId(), meal);
    }

}
