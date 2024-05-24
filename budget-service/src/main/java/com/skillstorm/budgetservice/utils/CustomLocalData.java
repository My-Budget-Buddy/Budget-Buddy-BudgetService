package com.skillstorm.budgetservice.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.time.LocalDate;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

// This is a custom deserializer for the LocalDate class, the purpose of this class is to make sure the date is in the format of yyyy-MM-dd for local date. The bucket and budget only need month and year, but the date needs to be in the format of yyyy-MM-dd for local date
public class CustomLocalData extends JsonDeserializer<LocalDate> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String date = p.getText();
        return LocalDate.parse(date + "-01", formatter); // This will add the day to the date to make sure it is in the format of yyyy-MM-dd for local date, but the day is not important
    }

}
