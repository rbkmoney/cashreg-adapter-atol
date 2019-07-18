package com.rbkmoney.adapter.atol.service.atol.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.rbkmoney.adapter.atol.service.atol.model.Receipt;
import com.rbkmoney.adapter.atol.service.atol.model.Service;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonRequest {

    /**
     * Идентификатор документа внешней системы, уникальный среди всех документов,
     * отправляемых в данную группу ККТ. Тип данных – строка.
     * Предназначен для защиты от потери документов при разрывах связи – всегда
     * можно подать повторно чек с таким же external_ID. Если данный external_id известен
     * системе будет возвращен UUID, ранее присвоенный этому чеку, иначе чек добавится
     * в систему с присвоением нового UUID.
     * Максимальная длина строки – 256 символов
     */
    @JsonProperty("external_id")
    private String externalId;

    /**
     * Чек
     */
    @JsonProperty("receipt")
    private Receipt receipt;

    /**
     * Служебный раздел
     */
    @JsonProperty("service")
    private Service service;

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

    private String cashier;

}