package org.malchun.installmentpoc.domain.installment.api;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;

@Value
@Builder
public class InstallmentCompletedCharge {

		Instant chargedAt;

		BigDecimal amount;

		String transactionId;
}
