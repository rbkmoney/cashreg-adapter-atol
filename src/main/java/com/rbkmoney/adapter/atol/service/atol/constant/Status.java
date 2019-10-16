package com.rbkmoney.adapter.atol.service.atol.constant;

import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Статус. Возможные значения:
 * - «done» – готово;
 * - «fail» – ошибка;
 * - «wait» – ожидание.
 */
@Getter
@RequiredArgsConstructor
public enum Status {

    /**
     * готово
     */
    DONE("done"),

    /**
     * ошибка
     */
    FAIL("fail"),

    /**
     * ожидание
     */
    WAIT("wait");

    private final String value;


    public static boolean isWait(CommonResponse response) {
        return Status.WAIT.getValue().equalsIgnoreCase(response.getStatus());
    }

    public static boolean isDone(CommonResponse response) {
        return Status.DONE.getValue().equalsIgnoreCase(response.getStatus());
    }

    public static boolean isFail(CommonResponse response) {
        return Status.FAIL.getValue().equalsIgnoreCase(response.getStatus());
    }

}
