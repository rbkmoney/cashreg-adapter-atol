package com.rbkmoney.adapter.atol.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomError {

    SLEEP_TIMEOUT("sleep_timeout", "sleep_timeout"),
    UNKNOWN("unknown_result", "Unknown error!");

    private final String code;
    private final String message;

}
