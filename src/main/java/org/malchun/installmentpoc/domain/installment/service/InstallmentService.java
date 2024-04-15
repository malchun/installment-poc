package org.malchun.installmentpoc.domain.installment.service;

import org.malchun.installmentpoc.domain.installment.api.InstallmentStatus;
import org.malchun.installmentpoc.domain.installment.api.InstallmentWorkflowState;
import org.malchun.installmentpoc.domain.installment.api.InstallmentRequest;

public interface InstallmentService {

		/**
		 * Runs installment WF.
		 *
		 * @return workflowId
		 */
		String initInstallment(InstallmentRequest parameters);

		String cancelInstallment(String workflowId);

		InstallmentStatus getInstallmentStatus(String installmentId);
}
