plugins {
    kotlin("jvm")
}

dependencies {
    implementation("org.ow2.asm:asm:9.7")
    implementation("org.ow2.asm:asm-util:9.7")
    implementation(project(":core"))
    implementation(project(":execution"))
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
    compileOnly(gradleApi())
    testImplementation(gradleApi())
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    workingDir = rootDir
}
