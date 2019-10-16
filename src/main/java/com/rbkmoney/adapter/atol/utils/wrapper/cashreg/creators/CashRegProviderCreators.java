package com.rbkmoney.adapter.atol.utils.wrapper.cashreg.creators;


import com.rbkmoney.damsel.cashreg.CashRegInfo;
import com.rbkmoney.damsel.cashreg.base.Timer;
import com.rbkmoney.damsel.cashreg.provider.*;
import com.rbkmoney.damsel.domain.Failure;

public class CashRegProviderCreators {

    public static Intent createFinishIntentSuccess() {
        return Intent.finish(new FinishIntent(createFinishStatusSuccess()));
    }

    public static Intent createFinishIntentFailure(Failure failure) {
        return Intent.finish(new FinishIntent(createFinishStatusFailure(failure)));
    }

    public static FinishStatus createFinishStatusSuccess() {
        return FinishStatus.success(new Success());
    }

    public static FinishStatus createFinishStatusFailure(Failure failure) {
        return FinishStatus.failure(failure);
    }

    public static Failure createFailure(com.rbkmoney.adapter.atol.service.atol.model.Error error) {
        return createFailure(error.getCode().toString(), error.getText());
    }

    public static Failure createFailure(String code) {
        return createFailure(code, code);
    }

    public static Failure createFailure(String code, String reason) {
        return new Failure().setCode(code).setReason(reason);
    }

    public static CashRegResult createCashRegResult(Intent intent, byte[] state, CashRegInfo cashRegInfo) {
        return new CashRegResult().setIntent(intent).setCashregInfo(cashRegInfo).setState(state);
    }

    public static Intent createIntentWithSleepIntent(Integer timer) {
        return Intent.sleep(createSleepIntent(createTimerTimeout(timer)));
    }

    public static SleepIntent createSleepIntent(Timer timer) {
        return new SleepIntent(timer);
    }

    public static Timer createTimerTimeout(Integer timeout) {
        return Timer.timeout(timeout);
    }

}
