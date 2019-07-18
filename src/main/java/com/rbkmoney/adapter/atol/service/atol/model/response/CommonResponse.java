package com.rbkmoney.adapter.atol.service.atol.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponse {

    /**
     * Уникальный идентификатор
     */
    private String uuid;

    /**
     * Дата и время документа внешней системы в
     * формате: «dd.mm.yyyy HH:MM:SS»
     * - dd – День месяца. Формат DD.
     * Возможные значения от «01» до «31».
     * - mm – Месяц. Формат MM.
     * Возможные значения от «01» до «12».
     * - yyyy – Год. Формат YYYY.
     * Допустимое количество символов – четыре.
     * - HH – Часы. Формат HH. Возможные значения от «00» до «24».
     * - MM – Минуты. Формат MM.Возможные значения от «00» до «59».
     * - SS – Секунды. Формат SS. Возможные значения от «00» до «59»
     */
    private String timestamp;

    /**
     * Тип источника ошибки
     * {@link com.rbkmoney.adapter.atol.service.atol.model.Error Error}
     */
    private com.rbkmoney.adapter.atol.service.atol.model.Error error;

    /**
     * {@link Status Status}
     */
    private String status;

}
