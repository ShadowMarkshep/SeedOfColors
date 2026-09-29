package ru.markshep.algorithm;

import java.nio.charset.StandardCharsets;

public class Fnv1a implements HashAlgorithm {
    private static final long OFFSET_BASIS = 0xcbf29ce484222325L;
    private static final long PRIME = 0x100000001b3L;

    @Override
    public long generate(String string) {
        long hash = OFFSET_BASIS;
        for (byte b : string.getBytes(StandardCharsets.UTF_8)) {
            hash ^= (b & 0xFF);
            hash *= PRIME;
        }
        return hash;
    }
}