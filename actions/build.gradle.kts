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
}
