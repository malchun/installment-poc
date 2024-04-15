package org.malchun.installmentpoc.domain.installment.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;

@Value
@Builder
@Jacksonized
public class InstallmentWorkflowParameters {
    BigDecimal totalPrice;

    BigDecimal downPaymentAmount;

    Integer numberOfPayments;

    Integer paymentIntervalInSeconds;

    String customerId;
}