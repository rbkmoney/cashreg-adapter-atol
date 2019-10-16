package com.rbkmoney.adapter.atol.flow;

import com.rbkmoney.adapter.atol.constant.TargetType;
import com.rbkmoney.adapter.atol.exception.UnknownTargetTypeException;
import org.springframework.stereotype.Component;

@Component
public class TargetTypeResolver {

    public TargetType resolve(com.rbkmoney.damsel.cashreg.type.Type type) {
        if (type != null) {
            if (type.isSetCredit()) {
                return TargetType.CREDIT;
            } else if (type.isSetDebit()) {
                return TargetType.DEBIT;
            } else if (type.isSetRefundDebit()) {
                return TargetType.REFUND_DEBIT;
            } else if (type.isSetRefundCredit()) {
                return TargetType.REFUND_CREDIT;
            }
        }
        throw new UnknownTargetTypeException();
    }


}
