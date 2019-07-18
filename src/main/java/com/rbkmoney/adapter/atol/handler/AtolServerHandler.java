package com.rbkmoney.adapter.atol.handler;


import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.service.atol.constant.CommonError;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.adapter.atol.service.atol.model.response.ReportResponse;
import com.rbkmoney.cashreg.proto.provider.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static com.rbkmoney.adapter.atol.service.atol.constant.Status.isDone;
import static com.rbkmoney.adapter.atol.service.atol.constant.Status.isWait;
import static com.rbkmoney.adapter.atol.utils.wrapper.cashreg.constant.Error.DEFAULT_ERROR_CODE;
import static com.rbkmoney.adapter.atol.utils.wrapper.cashreg.constant.Error.EMPTY_BODY;
import static com.rbkmoney.adapter.atol.utils.wrapper.cashreg.creators.CashRegProviderCreators.*;


@Slf4j
@Component
@RequiredArgsConstructor
public class AtolServerHandler implements CashRegProviderSrv.Iface {

    private final AtolClient client;

    @Override
    public CashRegResult debit(CashRegContext context) throws TException {
        log.info("Start: debit with request_id {}", context.getRequestId());
        try {
            ResponseEntity<CommonResponse> response = (ResponseEntity<CommonResponse>) client.debit(context);
            Intent intent = getIntent(response);
            String originalResponse = Objects.requireNonNull(response.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: debit with request_id {}, response {}", context.getRequestId(), response);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CashRegResult credit(CashRegContext context) throws TException {
        throw new TException("Method Credit Not Supported");
    }

    @Override
    public CashRegResult refundDebit(CashRegContext context) throws TException {
        log.info("Start: refund debit with request_id {}", context.getRequestId());
        try {
            ResponseEntity<CommonResponse> response = (ResponseEntity<CommonResponse>) client.refundDebit(context);
            Intent intent = getIntent(response);
            String originalResponse = Objects.requireNonNull(response.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: refund debit with request_id {}, response {}", context.getRequestId(), response);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    @Override
    public CashRegResult refundCredit(CashRegContext context) throws TException {
        throw new TException("Method Credit Not Supported");
    }

    @Override
    public CashRegResult getStatus(CashRegContext context) throws TException {
        log.info("Start: status with request_id {}", context.getRequestId());
        try {
            ResponseEntity<ReportResponse> response = (ResponseEntity<ReportResponse>) client.getStatus(context);
            Intent intent;
            if (!hasBody(response)) {
                intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, EMPTY_BODY));
            } else {
                ReportResponse reportResponse = response.getBody();
                intent = getIntent(response, reportResponse);
            }

            String originalResponse = Objects.requireNonNull(response.getBody()).toString();
            CashRegResult cashRegResult = createCashRegResult(intent, originalResponse);
            log.info("Finish: status with request_id {}, response {}", context.getRequestId(), response);
            return cashRegResult;
        } catch (Exception ex) {
            log.error("Exception with request_id {}: ", context.getRequestId());
            throw new RuntimeException(ex);
        }
    }

    private static boolean hasError(ReportResponse response) {
        return (response.getError() != null);
    }

    private static boolean hasError(CommonResponse response) {
        return (response.getError() != null);
    }

    private static boolean unknownStatus(ReportResponse response) {
        return response.getError().getCode().toString().equalsIgnoreCase(CommonError.CHECK_STATUS_NOT_FOUND_TRY_LATER.getCode()) &&
                response.getError().getText().equalsIgnoreCase(CommonError.CHECK_STATUS_NOT_FOUND_TRY_LATER.getDescription()
                );
    }

    private static boolean hasBody(ResponseEntity<?> response) {
        return response.getBody() == null;
    }

    private Intent getIntent(ResponseEntity<ReportResponse> response, ReportResponse reportResponse) {
        Intent intent;
        if (hasError(reportResponse)) {
            if (unknownStatus(reportResponse)) {
                intent = createFinishIntentSuccess(response.getBody().getUuid());
            } else {
                intent = createFinishIntentFailure(createFailure(reportResponse.getError()));
            }
        } else if (isWait(reportResponse)) {
            intent = createFinishIntentSuccess(response.getBody().getUuid());
        } else if (isDone(reportResponse)) {
            intent = createFinishIntentSuccess(preparePayload(reportResponse));
        } else {
            intent = createFinishIntentFailure(createFailure(response.getBody().getStatus()));
        }
        return intent;
    }

    private Intent getIntent(ResponseEntity<CommonResponse> response, CommonResponse commonResponse) {
        Intent intent;
        if (hasError(commonResponse)) {
            intent = createFinishIntentFailure(createFailure(response.getBody().getError()));
        } else if (isWait(commonResponse)) {
            intent = createFinishIntentSuccess(response.getBody().getUuid());
        } else {
            intent = createFinishIntentFailure(createFailure(response.getBody().getStatus()));
        }
        return intent;
    }

    private Intent getIntent(ResponseEntity<CommonResponse> response) {
        Intent intent;
        if (!hasBody(response)) {
            intent = createFinishIntentFailure(createFailure(DEFAULT_ERROR_CODE, EMPTY_BODY));
        } else {
            CommonResponse commonResponse = response.getBody();
            intent = getIntent(response, commonResponse);
        }
        return intent;
    }

    private static Payload preparePayload(ReportResponse reportResponse) {
        com.rbkmoney.adapter.atol.service.atol.model.Payload reportPayload = reportResponse.getPayload();
        Payload payload = new Payload();
        payload.setTotal(reportPayload.getTotal().toString());
        payload.setFnsSite(reportPayload.getFnsSite());
        payload.setFnNumber(reportPayload.getFnNumber());
        payload.setShiftNumber(reportPayload.getShiftNumber().toString());
        payload.setReceiptDatetime(reportPayload.getReceiptDatetime());
        payload.setFiscalReceiptNumber(reportPayload.getFiscalReceiptNumber().toString());
        payload.setFiscalDocumentNumber(reportPayload.getFiscalDocumentNumber().toString());
        payload.setEcrRegistrationNumber(reportPayload.getEcrRegistrationNumber());
        payload.setFiscalDocumentAttribute(reportPayload.getFiscalDocumentAttribute().toString());
        return payload;
    }
}
