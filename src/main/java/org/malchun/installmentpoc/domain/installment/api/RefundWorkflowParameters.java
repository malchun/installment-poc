package org.malchun.installmentpoc.domain.installment.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class RefundWorkflowParameters {

    String installmentWorkflowId;
}
