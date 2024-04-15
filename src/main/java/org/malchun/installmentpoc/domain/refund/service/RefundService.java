package org.malchun.installmentpoc.domain.refund.service;

public interface RefundService {

		/**
		 * Runs refund WF.
		 *
		 * @param workflowId installmentWorkflowId
		 *
		 * @return refundWorkflowId
		 */
		String initRefund(String workflowId);

}
