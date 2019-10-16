package com.rbkmoney.adapter.atol.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Base64;

@Component
@Getter
@Setter
@AllArgsConstructor
public abstract class StateSerializer<T> implements Serializer<T> {

    protected final ObjectMapper mapper;

    @Override
    public byte[] writeByte(Object obj) {
        try {
            return mapper.writeValueAsBytes(obj);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public String writeString(Object obj) {
        try {
            return Base64.getEncoder().encodeToString(getMapper().writeValueAsBytes(obj));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public T read(byte[] data) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public T read(String data) {
        throw new RuntimeException("Not supported");
    }
}