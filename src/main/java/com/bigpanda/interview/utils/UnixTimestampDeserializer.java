package com.bigpanda.interview.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

public class UnixTimestampDeserializer extends JsonDeserializer<Date> {
    private static final long serialVersionUID = 1L;

    public UnixTimestampDeserializer() {
    }

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext deserializationContext) throws IOException {
        String timestamp = jp.getText().trim();

        try {
            return new Date(Long.valueOf(timestamp + "000"));
        } catch (NumberFormatException e) {
//                logger.warn('Unable to deserialize timestamp: ' + timestamp, e)
            return null;
        }
    }
}
