package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.MockDB;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.util.List;

public class MealDAO {

    private final MockDB db;

    public MealDAO()
    {
        db = MockDB.init();
    }

    public void addMeal(Meal meal)
    {
        db.add(meal);
    }

    public List<Meal> getAllMeals()
    {
        return db.getMealList();
    }

    public Meal getMealById(int mealId)
    {
        List <Meal> list = db.getMealList();
        return list.stream().filter(e->e.getId().equals(mealId)).findAny().get();
    }

    public void updateMeal(Meal meal)
    {
        db.update(meal);
    }

    public void deleteMealById(int mealId)
    {
        db.remove(mealId);
    }

}
