plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    useJUnitPlatform()
}
