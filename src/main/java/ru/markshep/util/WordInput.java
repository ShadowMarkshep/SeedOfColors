package ru.markshep.util;

import java.util.Locale;
import java.util.regex.Pattern;

public final class WordInput {

    private static final Pattern ALLOWED = Pattern.compile("^[а-яё0-9 ]+$");

    private WordInput() {}

    public static String normalize(String raw) {
        return raw.strip().toLowerCase(Locale.ROOT);
    }

    public static boolean isValid(String normalized) {
        return !normalized.isEmpty() && ALLOWED.matcher(normalized).matches();
    }
}