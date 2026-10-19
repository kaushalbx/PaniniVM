package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EtattadohSulopoKoAnanjparoHaliSutra
import dev.panini.core.Linga
import dev.panini.core.Vibhakti
import dev.panini.core.Vacana
import dev.panini.shiksha.*
import kotlin.test.*

class PronounSuLopaVarnaTest {
    private val rule = EtattadohSulopoKoAnanjparoHaliSutra
    private fun state(left: String, right: String) = DerivationState(listOf(
        DerivationTerm("left", left, TermKind.PRATIPADIKA),
        DerivationTerm("right", right, TermKind.PRATIPADIKA)))

    @Test fun `su deletion requires a forward derived pronoun and preserves identity and exact trace`() {
        for (upadesha in listOf("तद्", "एतद्")) {
            val derivation = SubantaEngine().derive(SubantaDerivationRequest(
                upadesha, Vibhakti.PRATHAMA, Vacana.EKAVACANA, Linga.PUMS))
            val engine = SandhiEngine()
            val pada = engine.padaFromDerivation(derivation, "left")
            val right = SandhiEngine.Pada(DerivationTerm("right", "ददाति", TermKind.PRATIPADIKA))
            val result = engine.join(pada, right)
            val application = result.applications.single { it.sutra == rule.sutra }
            val term = application.before.terms.first()
            val left = application.after.terms.first()
            assertEquals(term.varnas.dropLast(1), left.varnas)
            assertEquals(term.phonologicalText.effectiveVarnas.dropLast(1), left.phonologicalText.effectiveVarnas)
            assertEquals(term.phonologicalText.sourceOrthographicSigns, left.orthographicSigns)
            assertEquals(term.id, left.id)
            assertEquals(term.upadesha, left.upadesha)
            assertEquals(application.before.terms.last(), application.after.terms.last())
            assertEquals(term.varnas.lastIndex, application.after.substitutions.last().sourceVarnaIndex)
            assertEquals(rule.sutra, application.after.substitutions.last().sutra)
            assertFalse(rule.matches(application.after))
        }
    }

    @Test fun `vowels other pronouns kaka and negative compound spellings are excluded`() {
        for ((left, right) in listOf("सः" to "अत्र", "एषः" to "अत्र", "यः" to "ददाति",
            "सकः" to "ददाति", "एषकः" to "ददाति", "असः" to "ददाति", "अनेषः" to "ददाति")) {
            assertFalse(rule.matches(state(left, right)), "$left + $right")
        }
        assertFalse(rule.matches(state("सः", "ददाति")), "surface spelling is not derivational provenance")
        assertFalse(rule.matches(state("एषः", "ददाति")), "surface spelling is not derivational provenance")
    }
}
