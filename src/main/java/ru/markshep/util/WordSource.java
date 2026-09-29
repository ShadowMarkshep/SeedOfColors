package ru.markshep.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public final class WordSource {

    private static final String RESOURCE = "/slovosochetaniya.txt";

    private WordSource() {}

    public static List<String> load() {
        try (InputStream in = WordSource.class.getResourceAsStream(RESOURCE)) {
            if (in == null) {
                throw new IllegalStateException("не найден ресурс " + RESOURCE);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
                return reader.lines()
                        .map(WordInput::normalize)
                        .filter(WordInput::isValid)
                        .distinct()
                        .toList();
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}