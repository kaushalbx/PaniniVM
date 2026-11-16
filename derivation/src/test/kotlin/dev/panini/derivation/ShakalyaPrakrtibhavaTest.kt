package dev.panini.derivation

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ShakalyaPrakrtibhavaTest {
    private val engine = SandhiEngine()
    private fun default(left: String, right: String) = engine.render(engine.join(left, right))
    private fun all(left: String, right: String) = engine.joinAll(left, right).withoutIndependentOptions().map(engine::render).toSet()

    @Test fun `6_1_127 keeps a final ik before a dissimilar vowel only as Shakalya's option`() {
        assertEquals("चक्र्यत्र", default("चक्रि", "अत्र"))
        assertEquals(setOf("चक्र्यत्र", "चक्रि अत्र"), all("चक्रि", "अत्र"))
        // A long ik is shortened: कुमारी + अत्र → कुमारि अत्र.
        assertTrue("कुमारि अत्र" in all("कुमारी", "अत्र"))
        // A similar vowel is not asavarṇa: दधि + इह has only the savarṇa-dīrgha.
        assertEquals(setOf("दधीह"), all("दधि", "इह"))
    }

    @Test fun `6_1_128 keeps a final ak before r only as an option`() {
        assertEquals("ब्रह्मर्षिः", default("ब्रह्म", "ऋषिः"))
        assertTrue("ब्रह्म ऋषिः" in all("ब्रह्म", "ऋषिः"), all("ब्रह्म", "ऋषिः").toString())
        // A long ak is shortened: महा + ऋषिः → मह ऋषिः.
        assertTrue("मह ऋषिः" in all("महा", "ऋषिः"), all("महा", "ऋषिः").toString())
    }
}
