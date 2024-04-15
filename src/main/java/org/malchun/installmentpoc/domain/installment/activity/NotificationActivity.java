package org.malchun.installmentpoc.domain.installment.activity;

import io.temporal.activity.ActivityInterface;
import org.malchun.installmentpoc.domain.installment.api.InstallmentNotification;

@ActivityInterface
public interface NotificationActivity {

    void sendNotification(InstallmentNotification notification);
}
