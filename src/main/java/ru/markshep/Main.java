package ru.markshep;

import dev.tamboui.toolkit.app.ToolkitApp;
import dev.tamboui.toolkit.element.Element;
import dev.tamboui.widgets.input.TextInputState;

import static dev.tamboui.toolkit.Toolkit.*;

public class Main extends ToolkitApp {

    private final TextInputState inputState = new TextInputState();

    @Override
    protected Element render() {
        return panel("Seed of Colors",
                textInput(inputState)
                        .id("input")
                        .placeholder("Введите текст на русском...")
                        .onSubmit(() -> {

                        })
                        .rounded(),
                list("A", "B", "C")
                        .id("list")
                        .rounded()
        ).rounded();
    }

    public static void main(String[] args) throws Exception {
        new Main().run();
    }
}
