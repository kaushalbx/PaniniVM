plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":"))
    implementation(project(":core"))
    implementation(project(":parser"))
    implementation(project(":sankhya"))
    implementation(project(":compiler"))
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
