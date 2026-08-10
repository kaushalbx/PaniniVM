plugins {
    kotlin("jvm")
    application
}

dependencies {
    implementation(project(":ashtadhyayi"))
    implementation(project(":core"))
    implementation(project(":execution"))
    implementation(project(":parser"))
    implementation(project(":sankhya"))
    implementation(project(":actions"))
    implementation(project(":dhatupatha"))
    implementation(project(":compiler"))
    implementation(project(":derivation"))
    implementation(project(":unadipatha"))
    implementation(project(":analysis"))
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
    standardInput = System.`in`
}

tasks.register<JavaExec>("renderExamples") {
    group = "documentation"
    description = "Regenerates readable Sanskrit .txt companions for all example .pvm files."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set(application.mainClass)
    args("--render-readable", rootProject.file("examples").absolutePath)
}
