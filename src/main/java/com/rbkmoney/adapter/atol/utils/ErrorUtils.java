package com.rbkmoney.adapter.atol.utils;

import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorUtils {

    public static boolean hasError(CommonResponse response) {
        return response.getError() != null && Status.FAIL.getValue().equals(response.getStatus());
    }

}
