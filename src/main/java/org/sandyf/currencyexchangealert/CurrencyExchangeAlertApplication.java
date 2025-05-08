package org.sandyf.currencyexchangealert;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CurrencyExchangeAlertApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyExchangeAlertApplication.class, args);
    }

}
