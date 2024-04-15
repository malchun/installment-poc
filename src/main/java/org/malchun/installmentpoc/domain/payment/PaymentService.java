package org.malchun.installmentpoc.domain.payment;


import java.math.BigDecimal;

public interface PaymentService {

    /**
     * @return transactionId
     */
    String charge(String referenceId, String customerId, BigDecimal amount);

    String refund(String transactionId);
}