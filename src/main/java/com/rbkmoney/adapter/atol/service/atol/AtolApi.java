package com.rbkmoney.adapter.atol.service.atol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.service.atol.constant.Operations;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.cashreg.spring.boot.starter.utils.converter.ip.ConverterIp;
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

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConverterIp converterIp;

    public ResponseEntity<CommonResponse> getToken(RequestWrapper<CommonRequest> requestWrapper) {
        String url = converterIp.replaceIpv4ToIpv6(requestWrapper.getUrl() + "getToken");
        log.info("Atol getToken URL {}", url);

        CommonRequest request = new CommonRequest();
        request.setLogin(requestWrapper.getRequest().getLogin());
        request.setPass(requestWrapper.getRequest().getPass());
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
    public ResponseEntity<CommonResponse> sell(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.SELL);
    }

    /**
     * чек «Возврат прихода»
     */
    public ResponseEntity<CommonResponse> sellRefund(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.SELL_REFUND);
    }

    /**
     * чек «Коррекция прихода»
     */
    public ResponseEntity<CommonResponse> sellCorrection(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.SELL_CORRECTION);
    }

    /**
     * чек «Расход»
     */
    public ResponseEntity<CommonResponse> buy(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.BUY);
    }

    /**
     * чек «Возврат расхода»
     */
    public ResponseEntity<CommonResponse> buyRefund(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.BUY_REFUND);
    }

    /**
     * чек «Коррекция расхода»
     */
    public ResponseEntity<CommonResponse> buyCorrection(RequestWrapper<CommonRequest> requestWrapper) {
        return send(requestWrapper, Operations.BUY_CORRECTION);
    }

    /**
     * Получение информации о чеке по uuid-у
     */
    public ResponseEntity<CommonResponse> report(RequestWrapper<CommonRequest> requestWrapper) {
        String url = converterIp.replaceIpv4ToIpv6(requestWrapper.getUrl() + requestWrapper.getGroup() + "/report/" + requestWrapper.getRequest().getExternalId() + "?token=" + requestWrapper.getToken());
        log.info("{} with url: {}", "report", url);
        ResponseEntity<CommonResponse> response = restTemplate.exchange(url, HttpMethod.GET, HttpEntity.EMPTY, CommonResponse.class);
        log.info("{} with response: {}", "report", response);
        return response;
    }

    private ResponseEntity send(RequestWrapper<CommonRequest> requestWrapper, Operations operation) {
        try {
            String body = objectMapper.writeValueAsString(requestWrapper.getRequest());
            log.info("{} with request: {}", operation, body);

            HttpEntity httpEntity = new HttpEntity(body, getHttpHeaders());
            String url = converterIp.replaceIpv4ToIpv6(requestWrapper.getUrl() + requestWrapper.getGroup() + "/" + operation.getOperation() + "?token=" + requestWrapper.getToken());
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
