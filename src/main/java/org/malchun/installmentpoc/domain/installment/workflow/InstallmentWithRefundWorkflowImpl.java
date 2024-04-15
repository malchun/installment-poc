package org.malchun.installmentpoc.domain.installment.workflow;

import io.temporal.client.WorkflowClient;
import io.temporal.common.RetryOptions;
import io.temporal.failure.ApplicationFailure;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.ChildWorkflowOptions;
import io.temporal.workflow.Workflow;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowParameters;
import org.malchun.installmentpoc.domain.installment.api.RefundWorkflowParameters;
import org.malchun.installmentpoc.domain.refund.workflow.RefundWorkflow;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_REFUND_WORKFLOW_QUEUE;
import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;

@WorkflowImpl(taskQueues = INSTALLMENT_REFUND_WORKFLOW_QUEUE)
public class InstallmentWithRefundWorkflowImpl implements InstallmentWithRefundWorkflow {

    private static final Logger log = Workflow.getLogger(InstallmentWithRefundWorkflowImpl.class);

    private static final RetryOptions ZERO_TOLERANCE_RETRY_POLICY = RetryOptions.newBuilder()
            .setMaximumAttempts(1)
            .setBackoffCoefficient(3)
            .build();

    @Override
    public String process(InstallmentWorkflowParameters parameters) {
        String id = UUID.randomUUID().toString();
        String resultId = id;
        try {
            initInstallment(id, parameters);
        } catch (RuntimeException e) {
            log.info("Installment failed, refunding");
            resultId = refundInstallment(id);
        }
        return resultId;
    }

    private void initInstallment(String id, InstallmentWorkflowParameters parameters) {
        InstallmentWorkflow installmentWorkflow = Workflow.newChildWorkflowStub(InstallmentWorkflow.class,
                ChildWorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue(INSTALLMENT_WORKFLOW_QUEUE)
                        .setWorkflowExecutionTimeout(Duration.ofMinutes(30))
                        .setRetryOptions(ZERO_TOLERANCE_RETRY_POLICY)
                        .build());
        installmentWorkflow.process(parameters);
    }

    private String refundInstallment(String installmentId) {
        String id = UUID.randomUUID().toString();
        RefundWorkflow refundWorkflow = Workflow.newChildWorkflowStub(RefundWorkflow.class,
                ChildWorkflowOptions.newBuilder()
                        .setWorkflowId(id)
                        .setTaskQueue("RefundQueue")
                        .setWorkflowExecutionTimeout(Duration.ofMinutes(30))
                        .build());
        RefundWorkflowParameters refundWorkflowParameters = RefundWorkflowParameters.builder()
                .installmentWorkflowId(installmentId)
                .build();
        refundWorkflow.process(refundWorkflowParameters);
        return id;
    }

}
