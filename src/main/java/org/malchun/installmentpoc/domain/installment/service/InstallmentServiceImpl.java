package org.malchun.installmentpoc.domain.installment.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.AllArgsConstructor;
import org.malchun.installmentpoc.domain.installment.api.*;
import org.malchun.installmentpoc.domain.installment.workflow.InstallmentWorkflow;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.workflow.InstallmentWorkflowImpl.INSTALLMENT_DEFAULT_RETRY_OPTIONS;

@Service
@AllArgsConstructor
public class InstallmentServiceImpl implements InstallmentService {

    private WorkflowClient workflowClient;

    @Override
    public String initInstallment(InstallmentRequest parameters) {
        String id = UUID.randomUUID().toString();
        InstallmentWorkflow installmentWorkflow = workflowClient.newWorkflowStub(InstallmentWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue(INSTALLMENT_WORKFLOW_QUEUE)
                        .setWorkflowExecutionTimeout(Duration.ofMinutes(30))
                        .setRetryOptions(INSTALLMENT_DEFAULT_RETRY_OPTIONS)
                        .build());
        InstallmentWorkflowParameters installmentWorkflowParameters = InstallmentWorkflowParameters.builder()
                .customerId(parameters.getCustomerId())
                .downPaymentAmount(parameters.getDownPaymentAmount())
                .numberOfPayments(parameters.getNumberOfPayments())
                .paymentIntervalInSeconds(parameters.getPaymentIntervalInSeconds())
                .totalPrice(parameters.getTotalPrice())
                .build();
        CompletableFuture.runAsync(() -> installmentWorkflow.process(installmentWorkflowParameters));
        return id;
    }

    @Override
    public String cancelInstallment(String workflowId) {
        workflowClient.newWorkflowStub(InstallmentWorkflow.class, workflowId).cancel();
        return "canceled";
    }

    @Override
    public InstallmentStatus getInstallmentStatus(String workflowId) {
        InstallmentWorkflowState workflowState = workflowClient.newWorkflowStub(InstallmentWorkflow.class, workflowId).getState();
        InstallmentStatus status = InstallmentStatus.builder()
                .completedCharges(workflowState.getCompletedCharges())
                .plannedCharges(workflowState.getPlannedCharges())
                .build();
        return status;
    }
}
