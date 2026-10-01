package ru.markshep.util;

import ru.markshep.algorithm.HashAlgorithm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class HashBench {

    private static final int HUES = 360;
    private static final int BITS = 64;

    private HashBench() {}

    public static HashStats measure(HashAlgorithm algorithm, List<String> words) {
        int n = words.size();

        hashAll(algorithm, words);
        long start = System.nanoTime();
        long[] hashes = hashAll(algorithm, words);
        double nanosPerWord = (double) (System.nanoTime() - start) / n;

        Set<Long> unique = new HashSet<>();
        Set<Integer> unique32 = new HashSet<>();
        int[] buckets = new int[HUES];
        int[] bitOnes = new int[BITS];

        for (long hash : hashes) {
            unique.add(hash);
            unique32.add((int) hash);
            buckets[ColorUtil.hueFromHash(hash)]++;
            for (int bit = 0; bit < BITS; bit++) {
                if ((hash >>> bit & 1) == 1) bitOnes[bit]++;
            }
        }

        return new HashStats(
                n,
                unique.size(),
                n - unique.size(),
                n - unique32.size(),
                (double) n * (n - 1) / 2 / Math.pow(2, 32),
                buckets,
                bitOnes,
                avalanche(algorithm, words, hashes),
                nanosPerWord
        );
    }

    private static long[] hashAll(HashAlgorithm algorithm, List<String> words) {
        long[] hashes = new long[words.size()];
        for (int i = 0; i < hashes.length; i++) {
            hashes[i] = algorithm.generate(words.get(i));
        }
        return hashes;
    }

    /** Меняем младший бит последнего символа и считаем, сколько бит хэша перевернулось. Идеал — 32. */
    private static double avalanche(HashAlgorithm algorithm, List<String> words, long[] hashes) {
        long flipped = 0;
        for (int i = 0; i < hashes.length; i++) {
            String word = words.get(i);
            int last = word.length() - 1;
            String changed = word.substring(0, last) + (char) (word.charAt(last) ^ 1);
            flipped += Long.bitCount(hashes[i] ^ algorithm.generate(changed));
        }
        return (double) flipped / hashes.length;
    }
}
