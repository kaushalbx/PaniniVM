package dev.panini.execution.binding

import dev.panini.core.Karaka
import dev.panini.core.SupAffix
import dev.panini.execution.ExecutionBindingResult
import dev.panini.execution.ExecutionExpression
import dev.panini.execution.SambhashanaContext
import dev.panini.execution.SanskritUktiInput
import dev.panini.execution.SanskritValue
import dev.panini.execution.ValueEnvironment
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class TypedOperandBindingTest {
    @Test
    fun `derived nouns use morphology retaining reference keys`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        for (stem in listOf("मान", "युज् + ल्युट्")) {
            val plain = parser.parse("$stem + अम् मुद्र् + णिच् + लोट् + सिप् ।")
                .grammaticalVakyas().single().padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
            val value = SanskritValue.Sankhya(6, "षट्")
            for (suffix in listOf("मतुप्", "तरप्", "टाप्")) {
                val source = "$stem + $suffix + अम् मुद्र् + णिच् + लोट् + सिप् ।"
                val derived = parser.parse(source).grammaticalVakyas().single().padas
                    .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
                kotlin.test.assertNotEquals(plain.pratipadika.referenceKey(), derived.pratipadika.referenceKey())
                fun bind(environment: ValueEnvironment) = assertIs<ExecutionBindingResult.Bound>(
                    VyakaranamExecutionAdapter.bind(SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", source),
                        SambhashanaContext("प्रयोक्ता", "यन्त्रम्"), environment = environment))
                    .ukti.invocations.single().bindings.getValue(Karaka.KARMAN)
                kotlin.test.assertFalse(bind(ValueEnvironment(mapOf(plain.pratipadika.referenceKey() to value)))
                    is ExecutionExpression.TypedOperand)
                assertEquals(value, assertIs<ExecutionExpression.TypedOperand>(bind(
                    ValueEnvironment(mapOf(derived.pratipadika.referenceKey() to value)))).value)
            }
        }
    }

    @Test
    fun `coordination cannot union different member roles into one operand`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val members = listOf("मूल्य + अम् ।", "राम + ङे ।").map { source ->
            parser.parse(source).grammaticalVakyas().single().padas
                .filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        }
        val vakya = parser.parse("मूल्य + अम् दा + लोट् + सिप् ।").grammaticalVakyas().single()
        val frame = dev.panini.analysis.KriyaFrame(dev.panini.analysis.KriyaId("mixed"),
            vakya, null, dev.panini.core.Prayoga.KARTARI, emptyList(), emptyList())
        val ctx = BindingContext(null, 0, dev.panini.dhatupatha.juhotyadi.DaDhatu(), frame,
            previousDhatus = emptyList())
        val coordinated = dev.panini.vyakaranam.ast.SamuccitaSubanta("", members)
        val result = KarakaExtractor.extractKarakas(listOf(coordinated), ctx)
        assertEquals(setOf(Karaka.KARMAN, Karaka.SAMPRADANA), result.bindings.keys)
        assertEquals(emptyList(), result.ambiguous)
        result.bindings.values.forEach { kotlin.test.assertFalse(it is ExecutionExpression.Coordination) }
    }

    @Test
    fun `unresolved shared endings preserve ambiguity across voice and case`() {
        val parser = dev.panini.vyakaranam.parser.PaniniParser()
        val lexicalDhatu = dev.panini.dhatupatha.DhatuPatha.all.first { it.operations.isEmpty() }
        for (prayoga in listOf(dev.panini.core.Prayoga.KARTARI, dev.panini.core.Prayoga.KARMANI)) {
            for ((ending, expected) in listOf(
                "भ्याम्" to setOf(
                    if (prayoga == dev.panini.core.Prayoga.KARTARI) Karaka.KARANA else Karaka.KARTR,
                    Karaka.SAMPRADANA, Karaka.APADANA,
                ),
                "भ्यस्" to setOf(Karaka.SAMPRADANA, Karaka.APADANA),
                "ओस्" to setOf(Karaka.SAMBANDHA, Karaka.ADHIKARANA),
            )) {
                val vakya = parser.parse("राम + $ending दा + लोट् + सिप् ।").grammaticalVakyas().single()
                val frame = dev.panini.analysis.KriyaFrame(dev.panini.analysis.KriyaId("ambiguous"),
                    vakya, null, prayoga, emptyList(), emptyList())
                val ctx = BindingContext(null, 0, lexicalDhatu, frame, previousDhatus = emptyList())
                val result = KarakaExtractor.extractKarakas(vakya.padas, ctx)
                assertEquals(emptyMap(), result.bindings, "$prayoga $ending")
                assertEquals(expected, result.ambiguous.single().candidates, "$prayoga $ending")
            }
        }
    }

    @Test
    fun `resolved grammatical role is not reassigned to fit an operation`() {
        val vakya = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("राम + भ्याम् दा + लोट् + सिप् ।").grammaticalVakyas().single()
        val noun = vakya.padas.filterIsInstance<dev.panini.vyakaranam.ast.SubantaPada>().single()
        val id = dev.panini.analysis.KriyaId("resolved")
        val relation = dev.panini.analysis.KarakaRelation(id,
            dev.panini.analysis.SubantaAnalysis(noun, null, SupAffix.candidates(noun.sup.text), emptySet()),
            dev.panini.analysis.FrameKarakaResolution.Resolved(Karaka.KARANA))
        val frame = dev.panini.analysis.KriyaFrame(id, vakya, null,
            dev.panini.core.Prayoga.KARTARI, listOf(relation), emptyList())
        val ctx = BindingContext(null, 0, dev.panini.dhatupatha.juhotyadi.DaDhatu(), frame,
            previousDhatus = emptyList())
        val result = KarakaExtractor.extractKarakas(vakya.padas, ctx)
        kotlin.test.assertTrue(Karaka.KARANA in result.bindings)
        kotlin.test.assertFalse(Karaka.SAMPRADANA in result.bindings)
    }

    @Test
    fun `missing frame relation retains all shared ending candidates before role selection`() {
        val vakya = dev.panini.vyakaranam.parser.PaniniParser()
            .parse("राम + भ्याम् दा + लोट् + सिप् ।").grammaticalVakyas().single()
        val frame = dev.panini.analysis.KriyaFrame(
            dev.panini.analysis.KriyaId("fallback"), vakya, null,
            dev.panini.core.Prayoga.KARTARI, emptyList(), emptyList(),
        )
        val ctx = BindingContext(null, 0, dev.panini.dhatupatha.juhotyadi.DaDhatu(), frame,
            previousDhatus = emptyList())
        val result = KarakaExtractor.extractKarakas(vakya.padas, ctx)
        kotlin.test.assertTrue(Karaka.SAMPRADANA in result.bindings)
        kotlin.test.assertFalse(Karaka.KARANA in result.bindings)
        assertEquals(emptyList(), result.ambiguous)
    }

    @Test
    fun `coordinated shared dual ending resolves recipient instead of first case`() {
        val binding = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(
            SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "मूल्य + अम् राम + भ्याम् श्याम + भ्याम् च दा + लोट् + सिप् ।"),
            SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
        ))
        val bindings = binding.ukti.invocations.single().bindings
        assertIs<ExecutionExpression.Coordination>(bindings.getValue(Karaka.SAMPRADANA))
        kotlin.test.assertFalse(Karaka.KARANA in bindings)
    }

    @Test
    fun `scope value binds as a first class typed operand`() {
        val value = SanskritValue.Sankhya(6, "षट्")
        val binding = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(
                SanskritUktiInput("प्रयोक्ता", "यन्त्रम्", "विशेषणफल + अम् मुद्र् + लोट् + सिप् ।"),
                SambhashanaContext("प्रयोक्ता", "यन्त्रम्"),
                environment = ValueEnvironment(mapOf("विशेषणफल" to value)),
            ),
        )

        val operand = assertIs<ExecutionExpression.TypedOperand>(
            binding.ukti.invocations.single().bindings.getValue(Karaka.KARMAN),
        )
        assertEquals(value, operand.value)
        assertEquals(SupAffix.AM, operand.sup)
    }
}
