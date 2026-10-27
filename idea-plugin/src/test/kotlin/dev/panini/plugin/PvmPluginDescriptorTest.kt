package dev.panini.plugin

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmPluginDescriptorTest {
    @Test
    fun `descriptor declares Java 21 compatible IDE baseline and language dependency`() {
        val descriptor = DocumentBuilderFactory.newInstance().newDocumentBuilder()
            .parse(File("idea-plugin/src/main/resources/META-INF/plugin.xml"))

        assertEquals("0.1.0", descriptor.getElementsByTagName("version").item(0).textContent.trim())
        assertEquals(
            "242",
            descriptor.getElementsByTagName("idea-version").item(0).attributes
                .getNamedItem("since-build").nodeValue,
        )
        val dependencies = descriptor.getElementsByTagName("depends")
            .let { nodes -> (0 until nodes.length).map { nodes.item(it).textContent.trim() } }
        assertTrue("com.intellij.modules.platform" in dependencies)
        assertTrue("com.intellij.modules.lang" in dependencies)
    }
}
