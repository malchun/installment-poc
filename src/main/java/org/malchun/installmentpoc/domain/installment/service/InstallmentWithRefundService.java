package org.malchun.installmentpoc.domain.installment.service;

import org.malchun.installmentpoc.domain.installment.api.InstallmentRequest;

public interface InstallmentWithRefundService {

    /**
     * Runs installment with refund WF.
     *
     * @return workflowId
     */
    String initInstallment(InstallmentRequest parameters);

}
