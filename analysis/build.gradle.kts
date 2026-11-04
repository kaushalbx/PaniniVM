plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":ashtadhyayi"))
    testImplementation(kotlin("test"))
    testImplementation("com.google.code.gson:gson:2.11.0")
    testImplementation(project(":parser"))
    testImplementation(project(":dhatupatha"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
