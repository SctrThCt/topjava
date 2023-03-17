package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public abstract class BaseJdbcRepository {

    protected JdbcTemplate jdbcTemplate;

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    protected SimpleJdbcInsert insert;

    protected Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public void validate(Object o)
    {
        Set<ConstraintViolation<Object>> violations = validator.validate(o);
        if (!violations.isEmpty())
            throw new ConstraintViolationException(violations);
    }
}
