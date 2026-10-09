plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ashtadhyayi"))
    testImplementation(kotlin("test"))
    testImplementation(project(":parser"))
    testImplementation(project(":dhatupatha"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
