package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepositoryInMemory;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final Logger log = getLogger(MealServlet.class);
    private MealRepositoryInMemory repository = new MealRepositoryInMemory();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String action = req.getParameter("action");
        String forward = "";

        if (action==null)
        {
            log.debug("Show all meals");
            req.setAttribute("mealList", MealsUtil.filteredByStreams(repository.getAll().stream().collect(toList()),2000));
            req.getRequestDispatcher("/meals.jsp").forward(req,resp);
        }
        else {
            if (action.equalsIgnoreCase("add")) {
                log.debug("Forward to saveMeal to add meal");
                Meal newMeal = new Meal(LocalDateTime.now(), "", 0);
                req.setAttribute("meal", newMeal);
                req.getRequestDispatcher("/saveMeal.jsp").forward(req, resp);
            } else if (action.equalsIgnoreCase("update")) {
                log.debug("Forward to saveMeal to edit meal");
                req.setAttribute("meal", repository.getMeal(Integer.parseInt(req.getParameter("id"))));
                req.getRequestDispatcher("/saveMeal.jsp").forward(req, resp);
            } else if (action.equalsIgnoreCase("delete")) {
                log.debug("Delete meal");
                repository.delete(Integer.parseInt(req.getParameter("id")));
                resp.sendRedirect("meals");
            } else {
                log.debug("Show all meals");
                req.setAttribute("mealList", MealsUtil.filteredByStreams(repository.getAll().stream().collect(toList()), 2000));
                req.getRequestDispatcher("/meals.jsp").forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        Integer id = null;
        try {
            id = Integer.parseInt(req.getParameter("id"));
        }   catch (Exception e)
        {
            id = null;
        }
        LocalDateTime dateTime = null;
        try
        {
            dateTime = LocalDateTime.parse(req.getParameter("dateTime"));
        } catch (Exception e)
        {
            dateTime = null;
        }
        Integer calories = null;
        try
        {
            calories = Integer.parseInt(req.getParameter("calories"));
        } catch (Exception e)
        {
            calories=null;
        }
        String description = req.getParameter("description");

        if (id == null)
        {
            repository.save(new Meal(dateTime,description,calories));
        } else {
            Meal oldMeal = repository.getMeal(id);
            if(dateTime==null) dateTime=oldMeal.getDateTime();
            if(calories==null) calories=oldMeal.getCalories();
            if(description==null) description=oldMeal.getDescription();
            repository.save(new Meal(dateTime,description,calories,id));
        }

        req.setAttribute("list", repository.getAll().stream().collect(toList()));
        resp.sendRedirect("meals");
    }
}
