plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
//    implementation(project(":parser"))
    implementation(project(":ashtadhyayi"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
