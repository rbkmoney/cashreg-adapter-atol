package com.rbkmoney.adapter.atol.handler;

import com.rbkmoney.adapter.atol.IntegrationTest;
import com.rbkmoney.adapter.atol.MockUtils;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.adapter.atol.service.atol.constant.ErrorType;
import com.rbkmoney.adapter.atol.service.atol.constant.Status;
import com.rbkmoney.adapter.atol.service.atol.model.response.CommonResponse;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

public class AtolServerHandlerTest extends IntegrationTest {

    @MockBean
    public AtolClient client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockUtils.mockAtolClient(client);
    }

    @Test
    public void atolServerHandlerTest() throws TException {
        CashregContext context = makeCashRegContext();
        CashregResult result = handler.register(context);
        assertTrue(result.getIntent().isSetSleep());

        context.getSession().setState(result.getState());

        result = handler.register(context);
        assertTrue(result.getIntent().isSetFinish());
    }

    @Test
    public void atolServerHandlerFailureTest() throws TException {
        doAnswer((Answer<CommonResponse>) invocation -> {
            CommonResponse response = new CommonResponse();
            response.setStatus(Status.FAIL.getValue());

            com.rbkmoney.adapter.atol.service.atol.model.Error error = new com.rbkmoney.adapter.atol.service.atol.model.Error();
            error.setCode(1);
            error.setText("test");
            error.setErrorId("errorId");
            error.setType(ErrorType.SYSTEM.getValue());
            response.setError(error);
            return response;
        }).when(client).debit(any());

        CashregContext context = makeCashRegContext();
        CashregResult result = handler.register(context);
        assertTrue(result.getIntent().getFinish().getStatus().isSetFailure());
    }

}