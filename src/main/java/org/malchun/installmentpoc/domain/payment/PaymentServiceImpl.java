package org.malchun.installmentpoc.domain.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final Double faultChance;
    private final Double fatalFaultChance;

    public PaymentServiceImpl(@Value("${installmentpoc.faultchance:0.0}")Double faultChance,
                              @Value("${installmentpoc.fatalfaultchance:0.0}")Double fatalFaultChance) {
        this.faultChance = faultChance;
        this.fatalFaultChance = fatalFaultChance;
    }

    @Override
    public String charge(String referenceId, String customerId, BigDecimal amount) {
        log.info("Charging {} from customer {} with reference {}", amount, customerId, referenceId);
        if (Math.random() < faultChance) {
            throw new RuntimeException("Payment service is faulty");
        }
        if (Math.random() < fatalFaultChance) {
            throw new FatalForActivityException("Payment service fatally failed");
        }
        return Instant.now().toString();
    }

    @Override
    public String refund(String transactionId) {
        log.info("Refunding transaction {}", transactionId);
        if (Math.random() < faultChance) {
            throw new RuntimeException("Payment service is faulty");
        }
        return UUID.randomUUID().toString();
    }
}
