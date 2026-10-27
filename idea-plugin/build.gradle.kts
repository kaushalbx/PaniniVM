plugins {
    kotlin("jvm")
    id("org.jetbrains.intellij.platform")
}

version = rootProject.version

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.2.4")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
    }
    implementation("com.strumenta:antlr-kotlin-runtime:1.0.0-RC4")
    implementation(project(":core"))
    implementation(project(":execution"))
    implementation(project(":parser"))
    implementation(project(":dhatupatha"))
    implementation(project(":ganapatha"))
    implementation(project(":compiler"))
    implementation(project(":cli"))
    implementation(project(":ashtadhyayi"))
    implementation(project(":derivation"))
    implementation(project(":unadipatha"))
    implementation(project(":linganushasanam"))
    implementation(project(":analysis"))
    implementation(project(":aryabhatiya"))
    implementation(project(":katapayadi"))
    implementation(project(":bhutasamkhya"))
    testImplementation(kotlin("test"))
    testImplementation("junit:junit:4.13.2")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine:5.11.4")
}

intellijPlatform {
    pluginConfiguration {
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "242"
        }
    }
}

sourceSets.test {
    resources.srcDir(rootProject.file("cli/examples"))
}

tasks.withType<Test> {
    useJUnitPlatform()
    maxParallelForks = 1
    systemProperty("junit.jupiter.execution.parallel.enabled", "false")
    workingDir = rootDir
    systemProperty("panini.idea.test.classpath", sourceSets.main.get().runtimeClasspath.asPath)
}
