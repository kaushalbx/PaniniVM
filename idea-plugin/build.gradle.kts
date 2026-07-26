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
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
    implementation(project(":parser"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
