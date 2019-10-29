package com.rbkmoney.adapter.atol.service.atol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.service.atol.constant.Operations;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ConverterIp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class AtolApi {

    private String company;
    private String inn;
    private String address;
    private String group;

    private String url;

    private String login;
    private String password;
    private String apiToken;

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConverterIp converterIp;

    public ResponseEntity<CommonResponse> getToken(String login, String password) {
        String url = converterIp.replaceIpv4ToIpv6(this.url + "getToken");
        log.info("Atol getToken URL {}", url);

        CommonRequest request = new CommonRequest();
        request.setLogin(login);
        request.setPass(password);
        try {
            String body = objectMapper.writeValueAsString(request);
            HttpEntity httpEntity = new HttpEntity(body, getHttpHeaders());
            ResponseEntity<CommonResponse> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, CommonResponse.class);
            log.info("{} with response: {}", "getToken", response);
            return response;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Чек «Приход»
     */
    public ResponseEntity<CommonResponse> sell(CommonRequest request) {
        return send(request, Operations.SELL);
    }

    /**
     * чек «Возврат прихода»
     */
    public ResponseEntity<CommonResponse> sellRefund(CommonRequest request) {
        return send(request, Operations.SELL_REFUND);
    }

    /**
     * чек «Коррекция прихода»
     */
    public ResponseEntity<CommonResponse> sellCorrection(CommonRequest request) {
        return send(request, Operations.SELL_CORRECTION);
    }

    /**
     * чек «Расход»
     */
    public ResponseEntity<CommonResponse> buy(CommonRequest request) {
        return send(request, Operations.BUY);
    }

    /**
     * чек «Возврат расхода»
     */
    public ResponseEntity<CommonResponse> buyRefund(CommonRequest request) {
        return send(request, Operations.BUY_REFUND);
    }

    /**
     * чек «Коррекция расхода»
     */
    public ResponseEntity<CommonResponse> buyCorrection(CommonRequest request) {
        return send(request, Operations.BUY_CORRECTION);
    }

    /**
     * Получение информации о чеке по uuid-у
     */
    public ResponseEntity<CommonResponse> report(String uuid) {
        String url = converterIp.replaceIpv4ToIpv6(this.url + group + "/report/" + uuid + "?token=" + apiToken);
        log.info("{} with url: {}", "report", url);
        ResponseEntity<CommonResponse> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, CommonResponse.class);
        log.info("{} with response: {}", "report", response);
        return response;
    }

    private ResponseEntity send(CommonRequest request, Operations operation) {
        try {
            String body = objectMapper.writeValueAsString(request);
            log.info("{} with request: {}", operation, body);

            HttpEntity httpEntity = new HttpEntity(body, getHttpHeaders());
            String url = converterIp.replaceIpv4ToIpv6(this.url + group + "/" + operation.getOperation() + "?token=" + apiToken);
            log.info("atol send URL {}", url);

            ResponseEntity<CommonResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, httpEntity, CommonResponse.class);
            log.info("{} with response: {}", operation.getOperation(), responseEntity);
            return responseEntity;
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private HttpHeaders getHttpHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        return httpHeaders;
    }

}
