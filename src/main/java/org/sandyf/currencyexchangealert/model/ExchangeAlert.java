package org.sandyf.currencyexchangealert.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExchangeAlert {
    private int id;
    private int userId;
    private String userEmail;
    private String baseCurrency;
    private String targetCurrency;
    private BigDecimal targetRate;
    private boolean notified;
}
