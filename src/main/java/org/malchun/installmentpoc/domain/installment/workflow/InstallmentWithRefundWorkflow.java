package org.malchun.installmentpoc.domain.installment.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.malchun.installmentpoc.domain.installment.api.InstallmentRequest;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowParameters;

@WorkflowInterface
public interface InstallmentWithRefundWorkflow {

    @WorkflowMethod
    String process(InstallmentWorkflowParameters parameters);

}
