package ru.markshep.util;

public record HashStats(
        int total,
        int uniqueHashes,
        int collisions,
        int fullestHue,
        int fullestCount,
        int emptiestHue,
        int emptiestCount
) {}