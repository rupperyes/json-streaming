package com.atlassian.jsm.spikes.jsonstreaming;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileReader;

import static com.atlassian.jsm.spikes.jsonstreaming.Utils.withMemoryProfiling;

public class ParserInMemory {
    public static void main(String[] args) throws Exception {
        System.out.println("Using in-memory parser for file " + args[0]);
        withMemoryProfiling(() -> {
            ObjectMapper objectMapper = new ObjectMapper();

            Model model = objectMapper.reader().readValue(new FileReader(args[0]), Model.class);

            System.gc();
            return model.items.size();
        });
    }
}
