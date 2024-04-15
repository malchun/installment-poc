package org.malchun.installmentpoc.domain.installment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.malchun.installmentpoc.domain.installment.api.InstallmentNotification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InstallmentNotificationServiceImpl implements InstallmentNotificationService {
    private final Double faultChance;

    public InstallmentNotificationServiceImpl(@Value("${installmentpoc.faultchance:0.0}")Double faultChance) {
        this.faultChance = faultChance;
    }

    @Override
    public void sendInstallmentNotification(InstallmentNotification notification) {
        if (Math.random() < faultChance) {
            throw new RuntimeException("Notification service is faulty");
        }
        log.info("Sending notification: {} to customer {}", notification.getInstallmentWorkflowId(), notification.getCustomerId());
    }
}
