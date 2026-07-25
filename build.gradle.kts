plugins {
    kotlin("jvm") version "2.0.21"
    antlr
    application
}

val generatedAntlrDirectory = layout.buildDirectory.dir("generated-src/antlr/main")

/*
 * A parser grammar using tokenVocab must see the lexer's .tokens file before
 * ANTLR processes it. Gradle otherwise submits both nested grammar files in a
 * single invocation, whose input order is not guaranteed.
 */
val generateVyakaranamLexer by tasks.registering(AntlrTask::class) {
    source = fileTree("src/main/antlr") {
        include("dev/panini/vyakaranam/VyakaranamLexer.g4")
    }
    outputDirectory = generatedAntlrDirectory.get().asFile
}

group = "dev.panini"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("dev.panini.MainKt")
}

dependencies {
    antlr("org.antlr:antlr4:4.13.2")
    implementation("org.antlr:antlr4-runtime:4.13.2")
    implementation("org.ow2.asm:asm:9.7")
    compileOnly(gradleApi())
    testImplementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.generateGrammarSource {
    dependsOn(generateVyakaranamLexer)
    source = fileTree("src/main/antlr") {
        exclude("dev/panini/vyakaranam/VyakaranamLexer.g4")
    }
    maxHeapSize = "64m"
    arguments = arguments + listOf(
        "-visitor",
        "-listener",
        "-lib",
        generatedAntlrDirectory.get().asFile.absolutePath,
    )
}

tasks.compileKotlin {
    dependsOn(tasks.generateGrammarSource)
}

tasks.compileTestKotlin {
    dependsOn(tasks.generateTestGrammarSource)
}

tasks.test {
    useJUnitPlatform()
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    forkEvery = 0          // reuse JVM across test classes (faster startup)
    maxHeapSize = "512m"
}

