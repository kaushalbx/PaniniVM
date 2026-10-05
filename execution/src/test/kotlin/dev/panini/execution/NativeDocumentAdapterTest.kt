package dev.panini.execution

import dev.panini.vyakaranam.ast.Prakriya
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertSame
import kotlin.test.assertFailsWith

class NativeDocumentAdapterTest {
    @Test
    fun `operation name source excludes domain and retains multiline morphology`() {
        val name = "हृ +\n ल्युट् + सुँ"
        val header = "गणित + ङस् $name इति प्रक्रिया + सुँ असँ + लट् + तिप्"
        for (source in listOf("$header ।", "$header ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥")) {
            val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(PvmScript.parseNative(source).single())
            assertEquals(name, definition.nameSegmented)
            assertEquals("गणित", definition.prakriya.domainIdentity)
            assertEquals("हृ + ल्युट्", definition.prakriya.nameIdentity)
        }
    }

    @Test
    fun `standalone declarations retain qualifiers identity and source with optional final danda`() {
        val header = "गणित + ङस् प्रयत्न + सुँ इति नित्य + सुँ प्रक्रिया + सुँ असँ + लट् + तिप्"
        for (source in listOf(header, "$header ।")) {
            val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(PvmScript.parseNative(source).single())
            assertEquals(source, definition.text)
            assertEquals(header, definition.headerSource)
            assertEquals("गणित", definition.prakriya.domainIdentity)
            assertEquals("प्रयत्न", definition.prakriya.nameIdentity)
            assertEquals(dev.panini.vyakaranam.ast.PrakriyaPrecedence.NITYA, definition.prakriya.modifiers.precedence)
            assertEquals(emptyList(), definition.body)
        }
        assertFailsWith<IllegalArgumentException> {
            PvmScript.parseNative("प्रयत्न + अम् इति प्रक्रिया + सुँ असँ + लट् + तिप् ।")
        }
    }

    @Test
    fun `native body spans preserve multiline comments and each statement terminator`() {
        val first = "मान + सुँ # parameter . ।\r\nसङ्ख्या + सुँ इति मान + सुँ ।"
        val last = "मान + अम्\r\nमुद्र् + णिच् + लोट् + सिप् ॥"
        val source = "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\r\n$first\r\n$last"
        val document = PaniniParser().parseDocument(source)
        assertEquals(listOf(first, last), document.prakriyaBodySpans.getValue(0).map {
            source.substring(it.start, it.endExclusive)
        })
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(PvmScript.fromDocument(document).single())
        assertEquals(listOf(first, last), definition.body.map { it.text })
        assertEquals(PrakriyaValueType.SANKHYA, PrakriyaSignatureDeclarationParser.parameter(definition.body.first())?.type)
    }

    @Test
    fun `native comments preserve CRLF offsets and do not truncate header at comment punctuation`() {
        val header = "प्रयत्न + सुँ # comment । .\r\nइति प्रक्रिया + सुँ असँ + लट् + तिप्"
        val source = "# preface\r\n  $header ।\r\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        val document = PaniniParser().parseDocument(source)
        val span = document.itemSpans.single()
        assertEquals(source.indexOf("प्रयत्न"), span.start)
        assertEquals(source.length, span.endExclusive)
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(PvmScript.parseNative(source).single())
        assertEquals(header, definition.headerSource)
        assertEquals(source.substring(span.start), definition.text)
        assertEquals("प्रयत्न", definition.prakriya.nameIdentity)
    }

    @Test
    fun `native document projects declarations and body ASTs without reparsing`() {
        val source = """
            गणित + सुँ इति अधिकार + सुँ ।
            एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ ।
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            मान + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            पञ्चन् + अम् प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        val statements = PvmScript.fromDocument(document)
        assertIs<PvmScriptStatement.AdhikaraDefinition>(statements[0])
        val range = assertIs<PvmScriptStatement.RangeDefinition>(statements[1]).range
        assertEquals(1L, range.minimum.value)
        assertEquals(10L, range.maximum.value)
        val definition = assertIs<PvmScriptStatement.PrakriyaDefinition>(statements[2])
        val original = assertIs<Prakriya>(document.items[2])
        assertSame(original.body.last(), definition.body.last().program)
        assertEquals("प्रयत्न", definition.prakriya.nameIdentity)
        assertEquals("प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप्", definition.headerSource)
        assertEquals(PrakriyaValueType.SANKHYA, PrakriyaSignatureDeclarationParser.parameter(definition.body.first())?.type)
        assertEquals("पञ्चन् + अम् प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।",
            assertIs<PvmScriptStatement.Sentence>(statements[3]).text)
    }

    @Test
    fun `native parsing handles comments and rejects descending range`() {
        assertEquals(1, PvmScript.parseNative("# comment\nएक + अम्\nमुद्र् + णिच् + लोट् + सिप् । // comment").size)
        assertFailsWith<IllegalArgumentException> {
            PvmScript.parseNative("दशन् + ङसिँ एक + अम् परि + अन्त + अम् इति सीमा + सुँ ।")
        }
    }
}
