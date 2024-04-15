package org.malchun.installmentpoc.domain.installment.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import org.malchun.installmentpoc.domain.installment.api.InstallmentRequest;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowParameters;
import org.malchun.installmentpoc.domain.installment.workflow.InstallmentWithRefundWorkflow;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_REFUND_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.workflow.InstallmentWorkflowImpl.INSTALLMENT_DEFAULT_RETRY_OPTIONS;

@Service
@RequiredArgsConstructor
public class InstallmentWithRefundServiceImpl implements InstallmentWithRefundService {

    private final WorkflowClient workflowClient;

    @Override
    public String initInstallment(InstallmentRequest parameters) {
        String id = UUID.randomUUID().toString();
        InstallmentWithRefundWorkflow installmentWithRefundWorkflow = workflowClient.newWorkflowStub(InstallmentWithRefundWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue(INSTALLMENT_REFUND_WORKFLOW_QUEUE)
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
        CompletableFuture.runAsync(() -> installmentWithRefundWorkflow.process(installmentWorkflowParameters));
        return id;
    }
}
