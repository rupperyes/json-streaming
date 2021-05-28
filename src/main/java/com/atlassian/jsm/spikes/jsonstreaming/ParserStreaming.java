package com.atlassian.jsm.spikes.jsonstreaming;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.atlassian.jsm.spikes.jsonstreaming.Utils.withMemoryProfiling;

public class ParserStreaming {
    public static void main(String[] args) throws Exception {
        System.out.println("Using streaming parser for file " + args[0]);

        withMemoryProfiling(() -> {
            int objectCount = 0;
            JsonParser parser = new JsonFactory().createParser(new FileReader(args[0]));

            if (parser.nextToken() != JsonToken.START_OBJECT)
                throw new UnexpectedTokenException(parser, JsonToken.START_OBJECT);
            if (parser.nextToken() != JsonToken.FIELD_NAME)
                throw new UnexpectedTokenException(parser, JsonToken.FIELD_NAME);
            if (parser.nextToken() != JsonToken.START_ARRAY)
                throw new UnexpectedTokenException(parser, JsonToken.START_ARRAY);

            JsonToken jsonToken;
            final Map<String, Object> values = new HashMap<>();
            while ((jsonToken = parser.nextToken()) != null) {
                if (jsonToken == JsonToken.END_ARRAY) break;

                // object start
                values.clear();
                objectCount++;

                while ((jsonToken = parser.nextToken()) != null && jsonToken != JsonToken.END_OBJECT) {
                    if (jsonToken != JsonToken.FIELD_NAME)
                        throw new UnexpectedTokenException(parser, JsonToken.FIELD_NAME);

                    values.put(parser.currentName(), getValue(parser));
                }
            }

            if (parser.nextToken() != JsonToken.END_OBJECT)
                throw new UnexpectedTokenException(parser, JsonToken.START_OBJECT);

            System.gc();
            return objectCount;
        });
    }

    private static Object getValue(JsonParser parser) throws IOException {
        JsonToken jsonToken = parser.nextToken();
        if (jsonToken == JsonToken.START_ARRAY) {
            ArrayList<Object> result = new ArrayList<>();
            while (parser.nextToken() != JsonToken.END_ARRAY) {
                result.add(parser.getValueAsString());
            }
            return result.toArray();
        } else {
            return parser.getValueAsString();
        }
    }

    static class UnexpectedTokenException extends Exception {
        public UnexpectedTokenException(JsonParser parser, JsonToken... expected) {
            super("Unexpected token " + parser.currentToken().name() + " at " + parser.getTokenLocation().sourceDescription() + "; Expected one of " + Arrays.toString(expected));
        }
    }
}
