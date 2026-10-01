package ru.markshep.util;

public final class ColorUtil {

    private static final int SATURATION = 70;
    private static final int VALUE = 90;

    private ColorUtil() {}

    public static RGB rgbFromHash(long hash) {
        return toRgb(hsvFromHash(hash));
    }

    public static HSV hsvFromHash(long hash) {
        return new HSV(hueFromHash(hash), SATURATION, VALUE);
    }

    public static RGB toRgb(HSV hsv) {
        float v = hsv.value() / 100f;
        float s = hsv.saturation() / 100f;

        float max = v;
        float min = v * (1 - s);

        int sector = hsv.hue() / 60;
        float f = (hsv.hue() % 60) / 60f;

        float rising = min + (max - min) * f;
        float falling = max - (max - min) * f;

        return switch (sector) {
            case 0 -> rgb(max, rising, min);
            case 1 -> rgb(falling, max, min);
            case 2 -> rgb(min, max, rising);
            case 3 -> rgb(min, falling, max);
            case 4 -> rgb(rising, min, max);
            case 5 -> rgb(max, min, falling);
            default -> throw new IllegalStateException("оттенок вне 0..359: " + hsv.hue());
        };
    }

    private static RGB rgb(float r, float g, float b) {
        return new RGB(to255(r), to255(g), to255(b));
    }

    private static int to255(float value) {
        return Math.round(value * 255);
    }

    /** Сворачиваем все 64 бита, чтобы короткие строки не слипались в оттенок 0°. */
    public static int hueFromHash(long hash) {
        long folded = hash ^ (hash >>> 32);
        folded ^= folded >>> 16;
        return (int) Long.remainderUnsigned(folded, 360);
    }

    public static RGB rgbFromHue(int hue) {
        return toRgb(new HSV(hue, SATURATION, VALUE));
    }
}