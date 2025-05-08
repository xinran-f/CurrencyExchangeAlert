package org.sandyf.currencyexchangealert.service;

import org.sandyf.currencyexchangealert.dao.ExchangeAlertDao;
import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ExchangeAlertService {

    private final ExchangeAlertDao exchangeAlertDao;
    private final RestTemplate restTemplate;

    private static final String API_URL = "https://api.currencyfreaks.com/latest?apikey=YOUR_API_KEY";

    @Autowired
    public ExchangeAlertService(ExchangeAlertDao exchangeAlertDao, RestTemplate restTemplate) {
        this.exchangeAlertDao = exchangeAlertDao;
        this.restTemplate = restTemplate;
    }

    public void createAlert(ExchangeAlert alert) {
        exchangeAlertDao.save(alert);
    }

    public List<ExchangeAlert> getAllAlerts() {
        return exchangeAlertDao.findAll();
    }

    public void deleteAlert(int id) {
        exchangeAlertDao.deleteById(id);
    }

    // Check rates periodically and notify users
    @Scheduled(fixedRate = 60000) // runs every 60 seconds
    public void checkRatesAndNotify() {
        List<ExchangeAlert> alerts = exchangeAlertDao.findUnnotified();

        for (ExchangeAlert alert : alerts) {
            BigDecimal currentRate = fetchCurrentRate(alert.getBaseCurrency(), alert.getTargetCurrency());
            if (currentRate != null && currentRate.compareTo(alert.getTargetRate()) >= 0) {
                // Notify user (simple console log, could send email instead)
                System.out.println("ALERT: " + alert.getUserEmail() +
                        " - Rate for " + alert.getBaseCurrency() + " to " + alert.getTargetCurrency() +
                        " reached target: " + currentRate);

                // Mark as notified in DB
                exchangeAlertDao.markAsNotified(alert.getId());
            }
        }
    }

    // Helper method to fetch current rate from API
    private BigDecimal fetchCurrentRate(String baseCurrency, String targetCurrency) {
        String url = API_URL + "&base_currency=" + baseCurrency + "&symbols=" + targetCurrency;

        try {
            var response = restTemplate.getForObject(url, java.util.Map.class);
            java.util.Map<String, String> rates = (java.util.Map<String, String>) response.get("rates");
            return new BigDecimal(rates.get(targetCurrency));
        } catch (Exception e) {
            System.err.println("Error fetching rate: " + e.getMessage());
            return null;
        }
    }
}