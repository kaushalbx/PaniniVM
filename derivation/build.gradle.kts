plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":dhatupatha"))
    implementation(project(":unadipatha"))
    implementation(project(":ashtadhyayi"))
    implementation(project(":actions"))
    implementation(project(":analysis"))
    implementation(project(":linganushasanam"))
    implementation(project(":ganapatha"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
    // Several corpus/provenance fixtures retain complete derivation traces.
    // Concurrent classes and methods multiply the same source-backed indexes
    // and can exhaust the fixed test-worker heap.
    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
}
