plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":ganapatha"))
    implementation(project(":ashtadhyayi"))
    implementation(project(":sankhya"))
    implementation(project(":actions"))
    testImplementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
