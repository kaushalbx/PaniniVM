plugins {
    kotlin("jvm") version "2.0.21"
    antlr
    id("com.strumenta.antlr-kotlin") version "1.0.0-RC4"
    application
}

val generatedAntlrDirectory = layout.buildDirectory.dir("generated-src/antlr/main")

val generateVyakaranamLexer by tasks.registering(AntlrTask::class) {
    source = fileTree("src/main/antlr") {
        include("dev/panini/vyakaranam/VyakaranamLexer.g4")
    }
    outputDirectory = generatedAntlrDirectory.get().asFile
    arguments = arguments + listOf("-Dlanguage=Kotlin")
    doFirst {
        generatedAntlrDirectory.get().asFile.resolve("dev/panini/parser").mkdirs()
    }
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
    antlr("com.strumenta:antlr-kotlin-target:1.0.0-RC4")
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation("org.ow2.asm:asm:9.7")
    compileOnly(gradleApi())
    testImplementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.generateGrammarSource {
    dependsOn(generateVyakaranamLexer)
    outputDirectory = generatedAntlrDirectory.get().asFile
    source = fileTree("src/main/antlr") {
        exclude("dev/panini/vyakaranam/VyakaranamLexer.g4")
    }
    maxHeapSize = "64m"
    arguments = arguments + listOf(
        "-Dlanguage=Kotlin",
        "-visitor",
        "-listener",
        "-lib",
        generatedAntlrDirectory.get().asFile.resolve("dev/panini/parser").absolutePath,
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
