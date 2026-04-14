import com.vanniktech.maven.publish.SonatypeHost
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask
import org.gradle.kotlin.dsl.withType

plugins {
    kotlin("multiplatform") version "2.1.20"
    id("io.gitlab.arturbosch.detekt") version "1.23.8"
    id("com.vanniktech.maven.publish") version "0.30.0"
    id("org.jetbrains.dokka") version "2.2.0"
}

group = "io.github.ktomek"
version = project.findProperty("publishVersion") as String? ?: getGitTagVersion()

repositories {
    mavenCentral()
}

dependencies {
    detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.8")
}

kotlin {
    jvmToolchain(17)

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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)
    signAllPublications()

    coordinates("io.github.ktomek", "funKtional", version.toString())

    pom {
        name.set("funKtional")
        description.set("Lightweight Kotlin Multiplatform functional programming extensions for nullable handling, type casting, and coroutine-friendly utilities")
        url.set("https://github.com/ktomek/funKtional")
        licenses {
            license {
                name.set("MIT License")
                url.set("https://opensource.org/licenses/MIT")
            }
        }
        developers {
            developer {
                id.set("ktomek")
                name.set("ktomek")
                url.set("https://github.com/ktomek")
            }
        }
        scm {
            url.set("https://github.com/ktomek/funKtional")
            connection.set("scm:git:git://github.com/ktomek/funKtional.git")
            developerConnection.set("scm:git:ssh://git@github.com/ktomek/funKtional.git")
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
