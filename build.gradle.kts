plugins {
    kotlin("jvm") version "2.0.21"
}

group = "dev.panini"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

dependencies {
    implementation("org.ow2.asm:asm:9.7")
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":ganapatha"))
    compileOnly(gradleApi())
    testImplementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    forkEvery = 0          // reuse JVM across test classes (faster startup)
    maxHeapSize = "512m"
}

subprojects {
    tasks.withType<Test> {
        systemProperties(
            "junit.jupiter.execution.parallel.enabled" to "true",
            "junit.jupiter.execution.parallel.mode.default" to "concurrent",
            "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent"
        )
    }
}

