package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
public class JspMealController extends AbstractMealController{

    @RequestMapping(value = "/meals/delete")
    public String delete(@RequestParam int id)
    {
        log.info("deleting meal {} from user {}",id, SecurityUtil.authUserId());
        service.delete(id, SecurityUtil.authUserId());
        return "redirect:/meals";
    }

    @RequestMapping(value = "/meals/update")
    public ModelAndView update(@RequestParam int id) throws IOException {
        log.info("updating meal {} for user {}", id, SecurityUtil.authUserId());
        ModelAndView mav = new ModelAndView("mealForm");
        Meal meal = service.get(id,SecurityUtil.authUserId());
        mav.addObject("meal",meal);
        return mav;
    }

    @RequestMapping(value = "/meals/create")
    public ModelAndView create() throws IOException {
        log.info("create new meal for user {}", SecurityUtil.authUserId());
        ModelAndView mav = new ModelAndView("mealForm");
        Meal meal = new Meal();
        mav.addObject("meal",meal);
        return mav;
    }
    @RequestMapping(value = "/meals")
    public String getAll(Model model)
    {
        log.info("get all meals for user {}", SecurityUtil.authUserId());
        model.addAttribute("meals", MealsUtil.getTos(service.getAll(SecurityUtil.authUserId()),SecurityUtil.authUserCaloriesPerDay()));
        return "meals";
    }

    @RequestMapping(value = "/meals/save", method = RequestMethod.POST)
    public String save (HttpServletRequest request) throws IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            meal.setId(getId(request));
            log.info("update meal {} for user {}", meal,SecurityUtil.authUserId());
            service.update(meal, SecurityUtil.authUserId());
        } else {
            log.info("save meal {} for user {}", meal,SecurityUtil.authUserId());
            service.create(meal,SecurityUtil.authUserId());
        }
        return "redirect:/meals";
    }

    @RequestMapping("/meals/filter")
    public String getFiltered(Model model, HttpServletRequest request)
    {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        model.addAttribute("meals", getBetween(startDate,startTime,endDate,endTime));
        return "meals";
    }


    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
