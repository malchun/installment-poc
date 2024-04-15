package org.malchun.installmentpoc.domain.installment.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.malchun.installmentpoc.domain.installment.api.InstallmentResponse;
import org.malchun.installmentpoc.domain.installment.api.InstallmentRequest;
import org.malchun.installmentpoc.domain.installment.api.InstallmentStatus;
import org.malchun.installmentpoc.domain.installment.service.InstallmentService;
import org.malchun.installmentpoc.domain.installment.service.InstallmentWithRefundService;
import org.malchun.installmentpoc.domain.refund.service.RefundService;
import org.springframework.web.bind.annotation.*;

@RestController("/installments")
@Slf4j
@RequiredArgsConstructor
public class InstallmentController {

    private final InstallmentService installmentService;

    private final RefundService refundService;

    private final InstallmentWithRefundService installmentWithRefundService;

    @Operation(summary = "Get an installment by id")
    @GetMapping("/{installmentWorkflowId}")
    public InstallmentStatus getChargeStatus(@PathVariable(name = "installmentWorkflowId") String installmentWorkflowId) {
        return installmentService.getInstallmentStatus(installmentWorkflowId);
    }

    @Operation(summary = "Create installment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the book",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = InstallmentResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid parameters supplied",
                    content = @Content)})
    @PostMapping("/")
    public InstallmentResponse createInstallment(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Charge workflow parameters",
                    content = @Content(schema = @Schema(implementation = InstallmentRequest.class)))
            @RequestBody InstallmentRequest input) {
        String id = installmentService.initInstallment(input);
        return InstallmentResponse.builder().id(id).build();
    }

    @Operation(summary = "Refund installment by id")
    @PostMapping("/{installmentWorkflowId}/refund")
    public String refund(@PathVariable(name = "installmentWorkflowId") String installmentWorkflowId) {
        return refundService.initRefund(installmentWorkflowId);
    }

    @Operation(summary = "Cancel installment by id")
    @PostMapping("/{installmentWorkflowId}/cancel")
    public String cancel(@PathVariable(name = "installmentWorkflowId") String installmentWorkflowId) {
        return installmentService.cancelInstallment(installmentWorkflowId);
    }

    @Operation(summary = "Initiate an installment with refund on error")
    @PostMapping("/withRefund/")
    public String withRefund(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Charge workflow parameters",
                    content = @Content(schema = @Schema(implementation = InstallmentRequest.class)))
            @RequestBody InstallmentRequest input) {
        return installmentWithRefundService.initInstallment(input);
    }
}
