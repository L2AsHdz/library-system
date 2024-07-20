package com.ayd2.librarysystem.loan.task;

import com.ayd2.librarysystem.loan.service.RatesService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledCalculation {

    private final RatesService ratesService;

    @Scheduled(cron = "0 0 0 * * *")
    public void calculateRates() {
        ratesService.priceCalculation();
        ratesService.arrearCalculation();
    }
}
