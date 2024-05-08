package org.weare4saken.trackingsystem.rest.converter;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomLocalDateDeserializer extends JsonDeserializer<LocalDate> {
    private final String pattern = "dd-MM-yyyy";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(this.pattern);

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext context) throws IOException {
        String valueAsString = p.getValueAsString();
        try {
            return LocalDate.parse(valueAsString, this.formatter);
        } catch (DateTimeException e) {
            throw new JsonParseException(p, "Невозможно отобразить дату: " + valueAsString +
                    ", корректный формат даты: " + this.pattern, e);
        }
    }
}