plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ashtadhyayi"))
    implementation(project(":unadipatha"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
