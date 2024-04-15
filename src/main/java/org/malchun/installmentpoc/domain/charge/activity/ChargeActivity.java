package org.malchun.installmentpoc.domain.charge.activity;

import io.temporal.activity.ActivityInterface;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityParameters;
import org.malchun.installmentpoc.domain.charge.api.ChargeActivityResult;

@ActivityInterface
public interface ChargeActivity {
    ChargeActivityResult process(ChargeActivityParameters parameters);
}