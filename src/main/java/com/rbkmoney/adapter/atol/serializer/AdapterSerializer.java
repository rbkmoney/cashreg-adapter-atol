package com.rbkmoney.adapter.atol.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rbkmoney.adapter.atol.model.AdapterContext;
import com.rbkmoney.adapter.common.serializer.StateSerializer;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AdapterSerializer extends StateSerializer<AdapterContext> {

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

}
