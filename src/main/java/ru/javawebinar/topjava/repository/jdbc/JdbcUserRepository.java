package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    class CustomRowCallbackHandler implements RowCallbackHandler {

        private User u = new User();

        @Override
        public void processRow(ResultSet rs) throws SQLException {
            if (rs.getRow()!=0) {
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setId(rs.getInt("id"));
                u.setEnabled((rs.getBoolean("enabled")));
                u.setCaloriesPerDay(rs.getInt("calories_per_day"));
                u.setRegistered(rs.getDate("registered"));
                Set<Role> roles = new HashSet<>();
                do {
                    roles.add(Role.valueOf(rs.getString("role")));
                } while (rs.next());
                u.setRoles(roles);
            }
        }

        public User getU() {
            return u;
        }
    }

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {

        BatchPreparedStatementSetter statementSetter = new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setString(1,user.getRoles().toArray()[i].toString());
                ps.setInt(2,user.getId());
            }

            @Override
            public int getBatchSize() {
                return user.getRoles().size();
            }
        };
        MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("name",user.getName())
                .addValue("email",user.getEmail())
                .addValue("password",user.getPassword())
                .addValue("registered",user.getRegistered())
                .addValue("enabled",user.isEnabled())
                .addValue("caloriesPerDay",user.getCaloriesPerDay())
                .addValue("id",user.getId());
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("insert into user_role (role, user_id) values(?,?)",statementSetter);
        } else if (namedParameterJdbcTemplate.update("UPDATE users SET name=:name, email=:email, password=:password," +
                "registered=:registered, enabled=:enabled," +
                " calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        jdbcTemplate.batchUpdate("update user_role set role=? where user_id = ?",statementSetter);
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        CustomRowCallbackHandler handler = new CustomRowCallbackHandler();
        jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_role r ON id=r.user_id WHERE id=?", handler, id);
        User user = handler.getU();
        if (user.getName()==null)
        {
            return null;
        } else
        {
            return user;
        }

    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        CustomRowCallbackHandler handler = new CustomRowCallbackHandler();
        jdbcTemplate.query("SELECT * FROM users LEFT OUTER JOIN user_role r ON id=r.user_id WHERE email=?", handler, email);
        User user = handler.getU();
        try
        {
            user.getName();
        } catch (NullPointerException e)
        {
            return null;
        }
        return user;
    }

    @Override
    public List<User> getAll() {

        List <User> users = jdbcTemplate.query("SELECT * FROM users  ORDER BY  name, email",ROW_MAPPER);

        Map<Integer,Set<Role>> userRoles = new HashMap<>();

        jdbcTemplate.query("SELECT u.id, r.role  FROM users u LEFT OUTER JOIN user_role r ON u.id=r.user_id",
                new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                do
                {
                    int id = rs.getInt("id");
                    if (userRoles.containsKey(id))
                    {
                        userRoles.get(id).add(Role.valueOf(rs.getString("role")));
                    } else {
                        Set set = new HashSet<>();
                        try {
                            set.add(Role.valueOf(rs.getString("role")));
                        } catch (NullPointerException e)
                        {
                            set = new HashSet<>();
                        }
                        userRoles.put(id,set);
                    }

                } while (rs.next());
            }
        });
        users.forEach((user->user.setRoles(userRoles.get(user.getId()))));
        return users;
    }
}
