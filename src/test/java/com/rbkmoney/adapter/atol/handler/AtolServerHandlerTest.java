package com.rbkmoney.adapter.atol.handler;

import com.rbkmoney.adapter.atol.IntegrationTest;
import com.rbkmoney.adapter.atol.MockUtils;
import com.rbkmoney.adapter.atol.service.atol.AtolClient;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import com.rbkmoney.damsel.cashreg.provider.CashRegResult;
import org.apache.thrift.TException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.net.URISyntaxException;

import static junit.framework.TestCase.assertTrue;

public class AtolServerHandlerTest extends IntegrationTest {

    @MockBean
    public AtolClient client;

    @Before
    public void setup() throws URISyntaxException, TException {
        MockitoAnnotations.initMocks(this);
        MockUtils.mockAtolClient(client);
    }

    @Test
    public void testMe() throws TException {
        CashRegContext cashRegContext = makeCashRegContext();
        CashRegResult result = handler.register(cashRegContext);
        assertTrue(result.getIntent().isSetSleep());

        cashRegContext.getSession().setState(result.getState());

        result = handler.register(cashRegContext);
        assertTrue(result.getIntent().isSetFinish());
    }

}