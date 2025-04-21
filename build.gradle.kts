import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.kotlin.dsl.withType
import java.io.ByteArrayOutputStream

plugins {
    kotlin("jvm") version "2.1.20"
    jacoco
    id("io.gitlab.arturbosch.detekt") version ("1.23.8")
}

group = "com.github.ktomek.funktional"
version = getGitTagVersion()

repositories {
    mavenCentral()
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
    testImplementation("io.mockk:mockk:1.13.10")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")
    testImplementation("app.cash.turbine:turbine:1.2.0")

    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}


tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

// Kotlin DSL
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
        "1.0.0" // fallback if no tag found
    }
}
