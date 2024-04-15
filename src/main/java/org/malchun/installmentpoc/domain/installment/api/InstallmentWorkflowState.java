package org.malchun.installmentpoc.domain.installment.api;

import lombok.Value;

import java.util.List;

@Value
public class InstallmentWorkflowState {

    InstallmentWorkflowParameters parameters;

    /**
     * Charge id is {workflowId}#{index}
     */
    List<PlannedCharge> plannedCharges;

    List<InstallmentCompletedCharge> completedCharges;

}
