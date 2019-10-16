package com.rbkmoney.adapter.atol.validator;

import com.rbkmoney.adapter.atol.constant.OptionalField;

import java.util.Map;
import java.util.Objects;

public abstract class ValidatorImpl<T> implements Validator<T> {

    public void validateRequredFields(Map<String, String> options) {
        Objects.requireNonNull(options.get(OptionalField.LOGIN.name()), "Option 'login' must be set");
        Objects.requireNonNull(options.get(OptionalField.PASS.name()), "Option 'pass' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_METHOD.name()), "Option 'payment_method' must be set");
        Objects.requireNonNull(options.get(OptionalField.PAYMENT_OBJECT.name()), "Option 'payment_object' must be set");
        Objects.requireNonNull(options.get(OptionalField.GROUP.name()), "Option 'group' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY.name()), "Option 'company' must be set");
        Objects.requireNonNull(options.get(OptionalField.COMPANY_ADDRESS.name()), "Option 'company_address' must be set");
    }

}
