import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.kotlin.dsl.withType
import java.io.ByteArrayOutputStream

plugins {
    kotlin("multiplatform") version "2.1.20"
    id("com.android.library") version "8.2.2"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("maven-publish")
    id("org.jetbrains.dokka") version "1.9.10"
}

group = "com.github.ktomek"
version = getGitTagVersion()

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvmToolchain(17)

    androidTarget {
        publishLibraryVariants("release")
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
            implementation("app.cash.turbine:turbine:1.2.0")
        }
    }
}

android {
    namespace = "com.github.ktomek.funktional"
    compileSdk = 34
    defaultConfig {
        minSdk = 21
    }
}

tasks.withType<Detekt>().configureEach {
    jvmTarget = "1.8"
}
tasks.withType<DetektCreateBaselineTask>().configureEach {
    jvmTarget = "1.8"
}

tasks.named("check").configure {
    this.setDependsOn(
        this.dependsOn.filterNot {
            it is TaskProvider<*> && it.name == "detekt"
        }
    )
}

detekt {
    toolVersion = "1.23.8"
    config.setFrom(file("config/detekt-config.yml"))
    buildUponDefaultConfig = true
    autoCorrect = true
}

publishing {
    publications.withType<MavenPublication> {
        pom {
            name.set("funKtional")
            url.set("https://github.com/ktomek/funKtional")
        }
    }
}

@Suppress("TooGenericExceptionCaught", "SwallowedException")
fun getGitTagVersion(): String {
    return try {
        val stdout = ByteArrayOutputStream()
        exec {
            commandLine = listOf("git", "describe", "--tags", "--abbrev=0")
            standardOutput = stdout
        }
        stdout.toString().trim().removePrefix("v")
    } catch (_: Exception) {
        "1.0.0"
    }
}
