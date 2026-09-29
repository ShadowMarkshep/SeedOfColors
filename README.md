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
| FNV-1a | `FNV_1a` | Fowler–Noll–Vo 1a *(в разработке)* |

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

## Требования

- JDK 25 или новее
- Терминал с поддержкой true color (для корректного отображения цветов)

Gradle ставить не нужно — используется Gradle Wrapper.

## Запуск

```bash
git clone https://github.com/Markshep/SeedOfColors.git
cd SeedOfColors

# Linux / macOS
./gradlew run

# Windows
gradlew.bat run
```

Сборка дистрибутива со скриптами запуска:

```bash
./gradlew installDist
./build/install/SeedOfColors/bin/SeedOfColors
```

## Лицензия

Проект распространяется под лицензией MIT — подробности в файле [LICENSE](LICENSE).
