plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":ashtadhyayi"))
    implementation(project(":derivation"))
    implementation(project(":unadipatha"))
    implementation(project(":analysis"))
    implementation(project(":sankhya"))
    implementation(project(":katapayadi"))
    implementation(project(":aryabhatiya"))
    implementation(project(":bhutasamkhya"))
    implementation(project(":actions"))
    implementation(project(":dhatupatha"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
