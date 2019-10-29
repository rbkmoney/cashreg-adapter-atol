package com.rbkmoney.adapter.atol.validator;

import com.rbkmoney.adapter.atol.constant.OptionalField;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CashRegContextValidator implements Validator<CashRegContext> {

    @Override
    public void validate(CashRegContext context) {
        Map<String, String> options = context.getOptions();
        validateRequredFields(options);
    }

    private void validateRequredFields(Map<String, String> options) {
        Objects.requireNonNull(options.get(OptionalField.LOGIN.name()), "Option 'login' must be set");
        Objects.requireNonNull(options.get(OptionalField.PASS.name()), "Option 'pass' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_METHOD.name()), "Option 'payment_method' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_OBJECT.name()), "Option 'payment_object' must be set");
        Objects.requireNonNull(options.get(OptionalField.GROUP.name()), "Option 'group' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY.name()), "Option 'company' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY_ADDRESS.name()), "Option 'company_address' must be set");
    }
}
