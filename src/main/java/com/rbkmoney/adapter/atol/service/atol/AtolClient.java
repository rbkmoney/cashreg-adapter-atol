package com.rbkmoney.adapter.atol.service.atol;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.utils.ConverterIp;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Getter
@Setter
@RequiredArgsConstructor
public class AtolClient implements AdapterCashReg {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final ConverterIp converterIp;

    @Override
    public CommonResponse debit(RequestWrapper<CommonRequest> request) {
        AtolApi api = prepareAtolApi(request);
        api.setApiToken(extractToken(request, api));
        ResponseEntity<CommonResponse> responseEntity = api.sell(request.getRequest());
        return responseEntity.getBody();
    }

    @Override
    public CommonResponse credit(RequestWrapper<CommonRequest> request) {
        AtolApi api = prepareAtolApi(request);
        api.setApiToken(extractToken(request, api));
        ResponseEntity<CommonResponse> responseEntity = api.buy(request.getRequest());
        return responseEntity.getBody();
    }

    @Override
    public CommonResponse refundDebit(RequestWrapper<CommonRequest> request) {
        AtolApi api = prepareAtolApi(request);
        api.setApiToken(extractToken(request, api));
        ResponseEntity<CommonResponse> responseEntity = api.sellRefund(request.getRequest());
        return responseEntity.getBody();
    }

    @Override
    public CommonResponse refundCredit(RequestWrapper<CommonRequest> request) {
        AtolApi api = prepareAtolApi(request);
        api.setApiToken(extractToken(request, api));
        ResponseEntity<CommonResponse> responseEntity = api.buyCorrection(request.getRequest());
        return responseEntity.getBody();
    }

    @Override
    public CommonResponse status(RequestWrapper<CommonRequest> request) {
        AtolApi api = prepareAtolApi(request);
        api.setApiToken(extractToken(request, api));
        ResponseEntity<CommonResponse> responseEntity = api.report(request.getRequest().getExternalId());
        return responseEntity.getBody();
    }

    private String extractToken(RequestWrapper<CommonRequest> request, AtolApi api) {
        return api.getToken(request.getRequest().getLogin(), request.getRequest().getPass()).getBody().getToken();
    }

    private AtolApi prepareAtolApi(RequestWrapper<CommonRequest> request) {
        AtolApi api = new AtolApi(restTemplate, objectMapper, converterIp);
        api.setUrl(request.getUrl());
        api.setCompany(request.getCompany());
        api.setAddress(request.getCompanyAddress());
        if (request.getGroup() != null) {
            api.setGroup(request.getGroup());
        }
        return api;
    }

}
