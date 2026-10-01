package ru.markshep.algorithm.shadow;

import ru.markshep.algorithm.HashAlgorithm;

import java.nio.charset.StandardCharsets;

public class ShadowHash implements HashAlgorithm {
    @Override
    public long generate(String string) {
        long hash = 0;
        for (byte b : string.getBytes(StandardCharsets.UTF_8)) {
            hash += b;
            hash *= 77;
            hash ^= b;
        }
        return hash;
    }
}
