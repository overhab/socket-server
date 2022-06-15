package edu.school21.sockets.repositories;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;

@Component("messageRepository")
public class MessageRepositoryImpl implements MessageRepository {
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    private UsersRepository usersRepository;

    private static final String SQL_FIND_BY_ID = "select * from message where id = :id";
    private static final String SQL_FIND_ALL = "select * from message";
    private static final String SQL_INSERT = "insert into message(sender, m_text, date) values(:sender, :m_text, :date)";
    private static final String SQL_UPDATE = "update message set sender = :sender, m_text = :m_text, date = :date where id = :id";
    private static final String SQL_DELETE = "delete from message where id = :id";

    private final RowMapper<Message> messageRowMapper = (ResultSet resultSet, int i) -> {
        return new Message(resultSet.getLong("id"),
                usersRepository.findById(resultSet.getLong("sender")),
                resultSet.getString("message"),
                resultSet.getTimestamp("date"));
    };

    @Override
    public Message findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_FIND_BY_ID, new MapSqlParameterSource("id", id), messageRowMapper);
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, messageRowMapper);
    }

    @Override
    public void save(Message entity) {
        jdbcTemplate.update(SQL_INSERT, new MapSqlParameterSource("sender", entity.getSender().getId())
                .addValue("m_text", entity.getText())
                .addValue("date", entity.getDate()));
    }

    @Override
    public void update(Message entity) {
        jdbcTemplate.update(SQL_UPDATE, new MapSqlParameterSource("sender", entity.getSender().getId())
                .addValue("m_text", entity.getText())
                .addValue("date", entity.getDate())
                .addValue("id", entity.getId()));
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE, new MapSqlParameterSource("id", id));
    }
}
