package org.malchun.installmentpoc.domain.refund.activity;

import io.temporal.activity.Activity;
import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityParameters;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityResult;
import org.malchun.installmentpoc.domain.payment.PaymentService;
import org.malchun.installmentpoc.domain.charge.persistence.ChargeRepository;
import org.malchun.installmentpoc.domain.charge.api.ChargeEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@ActivityImpl(taskQueues = "RefundQueue")
@RequiredArgsConstructor
@Slf4j
public class RefundActivityImpl implements RefundActivity {

    private final ChargeRepository chargeRepository;

    private final PaymentService paymentService;

    @Override
    public RefundActivityResult process(RefundActivityParameters parameters) {
        List<ChargeEntity> charges = chargeRepository.findByParentWorkflowId(parameters.getInstallmentWorkflowId());
        List<String> refundIds = new ArrayList<>();
        charges.forEach(charge -> {
            if (charge.getRefundWorkflowId() == null) {
                String refundId = paymentService.refund(charge.getId());
                var chargeToSave = charge.withRefundWorkflowId(Activity.getExecutionContext().getInfo().getWorkflowId());
                chargeRepository.save(chargeToSave);
                refundIds.add(refundId);
            } else {
                log.warn("Charge {} already refunded in {}", charge.getId(), charge.getRefundWorkflowId());
            }
        });
        return RefundActivityResult.builder().refundIds(refundIds).build();
    }
}
