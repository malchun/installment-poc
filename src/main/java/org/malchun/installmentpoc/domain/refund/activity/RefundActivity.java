package org.malchun.installmentpoc.domain.refund.activity;

import io.temporal.activity.ActivityInterface;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityParameters;
import org.malchun.installmentpoc.domain.refund.api.RefundActivityResult;

@ActivityInterface
public interface RefundActivity {
    RefundActivityResult process(RefundActivityParameters parameters);
}