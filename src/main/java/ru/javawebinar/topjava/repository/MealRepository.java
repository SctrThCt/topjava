package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.Collection;

public interface MealRepository {

    Meal getMeal(int id);
    void delete(int id);
    Collection<Meal> getAll();
    Meal save (Meal meal);
}
