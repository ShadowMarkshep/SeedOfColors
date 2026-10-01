package ru.markshep;

import dev.tamboui.style.Color;
import dev.tamboui.style.Style;
import dev.tamboui.text.Line;
import dev.tamboui.text.Span;
import dev.tamboui.text.Text;
import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.toolkit.elements.ListElement;
import dev.tamboui.toolkit.event.EventResult;
import dev.tamboui.tui.event.KeyCode;
import dev.tamboui.tui.event.KeyEvent;
import dev.tamboui.widgets.input.TextInputState;
import ru.markshep.ui.StatsView;
import ru.markshep.util.*;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static dev.tamboui.toolkit.Toolkit.*;

/**
 * Обновление для версии 1.0.3 было написано нейронкой Claude Opus 5.5
 * Не бейте ссаными тряпками пж
 * Все следующие обновы буду писать преимущественно руками
 */
public class Main extends ToolkitApp {

    private static final String SWATCH = "██████████";

    private enum Page {
        MAIN("F1", "Главная"),
        STATS("F2", "Замеры");

        final String key;
        final String title;

        Page(String key, String title) {
            this.key = key;
            this.title = title;
        }
    }

    private final TextInputState inputState = new TextInputState();
    private final Hashes[] items = Hashes.values();

    private final List<String> words = WordSource.load();
    private final Map<Hashes, HashStats> statsCache = new EnumMap<>(Hashes.class);

    private Page page = Page.MAIN;

    private String error;
    private Long hash;
    private int hue;
    private RGB rgb;

    private final ListElement<?> menu = list()
            .data(Arrays.stream(items).toList(), h -> text(h.getTitle()))
            .id("list")
            .highlightStyle(Style.EMPTY.reversed())
            .rounded()
            .highlightSymbol(">> ")
            .title("Алгоритм");

    @Override
    protected void onStart() {
        runner().eventRouter().addGlobalHandler(event -> {
            if (!(event instanceof KeyEvent key)) return EventResult.UNHANDLED;
            if (key.isKey(KeyCode.F1)) return open(Page.MAIN);
            if (key.isKey(KeyCode.F2)) return open(Page.STATS);
            if (page == Page.STATS) {
                if (key.isKey(KeyCode.UP)) {
                    menu.selectPrevious();
                    return EventResult.HANDLED;
                }
                if (key.isKey(KeyCode.DOWN)) {
                    menu.selectNext(items.length);
                    return EventResult.HANDLED;
                }
                if (key.isKey(KeyCode.ESCAPE)) return open(Page.MAIN);
            }
            return EventResult.UNHANDLED;
        });
    }

    private EventResult open(Page target) {
        if (target == Page.STATS) ensureStats();
        page = target;
        return EventResult.HANDLED;
    }

    @Override
    protected Element render() {
        return panel("Seed of Colors",
                tabBar(),
                page == Page.MAIN ? mainPage() : statsPage()
        ).rounded();
    }

    private Element mainPage() {
        return column(
                textInput(inputState)
                        .id("input")
                        .placeholder("Введите текст на русском...")
                        .onSubmit(this::handleSubmit)
                        .rounded(),
                panel("Результат", resultContent()).rounded().length(4),
                menu.length(items.length + 2),
                spacer(),
                footer("Enter — посчитать · Tab — к списку алгоритмов · F2 — замеры")
        );
    }

    private Element statsPage() {
        HashStats stats = statsCache.get(selectedValue());
        return column(
                panel("Сравнение алгоритмов", StatsView.comparison(statsCache, selectedValue()))
                        .rounded().length(items.length + 3),
                panel("Распределение по оттенкам — " + selectedValue().getTitle(), StatsView.hueHistogram(stats))
                        .rounded().length(12),
                row(
                        panel("Замеры", StatsView.metrics(stats)).rounded().fill(),
                        panel("Баланс битов (доля единиц)", StatsView.bitBalance(stats)).rounded().length(74)
                ).length(13),
                spacer(),
                footer("↑/↓ — алгоритм · F1 / Esc — назад · " + words.size() + " строк в наборе")
        );
    }

    private Element tabBar() {
        Span[] spans = new Span[Page.values().length];
        for (Page p : Page.values()) {
            Style style = p == page ? Style.EMPTY.bold().reversed() : Style.EMPTY.fg(Color.DARK_GRAY);
            spans[p.ordinal()] = Span.styled(" " + p.key + " " + p.title + " ", style);
        }
        return richText(Text.from(Line.from(spans))).length(1);
    }

    private Element resultContent() {
        if (error != null) {
            return text(error).style(Style.EMPTY.fg(Color.LIGHT_RED));
        }
        if (hash == null) {
            return text("Введите текст и нажмите Enter").dim();
        }
        Style swatch = Style.EMPTY.fg(Color.rgb(rgb.red(), rgb.green(), rgb.blue()));
        Style dim = Style.EMPTY.fg(Color.DARK_GRAY);
        String hex = String.format("#%02X%02X%02X", rgb.red(), rgb.green(), rgb.blue());
        return richText(Text.from(
                Line.from(Span.styled(SWATCH, swatch), Span.styled("  хэш     ", dim), Span.raw(String.valueOf(hash))),
                Line.from(Span.styled(SWATCH, swatch), Span.styled("  оттенок ", dim), Span.raw(hue + "°  " + hex))
        ));
    }

    private Element footer(String hint) {
        return text(hint).style(Style.EMPTY.fg(Color.DARK_GRAY)).length(1);
    }

    private void handleSubmit() {
        String word = WordInput.normalize(inputState.text());

        if (!WordInput.isValid(word)) {
            error = "Только кириллица, цифры и пробелы!";
            return;
        }

        error = null;
        hash = selectedValue().getAlgorithm().generate(word);
        hue = ColorUtil.hueFromHash(hash);
        rgb = ColorUtil.rgbFromHash(hash);
    }

    private void ensureStats() {
        if (statsCache.isEmpty()) {
            for (Hashes h : items) {
                statsCache.put(h, HashBench.measure(h.getAlgorithm(), words));
            }
        }
    }

    private Hashes selectedValue() {
        return items[menu.selected()];
    }

    public static void main(String[] args) throws Exception {
        new Main().run();
    }
}
