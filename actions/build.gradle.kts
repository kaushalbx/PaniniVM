plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    testImplementation(project(":dhatupatha"))
    testImplementation(project(":ashtadhyayi"))
    testImplementation(project(":derivation"))
    testImplementation(project(":analysis"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
    // Tests temporarily replace the process-wide Sanskrit number renderer.
    // Keep this module serial until the renderer becomes context-scoped.
    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
}
