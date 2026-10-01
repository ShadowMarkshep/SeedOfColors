package ru.markshep.util;

public record HashStats(
        int total,
        int uniqueHashes,
        int collisions,
        int collisions32,
        double expectedCollisions32,
        int[] hueBuckets,
        int[] bitOnes,
        double avalancheBits,
        double nanosPerWord
) {

    public double expectedPerHue() {
        return (double) total / hueBuckets.length;
    }

    public double chiSquare() {
        double expected = expectedPerHue();
        double chi2 = 0;
        for (int count : hueBuckets) {
            chi2 += (count - expected) * (count - expected) / expected;
        }
        return chi2;
    }

    /** На сколько стандартных отклонений χ^2 ушёл от идеала: |z| < 3 — норма. */
    public double chiSquareZ() {
        int dof = hueBuckets.length - 1;
        return (chiSquare() - dof) / Math.sqrt(2.0 * dof);
    }

    public int emptyHues() {
        int empty = 0;
        for (int count : hueBuckets) {
            if (count == 0) empty++;
        }
        return empty;
    }

    public int fullestHue() {
        int fullest = 0;
        for (int hue = 1; hue < hueBuckets.length; hue++) {
            if (hueBuckets[hue] > hueBuckets[fullest]) fullest = hue;
        }
        return fullest;
    }

    public int emptiestHue() {
        int emptiest = 0;
        for (int hue = 1; hue < hueBuckets.length; hue++) {
            if (hueBuckets[hue] < hueBuckets[emptiest]) emptiest = hue;
        }
        return emptiest;
    }

    /** Доля хэшей, в которых бит равен 1. Идеал — 0.5. */
    public double bitRatio(int bit) {
        return (double) bitOnes[bit] / total;
    }

    public int worstBit() {
        int worst = 0;
        for (int bit = 1; bit < bitOnes.length; bit++) {
            if (Math.abs(bitRatio(bit) - 0.5) > Math.abs(bitRatio(worst) - 0.5)) worst = bit;
        }
        return worst;
    }
}
