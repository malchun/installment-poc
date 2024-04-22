package org.malchun.installmentpoc.domain.installment.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class RefundWorkflowResult {
    List<String> refundsIds;
}
