package org.malchun.installmentpoc.domain.refund.api;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Value
@Builder
@Jacksonized
public class RefundActivityResult {
    List<String> refundIds;
}
