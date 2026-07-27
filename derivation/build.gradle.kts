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
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
