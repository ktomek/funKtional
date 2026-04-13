import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.kotlin.dsl.withType

plugins {
    kotlin("multiplatform") version "2.1.20"
    id("com.android.kotlin.multiplatform.library") version "9.1.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("maven-publish")
    id("org.jetbrains.dokka") version "2.2.0"
}

group = "com.github.ktomek"
version = getGitTagVersion()

repositories {
    mavenCentral()
    google()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

kotlin {
    jvmToolchain(17)

    android {
        namespace = "com.github.ktomek.funktional"
        compileSdk = 36
        minSdk = 21
    }

    jvm()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
            implementation("app.cash.turbine:turbine:1.2.1")
        }
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
    source.setFrom("src/commonMain/kotlin")
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
        ProcessBuilder("git", "describe", "--tags", "--abbrev=0")
            .redirectErrorStream(true)
            .start()
            .inputStream.bufferedReader().readText().trim().removePrefix("v")
    } catch (_: Exception) {
        "1.0.0"
    }
}
