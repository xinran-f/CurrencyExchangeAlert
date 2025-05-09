package org.sandyf.currencyexchangealert.dao.mappers;

import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ExchangeAlertRowMapper implements RowMapper<ExchangeAlert> {

    @Override
    public ExchangeAlert mapRow(ResultSet rs, int rowNum) throws SQLException {
        ExchangeAlert alert = new ExchangeAlert();
        alert.setId(rs.getInt("id"));
        alert.setUserId(rs.getInt("user_id"));
        alert.setUserEmail(rs.getString("user_email"));
        alert.setBaseCurrency(rs.getString("base_currency"));
        alert.setTargetCurrency(rs.getString("target_currency"));
        alert.setTargetRate(rs.getBigDecimal("target_rate"));
        alert.setNotified(rs.getBoolean("notified"));
        return alert;
    }
}