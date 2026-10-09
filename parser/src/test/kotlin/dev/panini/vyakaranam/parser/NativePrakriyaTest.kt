package dev.panini.vyakaranam.parser

import dev.panini.vyakaranam.ast.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs

class NativePrakriyaTest {
    private val parser = PaniniParser()
    @Test
    fun `nama and iti introduce the same native procedure identity`() {
        for (marker in listOf("नाम", "इति")) {
            val header = "गणन + सुँ $marker प्रक्रिया + सुँ असँ + लट् + तिप्"
            val source = "$header ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
            val document = parser.parseDocument(source)
            val procedure = assertIs<Prakriya>(document.items.single())
            assertEquals("गणन", procedure.nameIdentity)
            assertEquals(1, procedure.body.size)
            val span = document.prakriyaHeaderSpans.getValue(0)
            assertEquals(header, source.substring(span.start, span.endExclusive))
        }
    }

    @Test
    fun `nama procedure still requires nominative name and an asti declaration`() {
        for (header in listOf(
            "गणन + अम् नाम प्रक्रिया + सुँ असँ + लट् + तिप्",
            "गणन + सुँ नाम प्रक्रिया + सुँ असँ + लट् + तस्",
        )) assertFailsWith<IllegalArgumentException> {
            parser.parseDocument("$header ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥")
        }
    }

    @Test
    fun `double danda terminates ordinary passages without creating a procedure`() {
        val first = "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        val second = "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ।"
        val source = "$first\n$second"
        val document = parser.parseDocument(source)
        assertEquals(2, document.items.size)
        document.items.forEach { assertIs<Ukti>(it) }
        assertEquals(listOf(first, second), document.itemSpans.map { source.substring(it.start, it.endExclusive) })
    }

    @Test
    fun `standalone explicit header is a declaration not an executable quotation`() {
        val header = "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप्"
        val document = parser.parseDocument("$header ।")
        val declaration = assertIs<Prakriya>(document.items.single())
        assertEquals(emptyList(), declaration.body)
        assertEquals("प्रयत्न", declaration.nameIdentity)
        val span = document.prakriyaHeaderSpans.getValue(0)
        assertEquals(header, document.sourceText.substring(span.start, span.endExclusive))
    }

    @Test
    fun `document spans preserve leading whitespace multiline text and terminators`() {
        val first = "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।\nएक + अम् मुद्र् + णिच् + लोट् + सिप् ॥"
        val last = "प्रयत्न + टा डुकृञ् + उ + लोट् + सिप्"
        val source = " \n  $first\n\n $last  "
        val document = parser.parseDocument(source)
        assertEquals(2, document.itemSpans.size)
        assertEquals(listOf(first, last), document.itemSpans.map { source.substring(it.start, it.endExclusive) })
    }

    @Test
    fun `whole document retains ordered declarations and utterances on one line`() {
        val source = "गणित + सुँ इति अधिकार + सुँ । " +
            "एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + सुँ । " +
            "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् । " +
            "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥ " +
            "प्रयत्न + टा डुकृञ् + उ + लोट् + सिप् ।"
        val document = parser.parseDocument(source)
        assertEquals(source, document.sourceText)
        assertEquals(4, document.items.size)
        assertIs<Scope>(document.items[0])
        assertIs<RangeDeclaration>(document.items[1])
        assertIs<Prakriya>(document.items[2])
        assertIs<Ukti>(document.items[3])
        assertEquals(document.items, parser.parseDocument(source.replace(" । ", " ।\n")).items)
    }

    @Test
    fun `ordinary quotation before a procedure is not mistaken for its header`() {
        val document = parser.parseDocument(
            "एक + अम् इति मुद्र् + णिच् + लोट् + सिप् । " +
                "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् । " +
                "द्वि + औट् मुद्र् + णिच् + लोट् + सिप् ॥",
        )
        assertEquals(2, document.items.size)
        assertIs<Quotation>(assertIs<Ukti>(document.items.first()).body)
        assertIs<Prakriya>(document.items.last())
        assertEquals(emptyList(), parser.parseDocument(" ").items)
    }

    @Test
    fun `adhikara declaration retains domain identity with lexical or derived marker`() {
        for (marker in listOf("अधिकार", "अधि + कृ + घञ्")) {
            assertEquals("गणित", parser.parseScopeDeclaration("गणित + सुँ\nइति $marker + सुँ ।").domainIdentity)
        }
        assertFailsWith<IllegalArgumentException> {
            parser.parseScopeDeclaration("गणित + अम् इति अधिकार + सुँ ।")
        }
        assertFailsWith<IllegalArgumentException> {
            parser.parseScopeDeclaration("गणित + सुँ इति सूची + सुँ ।")
        }
    }

    @Test
    fun `sima declaration retains native boundary morphology across lines`() {
        val node = parser.parseRangeDeclaration("एक + ङसिँ\nदशन् + शस् परि + अन्त + अम्\nइति सीमा + सुँ ।")
        assertEquals(listOf("एक"), node.boundary.lowerLimit.stems)
        assertEquals(listOf("दशन्"), node.boundary.upperLimit.stems)
        assertEquals(MulaPratipadikaIdentity.SIMA, (node.marker.pratipadika as MulaPratipadika).lexicalIdentity)
        for (source in listOf("एक + ङसिँ दशन् + ङि इति सीमा + सुँ ।",
            "एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सीमा + अम् ।",
            "एक + ङसिँ दशन् + शस् परि + अन्त + अम् इति सूची + सुँ ।")) {
            assertFailsWith<IllegalArgumentException> { parser.parseRangeDeclaration(source) }
        }
    }

    @Test
    fun `native body retains typed signature statements and bounded loop`() {
        val node = parser.parsePrakriya("""
            प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            मान + सुँ सङ्ख्या + सुँ इति मान + सुँ ।
            सङ्ख्या + सुँ इति परिणाम + सुँ ।
            पञ्चन् + कृत्वसुच् यावत् फल + सुँ असँ + लट् + तिप्
            तावत् एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥
        """.trimIndent())
        assertEquals(3, node.body.size)
        assertIs<NamaVakya>(assertIs<Invocation>(node.body.first()).vakya)
        val loop = assertIs<WhileLoop>(node.body.last())
        assertEquals(listOf("पञ्चन्"), loop.maximumIterationStems)
    }

    @Test
    fun `multiline declaration and body build native prakriya`() {
        val source = """
            गणित + ङस् वृध् + ल्युट् + सुँ
            इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            एक + अम्
            मुद्र् + णिच् + लोट् + सिप् ।
            यदि फल + सुँ असँ + लट् + तिप् तर्हि विजय अन्यथा पराजय ॥
        """.trimIndent()
        val node = parser.parsePrakriya(source)
        assertEquals(source, node.sourceText)
        assertEquals("वृध् + ल्युट्", node.nameIdentity)
        assertEquals("गणित", node.domainIdentity)
        assertEquals(2, node.body.size)
        assertIs<Invocation>(node.body.first())
        assertIs<Conditional>(node.body.last())
        assertEquals(node.body, parser.parsePrakriya(source.replace('\n', ' ')).body)
    }

    @Test
    fun `nominal name and internal qualifier retain typed identity`() {
        val node = parser.parsePrakriya(
            "प्रयत्न + सुँ इति अन्तरङ्ग + टाप् + सुँ प्रक्रिया + सुँ असँ + लट् + तिप् । " +
                "एक + अम् मुद्र् + णिच् + लोट् + सिप् ॥",
        )
        assertEquals("प्रयत्न", node.nameIdentity)
        assertEquals(PrakriyaVisibility.INTERNAL, node.modifiers.visibility)
        assertEquals(PrakriyaPrecedence.ANTARANGA, node.modifiers.precedence)
    }

    @Test
    fun `invalid block structure and non declarations are rejected`() {
        val header = "प्रयत्न + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।"
        val body = "एक + अम् मुद्र् + णिच् + लोट् + सिप्"
        for (source in listOf("$header $body ।", "$header ॥", "$header $body ॥ $body ।",
            "प्रयत्न + सुँ । $body ॥", header.replace("प्रक्रिया", "संज्ञा") + " $body ॥")) {
            assertFailsWith<IllegalArgumentException>(source) { parser.parsePrakriya(source) }
        }
    }
}
