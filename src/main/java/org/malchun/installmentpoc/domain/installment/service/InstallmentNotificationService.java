package org.malchun.installmentpoc.domain.installment.service;

import org.malchun.installmentpoc.domain.installment.api.InstallmentNotification;

public interface InstallmentNotificationService {
    void sendInstallmentNotification(InstallmentNotification notification);
}
