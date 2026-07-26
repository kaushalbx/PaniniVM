plugins {
    kotlin("jvm")
    antlr
    id("com.strumenta.antlr-kotlin") version "1.0.0-RC4"
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

dependencies {
    antlr("com.strumenta:antlr-kotlin-target:1.0.0-RC4")
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
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

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
