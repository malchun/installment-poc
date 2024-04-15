package org.malchun.installmentpoc.domain.installment.activity;

import io.temporal.spring.boot.ActivityImpl;
import org.checkerframework.checker.units.qual.A;
import org.malchun.installmentpoc.domain.installment.api.PlannedCharge;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_REFUND_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;

@ActivityImpl(taskQueues = INSTALLMENT_WORKFLOW_QUEUE)
@Service
public class CalculatePlannedChargeActivityImpl implements CalculatePlannedChargeActivity {
    @Override
    public List<PlannedCharge> calculatePlannedCharge(BigDecimal totalAmount, int numberOfCharges, int intervalInSeconds) {
        Duration durationBetweenCharges = Duration.ofSeconds(intervalInSeconds);
        BigDecimal chargeAmount = totalAmount.divide(BigDecimal.valueOf(numberOfCharges), RoundingMode.HALF_UP);;
        List<PlannedCharge> plannedCharges = new ArrayList<>();
        for (int i = 1; i <= numberOfCharges; i++) {
            plannedCharges.add(new PlannedCharge(Instant.now().plus(durationBetweenCharges.multipliedBy(i)), chargeAmount));
        }
        return plannedCharges;
    }
}
