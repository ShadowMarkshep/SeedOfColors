package ru.markshep;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.widgets.input.TextInputState;
import ru.markshep.util.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static dev.tamboui.toolkit.Toolkit.*;

public class Main extends ToolkitApp {

    private static final String SWATCH = "████";

    private final TextInputState inputState = new TextInputState();
    private final Hashes[] items = Hashes.values();

    private final List<String> words = WordSource.load();
    private final Map<Hashes, HashStats> statsCache = new EnumMap<>(Hashes.class);

    private String result = "";
    private Color resultColor = Color.WHITE;
    private HashStats stats;

    private final ListElement<?> menu = list()
            .data(Arrays.stream(items).toList(), h -> text(h.getTitle()))
            .id("list")
            .highlightStyle(Style.EMPTY.reversed())
            .rounded()
            .highlightSymbol(">> ")
            .title("Алгоритм");

    @Override
    protected Element render() {
        return panel("Seed of Colors",
                textInput(inputState)
                        .id("input")
                        .placeholder("Введите текст на русском...")
                        .onSubmit(this::handleSubmit)
                        .rounded(),
                text(result).style(Style.EMPTY.fg(resultColor)),
                menu,
                statsPanel()
        ).rounded();
    }

    private Element statsPanel() {
        if (stats == null) {
            return panel("Замеры", text("Нажмите Enter").dim()).rounded();
        }
        return panel("Замеры",
                text("Словосочетаний: " + stats.total()),
                text("Разных хэшей: " + stats.uniqueHashes()),
                text("Коллизий: " + stats.collisions()),
                spacer(),
                swatchRow("Самый частый оттенок", stats.fullestHue(), stats.fullestCount()),
                swatchRow("Самый редкий оттенок", stats.emptiestHue(), stats.emptiestCount())
        ).rounded();
    }

    private Element swatchRow(String label, int hue, int count) {
        RGB rgb = ColorUtil.rgbFromHue(hue);
        return row(
                text(SWATCH).style(Style.EMPTY.fg(Color.rgb(rgb.red(), rgb.green(), rgb.blue()))),
                text(" " + label + ": " + hue + "° — " + count + " шт.")
        );
    }

    private void handleSubmit() {
        String word = WordInput.normalize(inputState.text());

        if (!WordInput.isValid(word)) {
            showError("Только кириллица, цифры и пробелы!");
            return;
        }

        Hashes selected = selectedValue();
        long hash = selected.getAlgorithm().generate(word);
        RGB rgb = ColorUtil.rgbFromHash(hash);

        result = String.valueOf(hash);
        resultColor = Color.rgb(rgb.red(), rgb.green(), rgb.blue());

        stats = statsCache.computeIfAbsent(selected, h -> HashBench.measure(h.getAlgorithm(), words));
    }

    private void showError(String message) {
        result = message;
        resultColor = Color.RED;
    }

    private Hashes selectedValue() {
        return items[menu.selected()];
    }

    public static void main(String[] args) throws Exception {
        new Main().run();
    }
}