package com.rbkmoney.adapter.atol.service.atol.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.Payload;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@ToString
@Getter
@Slf4j
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponse {

    /**
     * Уникальный идентификатор
     */
    private String uuid;

    /**
     * Тип источника ошибки
     * {@link com.rbkmoney.adapter.atol.service.atol.model.Error Error}
     */
    private com.rbkmoney.adapter.atol.service.atol.model.Error error;

    /**
     * {@link Status Status}
     */
    private String status;

    @JsonProperty("payload")
    private Payload payload;

    /**
     * Идентификатор группы ККТ
     */
    @JsonProperty("group_code")
    private String groupCode;

    /**
     * Наименование сервера
     */
    @JsonProperty("daemon_code")
    private String daemonCode;

    /**
     * Код ККТ
     */
    @JsonProperty("device_code")
    private String deviceCode;

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
     * URL, на который необходимо ответить после обработки документа.
     * Если поле заполнено, то после обработки документа
     * (успешной или не успешной фискализации в ККТ),
     * ответ будет отправлен POST запросом по URL указанному в данном поле.
     * Максимальная длина строки – 256 символов.
     */
    @JsonProperty("callback_url")
    private String callbackUrl;
}
