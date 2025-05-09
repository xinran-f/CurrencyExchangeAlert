package org.sandyf.currencyexchangealert.service;

import org.sandyf.currencyexchangealert.dao.ExchangeAlertDao;
import org.sandyf.currencyexchangealert.dao.UserDao;
import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.sandyf.currencyexchangealert.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ExchangeAlertService {

    private final ExchangeAlertDao exchangeAlertDao;
    private final UserDao userDao;
    private final RestTemplate restTemplate;

    @Value("${currencyfreaks.api.url}")
    private String apiUrl;

    @Value("${currencyfreaks.api.key}")
    private String apiKey;

    @Autowired
    public ExchangeAlertService(ExchangeAlertDao exchangeAlertDao,
                                UserDao userDao,
                                RestTemplate restTemplate) {
        this.exchangeAlertDao = exchangeAlertDao;
        this.userDao          = userDao;
        this.restTemplate     = restTemplate;
    }

    public void createAlert(ExchangeAlert alert) {
        // populate email from users table
        User user = userDao.findById(alert.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "No user with id=" + alert.getUserId()));
        alert.setUserEmail(user.getEmail());
        exchangeAlertDao.save(alert);
    }

    public List<ExchangeAlert> getAllAlerts() {
        return exchangeAlertDao.findAll();
    }

    public void deleteAlert(int id) {
        exchangeAlertDao.deleteById(id);
    }

    @Scheduled(fixedRate = 60000)
    public void checkRatesAndNotify() {
        List<ExchangeAlert> alerts = exchangeAlertDao.findUnnotified();
        for (ExchangeAlert alert : alerts) {
            BigDecimal currentRate = fetchCurrentRate(alert.getBaseCurrency(), alert.getTargetCurrency());
            if (currentRate != null && currentRate.compareTo(alert.getTargetRate()) >= 0) {
                System.out.println("ALERT: " + alert.getUserEmail() +
                        " – Rate " + alert.getBaseCurrency() + "→" +
                        alert.getTargetCurrency() + " = " + currentRate);
                exchangeAlertDao.markAsNotified(alert.getId());
            }
        }
    }

    /**
     * Public method to allow controllers (or tests) to fetch a single rate.
     */
    public BigDecimal getCurrentRate(String baseCurrency, String targetCurrency) {
        String url = String.format("%s?apikey=%s&base_currency=%s&symbols=%s",
                apiUrl, apiKey, baseCurrency, targetCurrency);

        @SuppressWarnings("unchecked")
        Map<String,Object> resp = restTemplate.getForObject(url, Map.class);
        @SuppressWarnings("unchecked")
        Map<String,String> rates = (Map<String,String>) resp.get("rates");

        return new BigDecimal(rates.get(targetCurrency));
    }

    private BigDecimal fetchCurrentRate(String base, String target) {
        String url = String.format("%s?apikey=%s&base_currency=%s&symbols=%s",
                apiUrl, apiKey, base, target);
        @SuppressWarnings("unchecked")
        Map<String,Object> resp = restTemplate.getForObject(url, Map.class);
        @SuppressWarnings("unchecked")
        Map<String,String> rates = (Map<String,String>)resp.get("rates");
        return new BigDecimal(rates.get(target));
    }
}