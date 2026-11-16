plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.ow2.asm:asm:9.7")
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":ganapatha"))
    implementation(project(":unadipatha"))
    implementation(project(":actions"))
    implementation(project(":dhatupatha"))
    compileOnly(gradleApi())
    testImplementation(gradleApi())
    testImplementation(project(":derivation"))
    testImplementation(project(":analysis"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    // Gradle already runs other modules' tests alongside; a few forks with a fixed JUnit pool keep cores busy without thrashing.
    maxParallelForks = 3
    forkEvery = 0
    maxHeapSize = "1g"
    systemProperties(
        "junit.jupiter.execution.parallel.enabled" to "true",
        "junit.jupiter.execution.parallel.mode.default" to "concurrent",
        "junit.jupiter.execution.parallel.mode.classes.default" to "concurrent"
    )
}
