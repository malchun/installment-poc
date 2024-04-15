package org.malchun.installmentpoc.domain.charge.api;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "charge")
@Table(name = "charge")
public class ChargeEntity {
    @Id
    String id;
    @NotNull
    BigDecimal amount;
    @NotNull
    String parentWorkflowId;
    @NotNull
    Integer chargeIndex;
    /**
     * The transactionId is the unique identifier for the charge from payment system.
     */
    @NotNull
    String transactionId;
    @With
    @Nullable
    String refundWorkflowId;
}
