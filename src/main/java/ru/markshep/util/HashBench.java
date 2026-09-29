package ru.markshep.util;

import ru.markshep.algorithm.HashAlgorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HashBench {

    private static final int HUES = 360;

    private HashBench() {}

    public static HashStats measure(HashAlgorithm algorithm, List<String> words) {
        Set<Long> unique = new HashSet<>();
        int[] buckets = new int[HUES];

        for (String word : words) {
            long hash = algorithm.generate(word);
            unique.add(hash);
            buckets[ColorUtil.hueFromHash(hash)]++;
        }

        int fullest = 0;
        int emptiest = 0;
        for (int hue = 1; hue < HUES; hue++) {
            if (buckets[hue] > buckets[fullest]) fullest = hue;
            if (buckets[hue] < buckets[emptiest]) emptiest = hue;
        }

        return new HashStats(
                words.size(),
                unique.size(),
                words.size() - unique.size(),
                fullest, buckets[fullest],
                emptiest, buckets[emptiest]
        );
    }
}