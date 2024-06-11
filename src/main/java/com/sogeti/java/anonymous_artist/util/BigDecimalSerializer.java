package com.sogeti.java.anonymous_artist.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalSerializer extends JsonSerializer<BigDecimal> {

    private static final DecimalFormat decimalFormat = new DecimalFormat("#,#0.00");

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(decimalFormat.format(value));
    }
}
