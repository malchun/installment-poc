package org.malchun.installmentpoc.domain.charge.activity;

import io.temporal.activity.Activity;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityParameters;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityResult;
import org.malchun.installmentpoc.domain.payment.PaymentService;
import org.malchun.installmentpoc.domain.charge.persistence.ChargeRepository;
import org.malchun.installmentpoc.domain.charge.api.ChargeEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
@Slf4j
@ActivityImpl(taskQueues = {"InstallmentQueue", "InstallmentRefundQueue"})
@RequiredArgsConstructor
public class ChargeActivityImpl implements ChargeActivity {

    private final ChargeRepository chargeRepository;

    private final PaymentService paymentService;

    @Override
    public ChargeActivityResult process(ChargeActivityParameters parameters) {
        String workflowId = Activity.getExecutionContext().getInfo().getWorkflowId();
        String referenceId = "%s#%s".formatted(workflowId, parameters.getChargeIndex());
        String transactionId = paymentService.charge(referenceId, parameters.getCustomerId(), parameters.getAmount());
        Instant chargedAt = Instant.now();
        chargeRepository.save(ChargeEntity.builder()
                .id(referenceId)
                .parentWorkflowId(workflowId)
                .amount(parameters.getAmount())
                .transactionId(transactionId)
                .chargeIndex(parameters.getChargeIndex())
                .build()
        );
        return ChargeActivityResult.builder()
                .chargedAt(chargedAt)
                .amount(parameters.getAmount())
                .transactionId(transactionId)
                .build();
    }
}
