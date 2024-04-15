package org.malchun.installmentpoc.domain.charge.api;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class ChargeActivityResult {
    Instant chargedAt;
    BigDecimal amount;
    String transactionId;
}
