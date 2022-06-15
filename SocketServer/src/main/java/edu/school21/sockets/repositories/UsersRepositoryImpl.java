package edu.school21.sockets.repositories;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;

@Component("usersRepository")
public class UsersRepositoryImpl implements UsersRepository {

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private static final String SQL_FIND_BY_ID = "select * from s_user where id = :id";
    private static final String SQL_FIND_BY_USERNAME = "select * from s_user where username = :username";
    private static final String SQL_FIND_ALL = "select * from s_user";
    private static final String SQL_INSERT = "insert into s_user(username, password) values(:username, :password)";
    private static final String SQL_UPDATE = "update s_user set username = :username, password = :password where id = :id";
    private static final String SQL_DELETE = "delete from s_user where id = :id";

    private final RowMapper<User> userRowMapper = (ResultSet resultSet, int i) -> {
        return new User(resultSet.getLong("id"),
                resultSet.getString("username"),
                resultSet.getString("password"));
    };

    @Override
    public User findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new MapSqlParameterSource("id", id), userRowMapper);
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, userRowMapper);
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update(SQL_INSERT, new BeanPropertySqlParameterSource(entity));
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update(SQL_UPDATE, new BeanPropertySqlParameterSource(entity));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE, new MapSqlParameterSource("id", id));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = jdbcTemplate.queryForObject(SQL_FIND_BY_USERNAME, new MapSqlParameterSource("username", username), userRowMapper);
            return Optional.ofNullable(user);
        } catch (DataAccessException exception) {
            return Optional.empty();
        }
    }
}
