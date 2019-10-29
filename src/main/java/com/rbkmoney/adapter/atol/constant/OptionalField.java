package com.rbkmoney.adapter.atol.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum OptionalField {

    LOGIN("login"),
    PASS("pass"),
    URL("url"),
    GROUP("group"),
    PAYMENT_METHOD("payment_method"),
    PAYMENT_OBJECT("payment_object"),
    COMPANY("company"),
    COMPANY_ADDRESS("company_address"),
    POLLING_DELAY("polling_delay"),
    MAX_TIME_POLLING("max_time_polling"),
    TIMER_TIMEOUT("timer_timeout"),
    TIMER_ADD_TIME("timer_add_time");

    private final String field;
}
