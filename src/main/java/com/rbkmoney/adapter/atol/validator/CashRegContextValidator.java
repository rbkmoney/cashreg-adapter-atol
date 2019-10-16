package com.rbkmoney.adapter.atol.validator;

import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CashRegContextValidator extends ValidatorImpl<CashRegContext> {

    @Override
    public void validate(CashRegContext context) {
        Map<String, String> options = context.getOptions();
        validateRequredFields(options);
    }
}
