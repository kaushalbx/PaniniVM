plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ganapatha"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
