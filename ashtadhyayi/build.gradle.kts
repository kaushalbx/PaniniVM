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
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
    forkEvery = 0
    maxHeapSize = "512m"
}
