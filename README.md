![Build](https://github.com/ktomek/funKtional/actions/workflows/ci.yml/badge.svg)
![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)
![GitHub release](https://img.shields.io/github/v/release/ktomek/funKtional)
![GitHub issues](https://img.shields.io/github/issues/ktomek/funKtional)
![GitHub pull requests](https://img.shields.io/github/issues-pr/ktomek/funKtional)
[![JitPack](https://jitpack.io/v/ktomek/funKtional.svg)](https://jitpack.io/#ktomek/funKtional)

# funKtional

A lightweight Kotlin library providing functional programming extensions and utilities for nullable handling, type casting, flow lifting, and more.

## Features

- Suspended `letCo` for coroutine-friendly mapping
- `orDefault` and `orDefaultCo` for safe null defaults
- `onNull` and `onNullCo` for executing side-effects on null
- Reified casts: `asType`, `asTypeOrNull`
- Null-aware `match`, `matchCo`, `matchAction`, `matchCoAction`
- `lift` functions for combining up to three nullable values
- Fully documented with KDoc and usage examples

## Installation

Add JitPack to your repositories and include the dependency:

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation 'com.github.ktomek:funKtional:Tag'
}
```

Replace `Tag` with the release tag or commit SHA you want to use.

## Usage

```kotlin
// letCo example
runBlocking {
    val length = "Hello".letCo { it.length }
    println(length) // 5
}

// orDefault example
val value: String? = null
val default = value.orDefault { "default" }
println(default) // "default"

// asType / asTypeOrNull example
val any: Any = "text"
val text: String? = any.asTypeOrNull<String>()
println(text) // "text"

// lift example
val sum: Int? = 2.lift(3) { a, b -> a + b }
println(sum) // 5
```

### Advanced Usage: Chaining Functional Extensions

```kotlin

runBlocking {
    val x: Int? = null
    val y: Int? = 5
    val z: Int? = 10

    // Combine nullable values, provide default, then transform
    val result = x.lift(y, z) { a, b, c -> a + b + c }
        .orDefault { 0 }
        .letCo { it * 2 }

    println(result) // (0 + 5 + 10) * 2 = 30
}

// Null-safe branching with match
val branch = "Kotlin"
    .match(
        some = { it.length },
        none = { 0 }
    )
    .let { length -> "Length is $length" }
    .also { println(it) } // "Length is 6"
```

## Contributing

Contributions are welcome! Please open issues or submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
