package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class MealsUtil {
    public static void main(String[] args) {
        List<Meal> meals = Arrays.asList(
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new Meal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<MealTo> mealsTo = filteredByCycles(meals, LocalTime.of(10, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));

    }

    public static List<MealTo> filteredByCycles(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<MealTo> out = new ArrayList<>();
        Map<LocalDate, Integer> calories = new HashMap<>();
        for (Meal meal : meals) {
            LocalDate date = meal.getDateTime().toLocalDate();
            calories.put(date, calories.getOrDefault(date, 0) + meal.getCalories());
        }
        for (Meal meal : meals) {

            if (startTime.equals(meal.getDateTime().toLocalTime()) || startTime.isBefore(meal.getDateTime().toLocalTime())
                    && endTime.isAfter(meal.getDateTime().toLocalTime())) {
                out.add(new MealTo(meal.getDateTime(),
                        meal.getDescription(), meal.getCalories(),
                        calories.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return out;
    }

    public static List<MealTo> filteredByStreams(List<Meal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        Map<LocalDate, Integer> calories = meals.stream().collect(
                Collectors.groupingBy
                        (Meal::getDate, Collectors.summingInt(Meal::getCalories)));

        return meals.stream().
                filter(e -> TimeUtil.isBetweenHalfOpen(e.getTime(), startTime, endTime)).
                map(e -> new MealTo(e, calories.get(e.getDate()) > caloriesPerDay)).
                collect(Collectors.toList());
    }

}
