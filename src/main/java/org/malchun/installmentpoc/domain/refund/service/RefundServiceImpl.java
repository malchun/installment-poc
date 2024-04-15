package org.malchun.installmentpoc.domain.refund.service;

import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowOptions;
import lombok.AllArgsConstructor;
import org.malchun.installmentpoc.domain.installment.api.*;
import org.malchun.installmentpoc.domain.installment.workflow.InstallmentWorkflow;
import org.malchun.installmentpoc.domain.refund.workflow.RefundWorkflow;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.workflow.InstallmentWorkflowImpl.INSTALLMENT_DEFAULT_RETRY_OPTIONS;

@Service
@AllArgsConstructor
public class RefundServiceImpl implements RefundService {

    private WorkflowClient workflowClient;

    @Override
    public String initRefund(String workflowId) {
        String id = UUID.randomUUID().toString();
        RefundWorkflow refundWorkflow = workflowClient.newWorkflowStub(RefundWorkflow.class,
                WorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue("RefundQueue")
                        .setWorkflowExecutionTimeout(Duration.ofMinutes(30))
                        .build());
        RefundWorkflowParameters refundWorkflowParameters = RefundWorkflowParameters.builder()
                .installmentWorkflowId(workflowId)
                .build();
        CompletableFuture.runAsync(() -> refundWorkflow.process(refundWorkflowParameters));
        return id;
    }
}
