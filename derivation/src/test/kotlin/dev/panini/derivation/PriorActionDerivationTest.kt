package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class PriorActionDerivationTest {
    private val engine = KrdantaEngine()

    @Test
    fun `unimplemented light syllable nic replacement stays unresolved`() {
        val stem = assertIs<KrdantaSourceStem.Unresolved>(engine.deriveSourceStem(
            "नम्", "ल्यप्", listOf("णिच्"), listOf("प्र"),
        ))
        assertEquals(KrdantaSourceStem.Unresolved.Reason.INCOMPLETE_DERIVATION, stem.reason)
    }

    @Test
    fun `explicit lyap uses the contextual ktva replacement derivation`() {
        val lyap = assertIs<KrdantaSourceStem.Productive>(engine.deriveSourceStem(
            "युज्", "ल्यप्", listOf("णिच्"), listOf("वि"),
        ))
        val ktva = assertIs<KrdantaSourceStem.Productive>(engine.deriveSourceStem(
            "युज्", "क्त्वा", listOf("णिच्"), listOf("वि"),
        ))
        assertEquals("वियोज्य", lyap.surface)
        assertEquals(ktva.surface, lyap.surface)
    }

    @Test
    fun `lyap without its contextual prefix stays unresolved`() {
        val stem = assertIs<KrdantaSourceStem.Unresolved>(engine.deriveSourceStem("युज्", "ल्यप्"))
        assertEquals(KrdantaSourceStem.Unresolved.Reason.INVALID_AFFIX_CONTEXT, stem.reason)
    }
}
