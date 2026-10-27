package dev.panini.plugin

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmFoldingBuilderFixtureTest : BasePlatformTestCase() {
    fun testMultilinePrakriyaFoldsWithCanonicalIdentity() {
        val source = """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            प्रथम + अम् द्वितीय + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्चन् + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val file = myFixture.configureByText(PvmFileType.INSTANCE, source)
        val builder = PvmFoldingBuilder()

        val descriptors = builder.buildFoldRegions(file, myFixture.editor.document, false)

        assertEquals(1, descriptors.size)
        val descriptor = descriptors.single()
        assertEquals(source.indexOf("गणित"), descriptor.range.startOffset)
        assertTrue(descriptor.range.endOffset < source.length)
        assertEquals("प्रक्रिया गण + ल्युट् …", builder.getPlaceholderText(descriptor.element))
        assertTrue(!builder.isCollapsedByDefault(descriptor.element))
    }
}
