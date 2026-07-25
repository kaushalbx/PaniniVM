package dev.panini.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class PaniniGradlePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val compileTask = project.tasks.register("compilePanini", CompilePaniniTask::class.java) { task ->
            task.group = "build"
            task.description = "Compiles PāṇiniVM Sanskrit scripts (.pvm) into native JVM bytecode .class files."
            task.sourceDir.set(project.layout.projectDirectory.dir("src/main/pvm"))
            task.outputDir.set(project.layout.buildDirectory.dir("classes/pvm/main"))
        }

        project.tasks.findByName("compileKotlin")?.dependsOn(compileTask)
        project.tasks.findByName("classes")?.dependsOn(compileTask)
    }
}
