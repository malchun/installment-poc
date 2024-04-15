package org.malchun.installmentpoc.domain.installment.api;


import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class InstallmentResponse {
    @NotNull
    String id;
}
