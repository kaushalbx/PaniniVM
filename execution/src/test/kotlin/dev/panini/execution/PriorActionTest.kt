package dev.panini.execution

import dev.panini.analysis.FrameDiagnosticCode
import dev.panini.analysis.PadaAnalyzer
import dev.panini.analysis.VakyaAnalyzer
import dev.panini.dhatupatha.curadi.MudrDhatu
import dev.panini.dhatupatha.rudhadi.YujirDhatu
import dev.panini.vyakaranam.ast.*
import dev.panini.vyakaranam.lexicon.InMemoryVyakaranamLexicon
import dev.panini.vyakaranam.parser.PaniniParser
import kotlin.test.*

class PriorActionTest {
    @Test
    fun `objectless display resolves the preceding prior action result`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).value)
        val sequence = assertIs<Sequence>(PriorActionLowering.lower(PaniniParser().parse(source).body))
        val main = assertIs<Invocation>(sequence.statements.last())
        assertTrue(main.vakya.padas.filterIsInstance<SubantaPada>().any(NaturalSemanticNormalizer::isPriorResult))
    }

    @Test
    fun `explicit display objects are not replaced by prior action results`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("नवन्", assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).value)
    }
    @Test
    fun `explicit prefixed lyap renders through derivation rather than raw source`() {
        val source = "द्वि + औट् एक + अम् च वि + युज् + णिच् + ल्यप् " +
            "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val readable = PvmUktiSadhaka().sadhayaLine(source)
        assertTrue("वियोज्य" in readable, readable)
        assertFalse("ल्यप्" in readable, readable)
    }
    @Test
    fun `binding projection rebuilds prior morphology from typed fields`() {
        val source = "एक + अम् द्वि + औट् च वि + युज् + णिच् + ल्यप् " +
            "फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val invocation = assertIs<Invocation>(PaniniParser().parse(source).body)
        val sentence = assertIs<AkhyataVakya>(invocation.vakya)
        val padas = sentence.padas.map { pada ->
            if (pada is AvyayaPada && pada.derivation is AvyayaKridantaDerivation) {
                val derivation = pada.derivation as AvyayaKridantaDerivation
                pada.copy(derivation = derivation.copy(dhatu = derivation.dhatu.copy(sourceText = "display only")))
            } else pada
        }
        val lowered = assertNotNull(PriorActionLowering.expand(invocation.copy(vakya = sentence.copy(padas = padas))))
        val prior = assertIs<AkhyataVakya>(assertIs<Invocation>(lowered.statements.first()).vakya)
        assertEquals("वि + युज् + णिच् + लोट् + सिप्", prior.tinganta.sourceText)
    }

    @Test
    fun `shared agent identity retains nominal derivational morphology`() {
        for (derived in listOf("गुण + मतुप्", "राम + टाप्")) {
            val base = derived.substringBefore(" + ")
            val source = "$base + सुँ एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
                "$derived + सुँ फल + अम् मुद्र् + णिच् + लट् + तिप् ।"
            val parsed = PaniniParser().parse(source)
            assertFailsWith<IllegalArgumentException>(source) {
                PriorActionLowering.lower(parsed.body)
            }
            val sharedDerived = "$derived + सुँ एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
                "$derived + सुँ फल + अम् मुद्र् + णिच् + लट् + तिप् ।"
            assertIs<Sequence>(PriorActionLowering.lower(PaniniParser().parse(sharedDerived).body))
        }
    }

    @Test
    fun `shared coordinated agents control only the finite main verb agreement`() {
        val analyzer = VakyaAnalyzer(PadaAnalyzer(InMemoryVyakaranamLexicon(
            emptyList(), listOf(YujirDhatu(), MudrDhatu()),
        )))
        for ((ending, expected) in listOf("तस्" to 0, "तिप्" to 1)) {
            val source = "राम + सुँ च श्याम + सुँ च एक + अम् द्वि + औट् च " +
                "युज् + णिच् + क्त्वा फल + अम् मुद्र् + णिच् + लट् + $ending ।"
            val sequence = assertIs<Sequence>(PriorActionLowering.lower(PaniniParser().parse(source).body))
            val frames = sequence.statements.map { analyzer.analyze(assertIs<Invocation>(it).vakya) }
            assertEquals(0, frames.first().diagnostics.count {
                it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH
            }, "Nonfinite morphology does not express finite number agreement")
            assertEquals(expected, frames.last().diagnostics.count {
                it.code == FrameDiagnosticCode.AGREEMENT_MISMATCH
            }, source)
        }
    }

    @Test
    fun `shared agent checking does not erase repeated nominal members`() {
        val source = "राम + सुँ च राम + सुँ च एक + अम् द्वि + औट् च " +
            "युज् + णिच् + क्त्वा राम + सुँ फल + अम् मुद्र् + णिच् + लट् + तिप् ।"
        assertFailsWith<IllegalArgumentException> {
            PriorActionLowering.lower(PaniniParser().parse(source).body)
        }
    }

    @Test
    fun `understood coordinated agents retain their group in either clause`() {
        val agents = "राम + सुँ च श्याम + सुँ च"
        val prior = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा"
        val main = "फल + अम् मुद्र् + णिच् + लट् + तस् ।"
        for (source in listOf("$agents $prior $main", "$prior $agents $main")) {
            val sequence = assertIs<Sequence>(PriorActionLowering.lower(PaniniParser().parse(source).body))
            for (statement in sequence.statements) {
                val sentence = assertIs<AkhyataVakya>(assertIs<Invocation>(statement).vakya)
                val group = sentence.padas.filterIsInstance<SamuccitaSubanta>().single()
                assertEquals(listOf("राम", "श्याम"), group.members.map { it.pratipadika.semanticKey() })
            }
        }
    }

    @Test
    fun `failed prior action does not invoke the reusable main command`() {
        val source = """
            प्रदर्शन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            नवन् + शस् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च वि + युज् + णिच् + ल्यप्
            प्रदर्शन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertEquals(1, results.size, results.toString())
        assertIs<ExecutionResult.Failure>(results.single())
    }

    @Test
    fun `prior action executes before a reusable procedure main command`() {
        val source = """
            प्रदर्शन + सुँ इति प्रक्रिया + सुँ असँ + लट् + तिप् ।
            फल + अम् मुद्र् + णिच् + लोट् + सिप् ॥
            एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा
            प्रदर्शन + टा डुकृञ् + उ + लोट् + सिप् ।
        """.trimIndent()
        val results = PaniniVM().evalScript(source)
        assertTrue(results.all { it is ExecutionResult.Success }, results.toString())
        assertEquals("त्रीणि", (results.last() as ExecutionResult.Success).value)
    }

    @Test
    fun `addition and print example executes and renders naturally`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        assertEquals("त्रीणि", assertIs<ExecutionResult.Success>(PaniniVM().evalScript(source).last()).value)
        val readable = PvmUktiSadhaka().sadhayaLine(source)
        assertTrue("योजयित्वा" in readable, readable)
        assertTrue("मुद्रय" in readable, readable)
        assertFalse("ततः" in readable, readable)
    }

    @Test
    fun `multiple prior actions use explicit results while independent operands stay independent`() {
        val add = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा"
        for ((continuation, expected) in listOf(
            "फल + अम् त्रि + शस् च युज् + णिच् + क्त्वा फल + अम्" to 6L,
            "त्रि + शस् चतुर् + शस् च युज् + णिच् + क्त्वा फल + अम्" to 7L,
        )) {
            val result = PaniniVM().evalScript("$add $continuation परिणाम + ङि स्था + णिच् + लोट् + सिप् ।").last()
            assertEquals(expected, assertIs<SanskritValue.Sankhya>(assertIs<ExecutionResult.Success>(result).typedValue).value)
        }
    }

    @Test
    fun `ktva and lyap execute before display while tatah still works`() {
        for ((addition, expected) in listOf("युज् + णिच् + क्त्वा" to 3L,
            "वि + युज् + णिच् + ल्यप्" to 1L,
            "युज् + णिच् + लोट् + सिप् ततः" to 3L)) {
            val source = "द्वि + औट् एक + अम् च $addition फल + अम् परिणाम + ङि स्था + णिच् + लोट् + सिप् " +
                "।\nफल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
            val results = PaniniVM().evalScript(source)
            assertTrue(results.none { it is ExecutionResult.Failure }, "$source\n$results")
            assertIs<ExecutionResult.Success>(results.last())
            assertEquals(expected, results.filterIsInstance<ExecutionResult.Success>()
                .mapNotNull { it.typedValue as? SanskritValue.Sankhya }.last().value, source)
        }
    }

    @Test
    fun `typed lowering partitions operands and does not request implicit piping`() {
        val source = "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा फल + अम् मुद्र् + णिच् + लोट् + सिप् ।"
        val parsed = PaniniParser().parse(source)
        val sequence = assertIs<Sequence>(PriorActionLowering.lower(parsed.body))
        assertEquals(listOf(SequenceConnector.PURVAKALA), sequence.connectorKinds)
        val prior = assertIs<AkhyataVakya>(assertIs<Invocation>(sequence.statements[0]).vakya)
        assertEquals("क्त्वा", prior.tinganta.priorAction?.pratyaya)
        assertEquals("योजयित्वा", PvmUktiSadhaka().sadhayaTinganta(prior.tinganta))
        assertEquals(4, prior.padas.size) // Two operands, च, and the binding head.
        val target = assertIs<Invocation>(sequence.statements[1])
        assertTrue(NaturalSemanticNormalizer.isPriorResult(target.vakya.padas.first() as SubantaPada))
    }

    @Test
    fun `different explicit agents are rejected`() {
        val results = PaniniVM().evalScript(
            "राम + सुँ एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा " +
                "श्याम + सुँ फल + अम् मुद्र् + णिच् + लट् + तिप् ।",
        )
        assertTrue(results.any { it is ExecutionResult.Failure }, results.toString())
    }

    @Test
    fun `unprefixed lyap and unsupported passive main clauses fail explicitly`() {
        for (source in listOf(
            "एक + अम् द्वि + औट् च युज् + णिच् + ल्यप् फल + अम् मुद्र् + णिच् + लोट् + सिप् ।",
            "एक + अम् द्वि + औट् च युज् + णिच् + क्त्वा फल + सुँ मुद्र् + णिच् + यक् + लट् + त ।",
        )) {
            assertTrue(PaniniVM().evalScript(source).any { it is ExecutionResult.Failure }, source)
        }
    }
}
