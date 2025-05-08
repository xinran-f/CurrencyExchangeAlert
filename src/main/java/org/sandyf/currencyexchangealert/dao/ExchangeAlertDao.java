package org.sandyf.currencyexchangealert.dao;

import org.sandyf.currencyexchangealert.dao.mappers.ExchangeAlertRowMapper;
import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ExchangeAlertDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ExchangeAlertDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(ExchangeAlert alert) {
        String sql = "INSERT INTO exchange_alert (user_email, base_currency, target_currency, target_rate, notified) " +
                "VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                alert.getUserEmail(),
                alert.getBaseCurrency(),
                alert.getTargetCurrency(),
                alert.getTargetRate(),
                alert.isNotified());
    }

    public List<ExchangeAlert> findAll() {
        String sql = "SELECT * FROM exchange_alert";
        return jdbcTemplate.query(sql, new ExchangeAlertRowMapper());
    }

    public List<ExchangeAlert> findUnnotified() {
        String sql = "SELECT * FROM exchange_alert WHERE notified = false";
        return jdbcTemplate.query(sql, new ExchangeAlertRowMapper());
    }

    public void markAsNotified(int id) {
        String sql = "UPDATE exchange_alert SET notified = true WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public void deleteById(int id) {
        String sql = "DELETE FROM exchange_alert WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<ExchangeAlert> findByUserEmail(String userEmail) {
        String sql = "SELECT * FROM exchange_alert WHERE user_email = ?";
        return jdbcTemplate.query(sql, new ExchangeAlertRowMapper(), userEmail);
    }
}