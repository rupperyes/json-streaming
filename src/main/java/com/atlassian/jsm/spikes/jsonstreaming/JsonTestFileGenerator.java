package com.atlassian.jsm.spikes.jsonstreaming;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonFactoryBuilder;
import com.fasterxml.jackson.core.JsonGenerator;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

public class JsonTestFileGenerator {
    private static final JsonFactory jsonFactory = new JsonFactoryBuilder().build();
    private static final Random random = new Random();

    public static void main(String[] args) throws IOException {
        try (JsonGenerator generator = jsonFactory.createGenerator(new BufferedOutputStream(new FileOutputStream("items_1.json"), 1024 * 1024))) {
            generator.writeStartObject();
            generator.writeFieldName("items");
            generator.writeStartArray();
            for (int i = 0; i < 1; i++) {
                addObject(generator);
            }
            generator.writeEndArray();
            generator.writeEndObject();
        }
    }

    private static void addObject(JsonGenerator generator) throws IOException {
        generator.writeStartObject();
        generator.writeStringField("key", Long.toString(random.nextLong(), Character.MAX_RADIX));
        generator.writeNumberField("number1", random.nextLong());
        generator.writeNumberField("number2", random.nextFloat());

        generator.writeFieldName("elements");
        generator.writeStartArray();
        for (int i = 0; i < random.nextInt(10); i++) {
            generator.writeNumber(random.nextInt(10000));
        }
        generator.writeEndArray();
        generator.writeEndObject();
    }
}
