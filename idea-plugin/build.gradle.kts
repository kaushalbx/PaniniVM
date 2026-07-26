plugins {
    kotlin("jvm")
}

dependencies {
    compileOnly("com.jetbrains.intellij.platform:core:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:core-impl:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:util:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:util-ui:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:analysis:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:editor:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:lang:241.14494.240")
    compileOnly("com.jetbrains.intellij.platform:execution:241.14494.240")
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":dhatupatha"))
    implementation(project(":ganapatha"))
    implementation(project(":compiler"))
    implementation(project(":cli"))
    implementation(project(":ashtadhyayi"))
    testImplementation(kotlin("test"))
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from({
        configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) }
    })
}

tasks.withType<Test> {
    useJUnitPlatform()
}
