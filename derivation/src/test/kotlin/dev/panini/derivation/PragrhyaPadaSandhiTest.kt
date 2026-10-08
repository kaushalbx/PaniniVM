package dev.panini.derivation

import dev.panini.core.Vacana
import dev.panini.core.Vibhakti
import kotlin.test.*

class PragrhyaPadaSandhiTest {
    private val engine = SandhiEngine()
    private fun pada(id: String, surface: String, rupa: Rupa = Rupa()) =
        SandhiEngine.Pada(DerivationTerm(id, surface, TermKind.PRATIPADIKA), rupa)

    @Test fun `nominative and accusative dual male remain pragrhya`() {
        for (case in listOf(Vibhakti.PRATHAMA, Vibhakti.DVITIYA)) {
            val results = engine.joinAll(pada("mala", "माले", Rupa(vibhakti = case, vacana = Vacana.DVIVACANA)),
                pada("iti", "इति"))
            assertEquals(setOf("माले इति"), results.map(engine::render).toSet(), results.joinToString { it.applications.map { a -> a.sutra }.toString() })
            for (result in results) {
                assertTrue(result.applications.any { it.sutra == "1.1.11" })
                assertTrue(result.applications.any { it.sutra == "6.1.125" })
                assertFalse(result.applications.any { it.sutra == "6.1.78" })
                assertEquals(listOf("mala", "iti"), result.final.terms.map { it.id })
            }
        }
    }

    @Test fun `singular male has ay and optional short a hiatus`() {
        val results = engine.joinAll(pada("mala", "माले", Rupa(vacana = Vacana.EKAVACANA)), pada("iti", "इति"))
        assertEquals(setOf("मालयिति", "माल इति"), results.map(engine::render).toSet())
        assertTrue(results.all { it.applications.any { a -> a.sutra == "6.1.78" } })
        assertTrue(results.none { it.applications.any { a -> a.sutra == "1.1.11" } })
    }

    @Test fun `right dual does not make a left singular pragrhya`() {
        val result = engine.join(pada("left", "माले", Rupa(vacana = Vacana.EKAVACANA)),
            pada("right", "ईते", Rupa(vacana = Vacana.DVIVACANA)))
        assertFalse(result.initial.samjnas.any { it.targetId == "left" && it.samjna == dev.panini.shiksha.Samjna.PRAGRHYA })
        assertTrue(result.applications.any { it.sutra == "6.1.78" })
    }

    @Test fun `completed dual long i and u remain unchanged`() {
        for (word in listOf("हरी", "विष्णू")) {
            val result = engine.join(pada("left", word, Rupa(vacana = Vacana.DVIVACANA)), pada("right", "इति"))
            assertEquals("$word इति", engine.render(result))
        }
    }

    @Test fun `sakalyas lopa does not delete lexical y after i or before unvoiced consonants`() {
        for ((left, right) in listOf("अग्निय्" to "इति", "मालय्" to "च")) {
            val result = engine.join(pada("left", left), pada("right", right))
            assertFalse(result.applications.any { it.sutra == "8.3.19" })
        }
    }
}
