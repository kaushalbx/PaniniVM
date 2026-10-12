package dev.panini.execution

import java.nio.file.Path
import org.junit.jupiter.api.io.TempDir
import kotlin.test.*

class ProjectSourceIdentityTest {
    @TempDir lateinit var directory: Path

    @Test fun `same size and timestamp edits cannot retain stale procedure bodies`() {
        val root = directory.toFile()
        val entry = root.resolve("main.pvm").apply { writeText("एक + अम् मुद्र् + णिच् + लोट् + सिप् ।") }
        val library = root.resolve("library.pvm")
        val original = "गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        library.writeText(original)
        val timestamp = library.lastModified()
        val length = library.length()
        val loader = PvmProjectLoader()
        val before = loader.loadLibraryRegistry(entry).all().single()
        library.writeText(original.replace("द्वि + अम्", "त्रि + अम्"))
        assertEquals(length, library.length())
        assertTrue(library.setLastModified(timestamp))
        val after = loader.loadLibraryRegistry(entry).all().single()
        assertNotEquals(before.body, after.body)
        assertTrue(after.body.single().text.contains("त्रि + अम्"))
    }

    @Test fun `unreadable libraries fail project loading rather than disappearing`() {
        val root = directory.toFile()
        val entry = root.resolve("main.pvm").apply { writeText("एक + अम् मुद्र् + णिच् + लोट् + सिप् ।") }
        val library = root.resolve("library.pvm").apply { writeText("एक + अम् मुद्र् + णिच् + लोट् + सिप् ।") }
        val cause = java.io.IOException("Read denied by host")
        val loader = PvmProjectLoader(readSource = { throw cause })
        val failure = assertFailsWith<IllegalArgumentException> { loader.loadLibraryRegistry(entry) }
        assertTrue(failure.message.orEmpty().contains(library.canonicalPath))
        assertSame(cause, failure.cause)
    }

    @Test fun `nested libraries with equal basenames retain distinct source identities`() {
        val root = directory.toFile()
        val entry = root.resolve("main.pvm").apply { writeText("एक + अम् मुद्र् + णिच् + लोट् + सिप् ।") }
        fun library(folder: String, name: String) {
            val file = root.resolve("$folder/helper.pvm")
            file.parentFile.mkdirs()
            file.writeText("$name + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।\n" +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥")
        }
        library("first", "रहस्य")
        library("second", "परीक्षण")
        library("third", "रहस्य")
        val registry = PvmProjectLoader().loadLibraryRegistry(entry)
        assertEquals(setOf("first/helper.pvm", "second/helper.pvm", "third/helper.pvm"), registry.all().map { it.sourceFile }.toSet())
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val call = parser.parse("रहस्य + टा कृ + लोट् + सिप् ।")
        assertEquals("first/helper.pvm", assertNotNull(registry.detectInvocation(call,
            callerSourceFile = "first/helper.pvm")).kriya.sourceFile)
        assertEquals("third/helper.pvm", assertNotNull(registry.detectInvocation(call,
            callerSourceFile = "third/helper.pvm")).kriya.sourceFile)
        assertNull(registry.detectInvocation(call, callerSourceFile = "second/helper.pvm"))
    }
}
