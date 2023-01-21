package ru.javawebinar.topjava.util;

import org.w3c.dom.ls.LSOutput;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.chrono.ChronoLocalDateTime;
import java.util.*;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(10, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

//        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with excess. Implement by cycles

        List<UserMealWithExcess> out = new ArrayList<>();
        Map<LocalDate,Integer> calories = new HashMap<>();
        for(UserMeal meal:meals)
        {
            if (!calories.containsKey(meal.getDateTime().toLocalDate()))
            {
                calories.put(meal.getDateTime().toLocalDate(),meal.getCalories());
            } else {
                Integer currentCalories = calories.get(meal.getDateTime().toLocalDate());
                calories.put(meal.getDateTime().toLocalDate(), currentCalories+meal.getCalories());
            }
            System.out.println(calories.get(meal.getDateTime().toLocalDate()));
        }
        for (UserMeal meal:meals)
        {

            if (startTime.equals(meal.getDateTime().toLocalTime())||startTime.isBefore(meal.getDateTime().toLocalTime())
            &&endTime.isAfter(meal.getDateTime().toLocalTime()))
            {
                out.add(new UserMealWithExcess(meal.getDateTime(),
                        meal.getDescription(), meal.getCalories(),
                        calories.get(meal.getDateTime().toLocalDate())>caloriesPerDay));
            }
        }
        return out;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO Implement by streams
        return null;
    }
}
