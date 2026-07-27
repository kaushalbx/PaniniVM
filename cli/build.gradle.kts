plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":ashtadhyayi"))
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":sankhya"))
    implementation(project(":actions"))
    implementation(project(":dhatupatha"))
    implementation(project(":compiler"))
    implementation(project(":aryabhatiya"))
    implementation(project(":katapayadi"))
    implementation(project(":bhutasamkhya"))
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("dev.panini.MainKt")
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}

tasks.withType<JavaExec> {
    workingDir = rootDir
}
