package com.rbkmoney.adapter.atol.service.atol.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип источника ошибки. Возможные значения:
 */
@Getter
@RequiredArgsConstructor
public enum ErrorType {

    /**
     * системная ошибка.
     */
    SYSTEM("system"),

    /**
     * ошибка при работе с ККТ
     */
    DRIVER("driver"),

    /**
     * Превышено время ожидания. Время ожидания задается в системе.
     * На данный момент установлено 300 сек
     */
    TIMEOUT("timeout");

    private final String value;

}
