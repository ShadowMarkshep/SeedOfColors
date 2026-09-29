package ru.markshep.algorithm.polynomial;

import ru.markshep.algorithm.HashAlgorithm;

public class JavaPolynomial implements HashAlgorithm {
    private static final int CONST = 31;
    @Override
    public long generate(String string) {
        long hash = 0;
        char[] word = string.toCharArray();
        for (char c : word) {
            hash = hash * CONST + c;
        }
        return hash;
    }
}
