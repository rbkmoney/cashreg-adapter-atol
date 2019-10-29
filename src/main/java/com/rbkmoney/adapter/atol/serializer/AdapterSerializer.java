package com.rbkmoney.adapter.atol.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.model.AdapterContext;
import com.rbkmoney.adapter.common.serializer.StateSerializer;
import com.rbkmoney.damsel.cashreg.provider.CashRegContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdapterSerializer extends StateSerializer<AdapterContext> {

    private static final byte[] DEFAULT_STATE = new byte[0];

    public AdapterSerializer(ObjectMapper mapper) {
        super(mapper);
    }

    public AdapterContext read(byte[] data) {
        if (data == null) {
            return new AdapterContext();
        }
        try {
            return getMapper().readValue(data, AdapterContext.class);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public AdapterContext getAdapterContext(CashRegContext context) {
        AdapterContext adapterContext = new AdapterContext();
        byte[] state = getState(context);
        if (state != null && state.length > 0) {
            return this.read(state);
        }
        return adapterContext;
    }

    public static byte[] getState(CashRegContext context) {
        if (context.getSession().isSetState()) {
            return context.getSession().getState();
        }
        return DEFAULT_STATE;
    }
}
