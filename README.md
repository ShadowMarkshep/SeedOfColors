# Seed of Colors

Простая консольная утилита на Java для экспериментов с хэш-функциями: превращает строку (слово или словосочетание) в числовой **сид**, а из сида получает **цвет**. Удобно, чтобы сравнивать, как разные алгоритмы хэширования распределяют значения и какие цвета получаются из одного и того же текста.

> Проект в ранней стадии разработки: часть функций ещё не реализована.

## Возможности

- Интерактивный TUI-интерфейс в терминале (на базе [TamboUI](https://github.com/tamboui/tamboui))
- Ввод произвольного текста, только на русском
- Несколько алгоритмов хэширования за общим интерфейсом `HashAlgorithm`
- Преобразование хэша в цвет
- Набор из 1000 русских словосочетаний для тестирования (`src/main/resources/slovosochetaniya.txt`)

## Алгоритмы

| Алгоритм | Класс | Описание |
|---|---|---|
| Java polynomial | `JavaPolynomial` | Полиномиальный хэш с множителем `31`, как в `String.hashCode()` |
| TikTok polynomial | `TiktokPolynomial` | Полиномиальный хэш с множителем `425267` |
| FNV-1a | `Fnv1a` | 64-битный Fowler–Noll–Vo 1a по байтам UTF-8 |

Все алгоритмы реализуют один интерфейс:

```java
public interface HashAlgorithm {
    long generate(String string);
}
```

### Как добавить свой алгоритм

1. Создайте класс в пакете `ru.markshep.algorithm`.
2. Реализуйте `HashAlgorithm`:

```java
public class MyHash implements HashAlgorithm {
    @Override
    public long generate(String string) {
        long hash = 0;
        for (char c : string.toCharArray()) {
            hash = hash * 17 + c;
        }
        return hash;
    }
}
```

## Установка (Windows)

Готовые сборки лежат на странице [Releases](https://github.com/ShadowMarkshep/SeedOfColors/releases/latest). Java ставить не нужно — она уже встроена в сборку.

**Вариант 1 — установщик `.msi`**

1. Скачайте `SeedOfColors-<версия>.msi`.
2. Запустите и выберите папку установки (права администратора не нужны — ставится для текущего пользователя).
3. Запускайте через ярлык на рабочем столе или из меню «Пуск» → *SeedOfColors*.

Новые версии ставятся поверх старой, удалить программу можно через «Параметры → Приложения».

**Вариант 2 — портативный `.zip`**

1. Скачайте `SeedOfColors-<версия>-windows.zip` и распакуйте в любую папку.
2. Запустите `SeedOfColors\SeedOfColors.exe`.

> Для корректного отображения цветов лучше запускать в [Windows Terminal](https://aka.ms/terminal) — нужен терминал с поддержкой true color.

## Сборка из исходников

### Требования

- JDK 25 или новее
- Терминал с поддержкой true color

Gradle ставить не нужно — используется Gradle Wrapper.

### Запуск

```bash
git clone https://github.com/ShadowMarkshep/SeedOfColors.git
cd SeedOfColors

# Linux / macOS
./gradlew run

# Windows
gradlew.bat run
```

### Сборка дистрибутивов

| Задача | Результат |
|---|---|
| `gradlew installDist` | `build/install/SeedOfColors/` — jar-файлы и скрипты запуска (нужна установленная Java) |
| `gradlew jpackageImage` | `build/jpackage/SeedOfColors/` — `SeedOfColors.exe` со встроенной урезанной Java |
| `gradlew packageApp` | `build/dist/SeedOfColors-<версия>-windows.zip` — то же самое, упакованное в zip |
| `gradlew packageMsi` | `build/dist/SeedOfColors-<версия>.msi` — установщик |

Задачи `jpackageImage`, `packageApp` и `packageMsi` работают только на Windows и используют `jpackage` из JDK 25 (Gradle найдёт его через toolchains). Для `packageMsi` дополнительно нужен [WiX Toolset](https://wixtoolset.org/), доступный в `PATH`.

Версия берётся из `version` в `build.gradle`.

## Лицензия

Проект распространяется под лицензией MIT — подробности в файле [LICENSE](LICENSE).
