package com.rbkmoney.adapter.atol.service.atol;


import com.rbkmoney.adapter.atol.service.atol.model.request.CommonRequest;
import com.rbkmoney.adapter.atol.service.atol.model.request.RequestWrapper;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;

public interface AdapterCashReg {

    CommonResponse debit(RequestWrapper<CommonRequest> request);

    CommonResponse credit(RequestWrapper<CommonRequest> request);

    CommonResponse refundDebit(RequestWrapper<CommonRequest> request);

    CommonResponse refundCredit(RequestWrapper<CommonRequest> request);

    CommonResponse status(RequestWrapper<CommonRequest> request);

}
