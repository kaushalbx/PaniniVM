package dev.panini.plugin

import dev.panini.vyakaranam.parser.PaniniParser
import dev.panini.execution.PrakriyaRegistry
import dev.panini.execution.PvmScript
import dev.panini.execution.registerDeclarations
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PvmInlayHintsTest {
    @Test
    fun `hints use canonical ukti spans and skip declarations`() {
        val source = """
            गणित + सुँ इति अधि + कृ + घञ् + सुँ असँ + लट् + तिप् ।
            पञ्चन् + अम् द्वि + अम् च युज् + णिच् + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)

        val hints = PvmInlayHints.compute(source, document)

        assertEquals(1, hints.size)
        assertEquals(source.length, hints.single().offset)
        assertTrue(hints.single().text.isNotBlank())
        assertEquals(PvmSyntaxHighlighter.INLAY_SURFACE, hints.single().attributesKey)
    }

    @Test
    fun `canceled computation yields no partial hints`() {
        val source = "पञ्चन् + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)

        assertTrue(PvmInlayHints.compute(source, document, isCanceled = { true }).isEmpty())
    }

    @Test
    fun `positional kriya arguments show resolved canonical parameter names`() {
        val source = """
            योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            दक्षिण + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            द्वि + अम् त्रि + अम् योजन + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        val registry = PrakriyaRegistry().apply {
            registerDeclarations(PvmScript.fromDocument(document), "fixture.pvm")
        }

        val callStart = source.lastIndexOf("द्वि + अम्")
        val hints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
            .filter { (offset, text) -> offset >= callStart && (text.endsWith(":") || text.endsWith(" =")) }
            .map { it.offset to it.text }
        assertEquals(
            listOf(
                callStart to "वाम:",
                source.indexOf("त्रि + अम्", callStart) to "दक्षिण:",
                source.indexOf("योजन + टा", callStart) to "करण =",
                source.indexOf("कृ + लोट्", callStart) to "क्रिया =",
            ),
            hints,
        )
        assertEquals(
            PvmSyntaxHighlighter.INLAY_PARAMETER,
            PvmInlayHints.compute(source, document, registry, "fixture.pvm")
                .single { it.text == "वाम:" }.attributesKey,
        )
    }

    @Test
    fun `explicit named arguments do not receive redundant parameter hints`() {
        val source = """
            योजन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।
            वाम + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            दक्षिण + सुँ नाम मान + सुँ सङ्ख्या + सुँ असँ + लट् + तिप् ।
            वाम + अम् दक्षिण + अम् च युज् + णिच् + लोट् + सिप् ॥

            दक्षिण + ङि त्रि + अम् वाम + ङि द्वि + अम् योजन + टा कृ + लोट् + सिप् ।
        """.trimIndent()
        val document = PaniniParser().parseDocument(source)
        val registry = PrakriyaRegistry().apply {
            registerDeclarations(PvmScript.fromDocument(document), "fixture.pvm")
        }

        val callStart = source.lastIndexOf("दक्षिण + ङि")
        val parameterHints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
            .filter { (offset, text) -> offset >= callStart && (text.endsWith(":") || text.endsWith(" =")) }
            .map { it.offset to it.text }

        assertEquals(
            listOf(
                callStart to "अधिकरण =",
                source.indexOf("त्रि + अम्", callStart) to "कर्मन् =",
                source.indexOf("वाम + ङि", callStart) to "अधिकरण =",
                source.indexOf("द्वि + अम्", callStart) to "कर्मन् =",
                source.indexOf("योजन + टा", callStart) to "करण =",
                source.indexOf("कृ + लोट्", callStart) to "क्रिया =",
            ),
            parameterHints,
        )
    }

    @Test
    fun `ordinary kriya participants show resolved karaka names`() {
        val source = "राम + सुँ फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)
        val registry = PrakriyaRegistry()

        val roleHints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
            .filter { (_, text) -> text.endsWith(" =") }
            .map { it.offset to it.text }

        assertEquals(
            listOf(
                source.indexOf("राम") to "कर्तृ =",
                source.indexOf("फल") to "कर्मन् =",
                source.indexOf("मुद्र्") to "क्रिया =",
            ),
            roleHints,
        )
        val typedHints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
        assertEquals(
            DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT,
            typedHints.single { it.text == "कर्तृ =" }.attributesKey,
        )
        assertEquals(
            DefaultLanguageHighlighterColors.INLINE_PARAMETER_HINT,
            typedHints.single { it.text == "क्रिया =" }.attributesKey,
        )
        assertTrue(typedHints.filter { it.text in setOf("कर्तृ =", "कर्मन् =", "क्रिया =") }
            .all { it.text.endsWith(" =") })
    }

    @Test
    fun `live inlay computation accepts shared canonical grammar analysis`() {
        val source = "राम + सुँ फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)
        val ukti = document.items.single() as dev.panini.vyakaranam.ast.Ukti
        val analysis = requireNotNull(VyakaranamExecutionAdapter.analyzeGrammar(ukti))

        val hints = PvmInlayHints.compute(
            source,
            document,
            PrakriyaRegistry(),
            "fixture.pvm",
            listOf(analysis),
        )

        assertTrue(hints.any { it.text == "कर्तृ =" })
        assertTrue(hints.any { it.text == "कर्मन् =" })
        assertTrue(hints.any { it.text == "क्रिया =" })
    }

    @Test
    fun `syncretic pada never presents candidates as a resolved karaka`() {
        val source = "देव + भ्याम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)

        val roleHints = PvmInlayHints.compute(
            source,
            document,
            PrakriyaRegistry(),
            "fixture.pvm",
        ).filter { (_, text) -> text.endsWith(" =") }
            .map { it.offset to it.text }

        assertEquals(
            listOf(
                source.indexOf("मुद्र्") to "क्रिया =",
            ),
            roleHints,
        )
    }

    @Test
    fun `resolved karaka explanation retains ashtadhyayi evidence`() {
        val source = "राम + सुँ फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)

        val karman = PvmInlayHints.compute(
            source,
            document,
            PrakriyaRegistry(),
            "fixture.pvm",
        ).single { it.text == "कर्मन् =" }

        assertTrue(karman.explanation.orEmpty().contains("पदम्: फल + अम्"))
        assertTrue(karman.explanation.orEmpty().contains("अष्टाध्यायी-प्रमाणम्:"))
        assertTrue(karman.explanation.orEmpty().contains("1.4.49"))
    }

    @Test
    fun `a named procedure header shows its roles`() {
        val header = "परीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।"
        for (source in listOf(header, "$header\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥")) {
            val document = PaniniParser().parseDocument(source)
            val registry = PrakriyaRegistry().also { it.registerDeclarations(PvmScript.fromDocument(document), "fixture.pvm") }
            val hints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
            assertTrue(hints.any { it.offset == 0 && it.text == "कर्तृ-समानाधिकरण: सञ्ज्ञा =" })
            assertTrue(hints.any { it.offset == source.indexOf("प्रक्रिया") && it.text.startsWith("कर्तृ") })
        }
    }

    @Test
    fun `comments get no hints`() {
        val comment = "# गणित + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।"
        val inner = "# द्वि + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val source = "$comment\nपरीक्षण + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तिप् ।\n$inner\nद्वि + अम् मुद्र् + णिच् + लोट् + सिप् ॥\n$comment\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)
        val registry = PrakriyaRegistry().also { it.registerDeclarations(PvmScript.fromDocument(document), "fixture.pvm") }
        val hints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
        val commentRanges = Regex("#[^\n]*").findAll(source).map { it.range }.toList()
        assertTrue(hints.none { hint -> commentRanges.any { hint.offset in it } }, hints.map { it.offset to it.text }.toString())
        assertTrue(hints.any { it.offset == source.indexOf("परीक्षण") })
    }

    @Test
    fun `a repetition count qualifies the action`() {
        val source = "षष् + कृत्वसुच् एक + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val document = PaniniParser().parseDocument(source)
        val registry = PrakriyaRegistry().also { it.registerDeclarations(PvmScript.fromDocument(document), "fixture.pvm") }
        val hints = PvmInlayHints.compute(source, document, registry, "fixture.pvm")
        assertTrue(hints.any { it.offset == 0 && it.text == "क्रियाविशेषण =" }, hints.map { it.offset to it.text }.toString())
    }
}
