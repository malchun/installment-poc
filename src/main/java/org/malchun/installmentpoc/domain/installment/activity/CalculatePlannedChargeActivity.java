package org.malchun.installmentpoc.domain.installment.activity;

import io.temporal.activity.ActivityInterface;
import org.malchun.installmentpoc.domain.installment.api.PlannedCharge;

import java.math.BigDecimal;
import java.util.List;

@ActivityInterface
public interface CalculatePlannedChargeActivity {

    List<PlannedCharge> calculatePlannedCharge(BigDecimal totalAmount, int numberOfInstallments, int intervalInSeconds);
}
