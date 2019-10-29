package com.rbkmoney.adapter.atol.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Step {
    CREATE,
    CHECK_STATUS,
}
