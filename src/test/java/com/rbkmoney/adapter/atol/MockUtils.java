package com.rbkmoney.adapter.atol;

import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.Payload;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import org.mockito.stubbing.Answer;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class MockUtils {

    public static void mockAtolClient(AtolClient client) {
        doAnswer((Answer<CommonResponse>) invocation -> {
            CommonResponse response = new CommonResponse();
            response.setStatus(Status.WAIT.getValue());
            response.setUuid("uuid");
            return response;
        }).when(client).debit(any());

        doAnswer((Answer<CommonResponse>) invocation -> {
            CommonResponse response = new CommonResponse();
            response.setStatus(Status.DONE.getValue());
            response.setPayload(Payload.builder()
                    .fnNumber("fnNumber")
                    .fiscalReceiptNumber(1L)
            .build());
            response.setUuid("uuid");
            return response;
        }).when(client).status(any());
    }
}
