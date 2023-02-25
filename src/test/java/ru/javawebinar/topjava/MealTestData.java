package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int ADMIN_BREAKFAST_ID = START_SEQ+3;
    public static final int ADMIN_LUNCH_ID = START_SEQ+4;
    public static final int ADMIN_DINNER_ID = START_SEQ+5;
    public static final int USER_LUNCH_ID = START_SEQ+6;
    public static final int USER_DINNER_ID = START_SEQ+7;

    public static final int USER_ID = START_SEQ;
    public static final int ADMIN_ID = START_SEQ + 1;
    public static final int GUEST_ID = START_SEQ + 2;

    public static final LocalDateTime BREAKFAST_TIME = LocalDateTime.of(2022, Month.JANUARY,8,8,0,0);
    public static final LocalDateTime LUNCH_TIME = LocalDateTime.of(2022, Month.JANUARY,8,13,2,0);
    public static final LocalDateTime DINNER_TIME = LocalDateTime.of(2022, Month.JANUARY,8,18,1,0);
    public static final LocalDateTime NEW_TIME = LocalDateTime.of(1996,Month.APRIL,24,20,0,0);

    public static final Meal admin_breakfast = new Meal(ADMIN_BREAKFAST_ID,BREAKFAST_TIME,"Админ завтрак",510);
    public static final Meal admin_lunch = new Meal(ADMIN_LUNCH_ID,LUNCH_TIME,"Админ обед",700);
    public static final Meal admin_dinner = new Meal(ADMIN_DINNER_ID,DINNER_TIME,"Админ ужин",510);
    public static final Meal user_lunch = new Meal(USER_LUNCH_ID,LUNCH_TIME,"Юзер обед",1200);
    public static final Meal user_dinner = new Meal(USER_DINNER_ID,DINNER_TIME,"Юзер ужин",600);


    public static Meal getNew()
    {
        return new Meal(null,NEW_TIME ,"Test description",500);
    }

    public static Meal getUpdated()
    {
        Meal updated = new Meal(admin_breakfast);
        updated.setDateTime(NEW_TIME);
        updated.setCalories(1000);
        updated.setDescription("Test description");
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected)
    {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingRecursiveFieldByFieldElementComparator().isEqualTo(expected);
    }


}
