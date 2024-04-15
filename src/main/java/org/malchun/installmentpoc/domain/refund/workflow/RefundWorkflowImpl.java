package org.malchun.installmentpoc.domain.refund.workflow;

import io.temporal.activity.ActivityOptions;
import io.temporal.spring.boot.WorkflowImpl;
import io.temporal.workflow.Workflow;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityParameters;
import org.malchun.installmentpoc.domain.installment.api.RefundWorkflowParameters;
import org.malchun.installmentpoc.domain.installment.api.RefundWorkflowResult;
import org.malchun.installmentpoc.domain.refund.activity.RefundActivity;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityResult;
import org.slf4j.Logger;

import java.time.Duration;

@WorkflowImpl(taskQueues = "RefundQueue")
public class RefundWorkflowImpl implements RefundWorkflow {

    private static final Logger log = Workflow.getLogger(RefundWorkflowImpl.class);

    private final RefundActivity refundActivity = Workflow.newActivityStub(
            RefundActivity.class,
            ActivityOptions.newBuilder().setStartToCloseTimeout(Duration.ofSeconds(2)).build());

    /**
     * Defines refund process.
     * Steps:
     * - Check that chargeWorkflow is completed (if not - fail)
     * - In RefundActivity
     *   - Get all charges by installmentWorkflowId
     *   - Refund all charges
     * @param parameters - contains installmentWorkflowId
     * @return reserved
     */
    @Override
    public RefundWorkflowResult process(RefundWorkflowParameters parameters) {
        log.debug("Starting charge process");
        RefundActivityResult result = refundActivity.process(new RefundActivityParameters(parameters.getInstallmentWorkflowId()));
        return RefundWorkflowResult.builder().refundsIds(result.getRefundIds()).build();
    }
}
