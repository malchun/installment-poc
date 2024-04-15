package org.malchun.installmentpoc.domain.installment.activity;

import io.temporal.spring.boot.ActivityImpl;
import lombok.RequiredArgsConstructor;
import org.malchun.installmentpoc.domain.installment.api.InstallmentNotification;
import org.malchun.installmentpoc.domain.installment.service.InstallmentNotificationService;
import org.springframework.stereotype.Service;

import static org.malchun.installmentpoc.domain.installment.utility.InstallmentConstants.INSTALLMENT_WORKFLOW_QUEUE;

@Service
@ActivityImpl(taskQueues = INSTALLMENT_WORKFLOW_QUEUE)
@RequiredArgsConstructor
public class NotificationActivityImpl implements NotificationActivity {

    private final InstallmentNotificationService notificationService;

    @Override
    public void sendNotification(InstallmentNotification notification) {
        notificationService.sendInstallmentNotification(notification);
    }
}
