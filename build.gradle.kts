plugins {
    kotlin("jvm") version "2.0.21"
    application
}

group = "dev.sanskrit"
version = "0.1.0"

kotlin {
    jvmToolchain(21)
}

application {
    mainClass.set("dev.sanskrit.MainKt")
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
