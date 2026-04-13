# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Development Commands

```bash
./gradlew build                     # Full build (all targets)
./gradlew testDebugUnitTest         # Android unit tests (runs on JVM, no emulator needed)
./gradlew iosSimulatorArm64Test     # iOS simulator tests (requires macOS + Xcode)
./gradlew testDebugUnitTest --tests "com.github.ktomek.funktional.TExtTest"  # Single test class
./gradlew detekt                    # Static analysis (must pass with 0 issues)
./gradlew dokkaHtml                 # Generate HTML documentation
```

Note: `detekt` is excluded from the `check` task and must be run separately.

## Architecture

Kotlin Multiplatform library targeting Android and iOS (iosX64, iosArm64, iosSimulatorArm64). No runtime dependencies beyond `kotlinx-coroutines-core`. All code lives in `src/commonMain/kotlin/com/github/ktomek/funktional/` across three files:

- **FunExt.kt** — `lift()` functions: apply a function only when 2–3 nullable inputs are all non-null
- **TExt.kt** — Extension functions on nullable `T`: `orDefault`, `onNull`, `letCo`, `match`, `matchAction`, and their `*Co` suspend variants
- **AnyExt.kt** — `asType<R>()` (strict cast) and `asTypeOrNull<R>()` (safe cast)

The `*Co` naming convention consistently marks suspend variants throughout the API.

## Key Configuration

- **Kotlin**: 2.1.20, JVM toolchain 17
- **Targets**: `androidTarget` (minSdk 21, compileSdk 34), `iosX64`, `iosArm64`, `iosSimulatorArm64`
- **Detekt config**: `config/detekt-config.yml` — `buildUponDefaultConfig: true`, max issues: 0
- **Version**: extracted from git tags at build time (fallback `1.0.0`)
- **Publishing**: Maven Central via JitPack; release triggered by `v*.*.*` tags

## Testing

Framework: `kotlin.test` + `kotlinx-coroutines-test`. Test files are in `src/commonTest/` and mirror the main source files (`FunExtTest`, `TExtTest`, `AnyExtTest`). Suspend functions are tested with `runTest {}`.
