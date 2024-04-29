package org.malchun.installmentpoc.domain.installment.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.common.RetryOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.malchun.installmentpoc.domain.charge.activity.ChargeActivity;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityParameters;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityResult;
import org.malchun.installmentpoc.domain.installment.activity.CalculatePlannedChargeActivity;
import org.malchun.installmentpoc.domain.installment.activity.NotificationActivity;
import org.malchun.installmentpoc.domain.installment.api.*;
import org.malchun.installmentpoc.domain.payment.FatalForActivityException;
import org.slf4j.Logger;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;

@WorkflowImpl(taskQueues = INSTALLMENT_WORKFLOW_QUEUE)
public class InstallmentWorkflowImpl implements InstallmentWorkflow {
    private final Logger log = Workflow.getLogger(InstallmentWorkflowImpl.class);

    public static final RetryOptions INSTALLMENT_DEFAULT_RETRY_OPTIONS = RetryOptions.newBuilder()
            .setMaximumAttempts(2)
            .setBackoffCoefficient(3)
            .build();

    private InstallmentWorkflowState installmentWorkflowState;
    private boolean exit = false;
    private final ChargeActivity chargeActivity = Workflow.newActivityStub(
            ChargeActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(2)
                            .setBackoffCoefficient(3)
                            .setDoNotRetry(FatalForActivityException.class.getSimpleName())
                            .build())
                    .build());

    private final CalculatePlannedChargeActivity calculateActivity = Workflow.newActivityStub(
            CalculatePlannedChargeActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(2)
                            .setBackoffCoefficient(3)
                            .setDoNotRetry(FatalForActivityException.class.getSimpleName())
                            .build())
                    .build());

    private final NotificationActivity notificationActivity = Workflow.newActivityStub(
            NotificationActivity.class,
            ActivityOptions.newBuilder()
                    .setStartToCloseTimeout(Duration.ofSeconds(2))
                    .setRetryOptions(RetryOptions.newBuilder()
                            .setMaximumAttempts(2)
                            .setBackoffCoefficient(1)
                            .build())
                    .build());

    @Override
    public InstallmentWorkflowState process(InstallmentWorkflowParameters parameters) {
        log.debug("Starting charge process");
        installmentWorkflowState = calculateInitialChargeWorkflowState(parameters);
        List<PlannedCharge> plannedCharges = installmentWorkflowState.getPlannedCharges();
        for (int i = 0; i < plannedCharges.size(); i++) {
            PlannedCharge plannedCharge = plannedCharges.get(i);
            if (!exit) {
                ChargeActivityResult result = processCharge(i, parameters.getCustomerId(), plannedCharge);
                installmentWorkflowState.getCompletedCharges().add(InstallmentCompletedCharge.builder()
                        .chargedAt(result.getChargedAt())
                        .amount(result.getAmount())
                        .transactionId(result.getTransactionId())
                        .build());
            }
        }
        notificationActivity.sendNotification(InstallmentNotification.builder()
                .installmentWorkflowId(Workflow.getInfo().getWorkflowId())
                .customerId(parameters.getCustomerId())
                .build());
        return installmentWorkflowState;
    }

    @Override
    public InstallmentWorkflowState getState() {
        return installmentWorkflowState;
    }

    @Override
    public void cancel() {
        this.exit = true;
    }

    private ChargeActivityResult processCharge(Integer chargeIndex, String customerId, PlannedCharge plannedCharge) {
        log.debug("Process starting");
        var calculateSleepTime = Duration.between(Instant.now(), plannedCharge.getChargeAt());
        var sleepTime = calculateSleepTime.toMillis() > 0 ? calculateSleepTime : Duration.ofMillis(1);
        Workflow.sleep(sleepTime);
        return chargeActivity.process(new ChargeActivityParameters(chargeIndex, customerId, plannedCharge.getAmount()));
    }

    private InstallmentWorkflowState calculateInitialChargeWorkflowState(InstallmentWorkflowParameters parameters) {
        List<PlannedCharge> plannedCharges = calculateActivity.calculatePlannedCharge(parameters.getTotalPrice(), parameters.getDownPaymentAmount(), parameters.getNumberOfPayments(), parameters.getPaymentIntervalInSeconds());
        return new InstallmentWorkflowState(parameters, plannedCharges, new ArrayList<>());
    }
}
