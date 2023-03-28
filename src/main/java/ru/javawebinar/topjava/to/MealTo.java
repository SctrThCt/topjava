package ru.javawebinar.topjava.to;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class MealTo {
    private Integer id;
    private LocalDateTime dateTime;

    private String description;

    private int calories;

//    private final AtomicBoolean excess;      // filteredByAtomic (or any ref type, e.g. boolean[1])
//    private final Boolean excess;            // filteredByReflection
//    private final Supplier<Boolean> excess;  // filteredByClosure
    private boolean excess;
    public MealTo()
    {}
    public MealTo(Integer id, LocalDateTime dateTime, String description, int calories, boolean excess) {
        this.id = id;
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
        this.excess = excess;
    }

//    for filteredByClosure
//    public Boolean getExcess() {
//        return excess.get();
//    }

    // for filteredBySetterRecursion
    public void setExcess(boolean excess) {
        this.excess = excess;
    }

    public Integer getId() {
        return id;
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

    public boolean isExcess() {
        return excess;
    }

    @Override
    public String toString() {
        return "MealTo{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess +
                '}';
    }
}
