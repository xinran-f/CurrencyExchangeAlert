package org.sandyf.currencyexchangealert.controller;

import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.sandyf.currencyexchangealert.service.ExchangeAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExchangeAlertController {

    private final ExchangeAlertService alertService;

    @Autowired
    public ExchangeAlertController(ExchangeAlertService alertService) {
        this.alertService = alertService;
    }

    /**
     * POST /api/alerts
     * Create a new exchange alert for a given userId.
     * The service will look up the user’s email and insert the alert.
     */
    @PostMapping("/alerts")
    public ResponseEntity<Void> createAlert(@RequestBody ExchangeAlert alert) {
        alertService.createAlert(alert);
        // Return 201 Created with Location header pointing to the new resource
        return ResponseEntity
                .created(URI.create("/api/alerts/" + alert.getId()))
                .build();
    }

    /**
     * GET /api/alerts
     * List all exchange alerts.
     */
    @GetMapping("/alerts")
    public ResponseEntity<List<ExchangeAlert>> getAllAlerts() {
        List<ExchangeAlert> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(alerts);
    }

    /**
     * DELETE /api/alerts/{id}
     * Delete an alert by its id.
     */
    @DeleteMapping("/alerts/{id}")
    public ResponseEntity<Void> deleteAlert(@PathVariable int id) {
        alertService.deleteAlert(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/rate?base=USD&target=EUR
     * Proxy call to CurrencyFreaks API returning the current rate.
     */
    @GetMapping("/rate")
    public ResponseEntity<BigDecimal> getRate(
            @RequestParam("base") String baseCurrency,
            @RequestParam("target") String targetCurrency) {

        BigDecimal rate = alertService.getCurrentRate(baseCurrency, targetCurrency);
        return ResponseEntity.ok(rate);
    }
}