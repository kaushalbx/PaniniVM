package dev.panini.plugin

import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmSymbolResolverTest {
    @Test
    fun `semantic spans retain canonical declaration and pipeline categories`() {
        val source = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        fun rendered(spans: List<dev.panini.vyakaranam.ast.DocumentSourceSpan>) =
            spans.map { source.substring(it.start, it.endExclusive) }

        assertEquals(listOf("गण + ल्युट् + सुँ"), rendered(PvmSymbolResolver.prakriyaDeclarationSpans(document)))
        assertEquals(listOf("गणित + सुँ"), rendered(PvmSymbolResolver.domainDeclarationSpans(document)))
        assertTrue(rendered(PvmSymbolResolver.prakriyaReferenceSpans(document)).all { it == "गण + ल्युट्" })
        assertTrue(rendered(PvmSymbolResolver.domainReferenceSpans(document)).all { it == "गणित" })
    }

    @Test
    fun `pipeline operation navigates to matching canonical prakriya declaration`() {
        val source = """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        val referenceOffset = source.lastIndexOf("गण + ल्युट्")

        val targets = PvmSymbolResolver.prakriyaDeclarationOffsets(document, referenceOffset)

        assertEquals(listOf(source.indexOf("गण + ल्युट्")), targets)
    }

    @Test
    fun `pipeline domain disambiguates declarations with the same operation identity`() {
        val source = """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            पाठ + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा ततः गणित + ङस् गण + ल्युट् + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        val referenceOffset = source.lastIndexOf("गण + ल्युट् + टा")

        val targets = PvmSymbolResolver.prakriyaDeclarationOffsets(document, referenceOffset)

        assertEquals(1, targets.size)
        assertTrue(targets.single() < source.indexOf("पाठ + ङस्"))
    }

    @Test
    fun `ordinary operands do not resolve as procedure declarations`() {
        val source = """
            गणित + ङस् परीक्षण + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            परीक्षण + अम् मुद्र् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)

        assertTrue(
            PvmSymbolResolver.prakriyaReferenceIdentityAt(
                document, source.lastIndexOf("परीक्षण"), setOf("परीक्षण"),
            ) == null,
        )
    }

    @Test
    fun `runtime invocation matcher supplies identity for an ordinary prakriya call`() {
        val source = """
            गणित + ङस् गण + ल्युट् + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            पञ्च + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ॥
            पञ्च + अम् द्वि + अम् च गणित + ङस् गण + ल्युट् + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)

        assertEquals(
            "गण + ल्युट्" to "गणित",
            PvmSymbolResolver.prakriyaReferenceIdentityAt(
                document, source.lastIndexOf("गण + ल्युट्"), setOf("गण + ल्युट्"),
            ),
        )
    }

    @Test
    fun `declaration lookup uses active canonical domain and enforces cross file visibility`() {
        val source = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ ।
            गण + ल्युट् + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)

        assertEquals(
            1,
            PvmSymbolResolver.prakriyaDeclarationOffsets(
                document, "गण + ल्युट्", "गणित", includeInternal = true,
            ).size,
        )
        assertTrue(
            PvmSymbolResolver.prakriyaDeclarationOffsets(
                document, "गण + ल्युट्", "गणित", includeInternal = false,
            ).isEmpty(),
        )
    }
}
