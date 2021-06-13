package com.affirm.api.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public interface ICsvParser<key, val> {
    Map<key, val> parse();

    default BufferedReader brReader(String fileName) throws URISyntaxException, IOException {
        URL res = ICsvParser.class.getClassLoader().getResource(fileName);
        return Files.newBufferedReader(Paths.get(res.toURI()));
    }

    default int parseInt(String input) {
        if (input.isEmpty()) return 0;
        return Integer.parseInt(input);
    }

    default float parseFloat(String input) {
        if (input.isEmpty()) return 0f;
        return Float.parseFloat(input);
    }
}
