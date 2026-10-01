package ru.markshep.util;

import ru.markshep.algorithm.Fnv1a;
import ru.markshep.algorithm.HashAlgorithm;
import ru.markshep.algorithm.polynomial.JavaPolynomial;
import ru.markshep.algorithm.polynomial.TiktokPolynomial;
import ru.markshep.algorithm.shadow.ShadowHash;

public enum Hashes {
    JAVA("Java", new JavaPolynomial()),
    FNV1A("FNV1A", new Fnv1a()),
    TIKTOK("TikTok", new TiktokPolynomial()),
    SHADOWHASH("ShadowHash", new ShadowHash());

    private final String title;
    private final HashAlgorithm algorithm;

    Hashes(String title, HashAlgorithm algorithm) {
        this.title = title;
        this.algorithm = algorithm;
    }

    public String getTitle() {
        return title;
    }

    public HashAlgorithm getAlgorithm() {
        return algorithm;
    }
}
