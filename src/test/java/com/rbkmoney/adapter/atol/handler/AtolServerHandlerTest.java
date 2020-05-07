package com.rbkmoney.adapter.atol.handler;

import com.rbkmoney.adapter.atol.IntegrationTest;
import com.rbkmoney.adapter.atol.MockUtils;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.damsel.cashreg.adapter.CashregContext;
import com.rbkmoney.damsel.cashreg.adapter.CashregResult;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertTrue;

public class AtolServerHandlerTest extends IntegrationTest {

    @MockBean
    public AtolClient client;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MockUtils.mockAtolClient(client);
    }

    @Test
    public void testMe() throws TException {
        CashregContext context = makeCashRegContext();
        CashregResult result = handler.register(context);
        assertTrue(result.getIntent().isSetSleep());

        context.getSession().setState(result.getState());

        result = handler.register(context);
        assertTrue(result.getIntent().isSetFinish());
    }

}