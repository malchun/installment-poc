package org.malchun.installmentpoc.domain.installment.workflow;

import io.temporal.workflow.QueryMethod;
import io.temporal.workflow.UpdateMethod;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowParameters;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowState;

/**
 * This Workflow does not handle refunds.
 */
@WorkflowInterface
public interface InstallmentWorkflow {
    @WorkflowMethod
    InstallmentWorkflowState process(InstallmentWorkflowParameters parameters);

    @QueryMethod
    InstallmentWorkflowState getState();

    /**
     * Stops further charges, do nothing about already happened.
     */
    @UpdateMethod
    void cancel();
}
