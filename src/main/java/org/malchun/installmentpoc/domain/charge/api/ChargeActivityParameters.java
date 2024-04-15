package org.malchun.installmentpoc.domain.charge.api;

import lombok.Value;

import java.math.BigDecimal;

@Value
public class ChargeActivityParameters {
    Integer chargeIndex;

    String customerId;

    BigDecimal amount;
}
