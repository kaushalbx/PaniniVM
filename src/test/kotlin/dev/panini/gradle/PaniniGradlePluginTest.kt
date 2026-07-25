package dev.panini.gradle

import org.gradle.testfixtures.ProjectBuilder
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PaniniGradlePluginTest {

    @Test
    fun `PaniniGradlePlugin registers compilePanini task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply(PaniniGradlePlugin::class.java)

        val task = project.tasks.findByName("compilePanini")
        assertNotNull(task)
        assertTrue(task is CompilePaniniTask)
        assertEquals("build", task.group)
    }

    @Test
    fun `CompilePaniniTask compiles pvm files into bytecode class files`() {
        val projectDir = File(System.getProperty("java.io.tmpdir"), "panini_gradle_test_" + System.currentTimeMillis())
        val srcPvmDir = File(projectDir, "src/main/pvm")
        srcPvmDir.mkdirs()

        val sampleScript = File(srcPvmDir, "math.pvm")
        sampleScript.writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n")

        val project = ProjectBuilder.builder().withProjectDir(projectDir).build()
        project.plugins.apply(PaniniGradlePlugin::class.java)

        val task = project.tasks.getByName("compilePanini") as CompilePaniniTask
        task.compile()

        val outDir = task.outputDir.get().asFile
        val compiledClass = File(outDir, "MathProgram.class")
        assertTrue(compiledClass.exists())
        assertTrue(compiledClass.length() > 0)

        projectDir.deleteRecursively()
    }
}
