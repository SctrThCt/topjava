package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.MockDB;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class Meal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private final Integer id;

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        id = MockDB.generateID();
    }

    public Meal(LocalDateTime dateTime, String description, int calories, Integer id)
    {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public Integer getId() {
        return id;
    }
}
