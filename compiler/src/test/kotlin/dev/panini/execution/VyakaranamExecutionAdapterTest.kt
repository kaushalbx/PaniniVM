package dev.panini.execution

import dev.panini.core.Karaka
import dev.panini.execution.binding.VyakaranamExecutionAdapter
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class VyakaranamExecutionAdapterTest {
    private val conversation = SambhashanaContext("प्रयोक्ता", "यन्त्रम्")

    @Test
    fun `ANTLR4 parses segmented single clause Sanskrit utterance`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "हे यन्त्र + सुँ, एक + अम् द्वि + औट् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।",
        )
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(input, conversation),
        )
        assertEquals(1, bound.ukti.invocations.size)
        assertEquals("07.0007", bound.ukti.invocations[0].dhatu.id)
        assertEquals(null, bound.ukti.invocations[0].selectedOperation)
        assertEquals(setOf("णिच्"), bound.ukti.invocations[0].grammaticalFeatures.sanadi)
    }

    @Test
    fun `ANTLR4 parses segmented multi-clause utterance with tatah`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् ततः फल + औट् द्वि + औट् युज् + णिच् + लोट् + सिप् ।",
        )
        val bound = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(input, conversation))
        assertEquals(listOf("07.0007", "07.0007"), bound.ukti.invocations.map { it.dhatu.id })
    }

    @Test
    fun `syncretic bhyam is not collapsed to trtiya without matching verbal semantics`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एक + भ्याम् द्वि + भ्याम् च युज् + णिच् + लोट् + सिप् ।",
        )
        val bound = assertIs<ExecutionBindingResult.Bound>(
            VyakaranamExecutionAdapter.bind(input, conversation),
        )

        val invocation = bound.ukti.invocations.single()
        assertEquals(setOf(Karaka.KARTR), invocation.bindings.keys)
        assertEquals(2, invocation.ambiguousBindings.size)
        invocation.ambiguousBindings.forEach {
            assertEquals(
                setOf(Karaka.KARANA, Karaka.SAMPRADANA, Karaka.APADANA),
                it.candidates,
            )
        }
    }

    @Test
    fun `ANTLR4 parses 3-clause chained utterance separated by danda`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "एक + अम् द्वि + औट् च युज् + णिच् + लोट् + सिप् । फल + अम् द्वि + औट् च गण + णिच् + लोट् + सिप् । फल + अम् त्रि + शस् च युज् + णिच् + लोट् + सिप् ।",
        )
        val bound = assertIs<ExecutionBindingResult.Bound>(VyakaranamExecutionAdapter.bind(input, conversation))
        assertEquals(listOf("07.0007", "10.0391", "07.0007"), bound.ukti.invocations.map { it.dhatu.id })
        assertEquals(2, bound.ukti.dependencies.size)
    }

    @Test
    fun `ANTLR4 parses nominal-only sentence`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "राम + सुँ लक्ष्मण + सुँ च ।",
        )
        assertIs<ExecutionBindingResult.Invalid>(VyakaranamExecutionAdapter.bind(input, conversation))
    }

    @Test
    fun `ANTLR4 parses complex compound members`() {
        // gachat (kridanta) - putra (simple)
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "गम् + शतृ-पुत्र + सुँ खद् + लट् + तिप् ।",
        )
        assertIs<ExecutionBindingResult.Invalid>(VyakaranamExecutionAdapter.bind(input, conversation))
    }

    @Test
    fun `ANTLR4 parses stri pratyaya derivations`() {
        val input = SanskritUktiInput(
            speaker = "प्रयोक्ता",
            listener = "यन्त्रम्",
            text = "अश्व + टाप् + सुँ ।",
        )
        assertIs<ExecutionBindingResult.Invalid>(VyakaranamExecutionAdapter.bind(input, conversation))
    }
}
