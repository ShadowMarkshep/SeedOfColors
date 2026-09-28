package ru.markshep.algorithm.polynomial;

import ru.markshep.algorithm.HashAlgorithm;

public class TiktokPolynomial implements HashAlgorithm {
    private static final int CONST = 425267;
    @Override
    public long generate(String string) {
        long hash = 0;
        char[] word = string.toCharArray();
        for (char c : word) {
            hash += c;
            hash *= CONST;
        }
        return hash;
    }
}
