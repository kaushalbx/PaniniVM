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

tasks.register<JavaExec>("benchmarkCompiler") {
    group = "benchmark"
    description = "Compares PaniniVM interpretation with compiler startup and generated execution."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.panini.compiler.CompilerBenchmark")
    args(providers.gradleProperty("iterations").getOrElse("1000"))
    args(providers.gradleProperty("warmups").getOrElse("100"))
}

tasks.register<JavaExec>("inventoryCompilerBoundaries") {
    group = "verification"
    description = "Reports generic action-runtime boundaries for compilable example programs."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.panini.compiler.CompilerBoundaryInventory")
    args(providers.gradleProperty("examplesDir").getOrElse(rootDir.resolve("examples").path))
}

tasks.register<JavaExec>("macrobenchmarkCompiler") {
    group = "benchmark"
    description = "Runs isolated JVM forks of the compiler macrobenchmark."
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("dev.panini.compiler.CompilerMacrobenchmark")
    args(providers.gradleProperty("forks").getOrElse("5"))
    args(providers.gradleProperty("iterations").getOrElse("1000"))
    args(providers.gradleProperty("warmups").getOrElse("100"))
}
