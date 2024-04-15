package org.malchun.installmentpoc.domain.refund.workflow;


import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import org.malchun.installmentpoc.domain.installment.api.RefundWorkflowParameters;
import org.malchun.installmentpoc.domain.installment.api.RefundWorkflowResult;

@WorkflowInterface
public interface RefundWorkflow {

		@WorkflowMethod
		RefundWorkflowResult process(RefundWorkflowParameters parameters);
}