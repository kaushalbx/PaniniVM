package dev.panini.gradle

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class PaniniGradlePluginTest {

    @Test
    fun `CompilePaniniTask compiles pvm files into bytecode class files`() {
        val projectDir = File(System.getProperty("java.io.tmpdir"), "panini_gradle_test_" + System.currentTimeMillis())
        val srcPvmDir = File(projectDir, "src/main/pvm")
        srcPvmDir.mkdirs()

        val sampleScript = File(srcPvmDir, "math.pvm")
        sampleScript.writeText("एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ।\n")

        val outDir = File(projectDir, "build/classes/pvm/main")
        CompilePaniniTask.compilePaniniFiles(srcPvmDir, outDir)

        val compiledClass = File(outDir, "MathProgram.class")
        assertTrue(compiledClass.exists())
        assertTrue(compiledClass.length() > 0)

        projectDir.deleteRecursively()
    }
}
