package dev.panini.execution

import dev.panini.vyakaranam.ast.MulaPratipadika
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertTrue

class TechnicalSamjnaIdentityTest {

    @Test
    fun `classifies canonical technical identities`() {
        listOf("सङ्ख्या", "गुण", "वृद्धि", "लोप", "साधकतमम्", "कर्म", "करणम्").forEach {
            assertFalse(SvamRupamEngine.isSelfReferentialLiteral(it))
        }
    }

    @Test
    fun `leaves ordinary lexical identity self referential`() {
        assertTrue(SvamRupamEngine.isSelfReferentialLiteral("राम"))
    }

    @Test
    fun `parsed nominal supplies case independent identity`() {
        val pratipadika = MulaPratipadika(
            sourceText = "राम + अम्",
            text = "राम",
        )

        val value = assertIs<SanskritValue.Shabda>(SvamRupamEngine.evaluate(pratipadika))

        assertEquals("राम", value.text)
    }

    @Test
    fun `raw lexical identity is never mistaken for serialized case morphology`() {
        val value = assertIs<SanskritValue.Shabda>(SvamRupamEngine.evaluateTerm("गणित + अम्"))

        assertEquals("गणित + अम्", value.text)
    }
}
