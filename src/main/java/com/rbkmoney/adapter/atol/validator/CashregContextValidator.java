package com.rbkmoney.adapter.atol.validator;

import com.rbkmoney.adapter.cashreg.spring.boot.starter.constant.OptionalField;
import com.rbkmoney.adapter.common.Validator;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CashregContextValidator implements Validator<CashregContext> {

    @Override
    public void validate(CashregContext context) {
        Map<String, String> options = context.getOptions();
        validateRequredFields(options);
    }

    private void validateRequredFields(Map<String, String> options) {
        Objects.requireNonNull(options.get(OptionalField.LOGIN.getField()), "Option 'login' must be set");
        Objects.requireNonNull(options.get(OptionalField.PASS.getField()), "Option 'pass' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_METHOD.getField()), "Option 'payment_method' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_OBJECT.getField()), "Option 'payment_object' must be set");
        Objects.requireNonNull(options.get(OptionalField.GROUP.getField()), "Option 'group' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY_NAME.getField()), "Option 'company_name' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY_ADDRESS.getField()), "Option 'company_address' must be set");
    }
}
