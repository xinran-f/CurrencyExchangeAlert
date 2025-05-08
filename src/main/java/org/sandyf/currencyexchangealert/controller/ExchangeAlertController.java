package org.sandyf.currencyexchangealert.controller;

import org.sandyf.currencyexchangealert.model.ExchangeAlert;
import org.sandyf.currencyexchangealert.service.ExchangeAlertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class ExchangeAlertController {
    @Autowired
    private ExchangeAlertService alertService;

    @PostMapping
    public ResponseEntity<String> createAlert(@RequestBody ExchangeAlert alert) {
        alertService.createAlert(alert);
        return ResponseEntity.ok("Alert created");
    }

    @GetMapping
    public List<ExchangeAlert> getAllAlerts() {
        return alertService.getAllAlerts();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAlert(@PathVariable int id) {
        alertService.deleteAlert(id);
        return ResponseEntity.ok("Alert deleted");
    }
}