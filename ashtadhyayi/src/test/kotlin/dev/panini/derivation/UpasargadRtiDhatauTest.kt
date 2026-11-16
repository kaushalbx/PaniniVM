package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.EngiPararupamSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.UpasargadRtiDhatauSutra
import dev.panini.shiksha.Samjna
import kotlin.test.*

class UpasargadRtiDhatauTest {
    private fun state(prefix: String, root: String, kind: TermKind = TermKind.DHATU,
                      environments: Set<DerivationalEnvironment> = emptySet()) = DerivationState(listOf(
        DerivationTerm("prefix", prefix, TermKind.PRATIPADIKA, upadesha = prefix),
        DerivationTerm("root", root, kind, upadesha = root),
    ), stage = DerivationStage.ANGAKARYA, samjnas = setOf(SamjnaAssignment("prefix", Samjna.UPASARGA)),
        context = DerivationalContext(environments = environments))

    @Test fun `6_1_91 gives ar for upasarga a before r of the dhatu`() {
        assertEquals("प्रार्च्छ्", UpasargadRtiDhatauSutra.apply(state("प्र", "ऋच्छ्")).state.surface)
        assertEquals("उपार्ष्", UpasargadRtiDhatauSutra.apply(state("उप", "ऋष्")).state.surface)
    }

    @Test fun `6_1_91 needs the dhatu or kriyayoga`() {
        assertFalse(UpasargadRtiDhatauSutra.matches(state("प्र", "ऋषभ", TermKind.PRATIPADIKA)))
        val completed = state("प्र", "ऋच्छति", TermKind.PRATIPADIKA, setOf(DerivationalEnvironment.KRIYAYOGA))
        assertEquals("प्रार्च्छति", UpasargadRtiDhatauSutra.apply(completed).state.surface)
        assertFalse(EngiPararupamSutra.matches(state("प्र", "एजते", TermKind.PRATIPADIKA)))
    }

    @Test fun `6_1_91 leaves a non a-final upasarga alone`() {
        assertFalse(UpasargadRtiDhatauSutra.matches(state("नि", "ऋच्छ्")))
    }
}
