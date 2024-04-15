package org.malchun.installmentpoc.domain.installment.api;

import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
public class PlannedCharge {
    Instant chargeAt;

    BigDecimal amount;
}
