package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya7.pada1.SamaseAnanpurveKtvoLyapSutra
import dev.panini.core.KrtAffix
import dev.panini.shiksha.Samjna
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KtvaLyapIdentityTest {
    private fun state() = DerivationState(
        terms = listOf(
            DerivationTerm("prefix-renamed", "प्र", TermKind.PRATIPADIKA),
            DerivationTerm("root", "कृ", TermKind.DHATU),
            KrtAffix.KTVA.term("suffix-renamed").copy(surface = "त्वा"),
        ),
        samjnas = setOf(SamjnaAssignment("prefix-renamed", Samjna.UPASARGA)),
    )

    @Test
    fun `renamed typed ktva is replaced once with fresh lyap`() {
        val original = state()
        assertTrue(SamaseAnanpurveKtvoLyapSutra.matches(original))
        val result = SamaseAnanpurveKtvoLyapSutra.apply(original).state
        val ending = result.terms.last()
        assertTrue(ending.hasCurrentAffix(KrtAffix.LYAP))
        assertFalse(ending.hasCurrentAffix(KrtAffix.KTVA))
        assertTrue(ending.matchesAffix(KrtAffix.KTVA))
        assertEquals(ItProcessingPhase.RAW_UPADESHA, ending.itProcessingPhase)
        assertEquals(original.terms.dropLast(1), result.terms.dropLast(1))
        assertFalse(SamaseAnanpurveKtvoLyapSutra.matches(result))
    }

    @Test
    fun `identifier surface and stale samjna cannot manufacture applicability`() {
        val original = state()
        assertFalse(SamaseAnanpurveKtvoLyapSutra.matches(original.copy(samjnas = emptySet())))
        assertFalse(SamaseAnanpurveKtvoLyapSutra.matches(original.copy(
            samjnas = setOf(SamjnaAssignment("missing", Samjna.UPASARGA)),
        )))
        for (fake in listOf(
            DerivationTerm("ktva_pratyaya", "त्वा", TermKind.PRATYAYA),
            DerivationTerm("fake-root", "क्त्वा", TermKind.DHATU),
        )) {
            assertFalse(SamaseAnanpurveKtvoLyapSutra.matches(original.copy(terms = original.terms.dropLast(1) + fake)))
        }
        assertFalse(SamaseAnanpurveKtvoLyapSutra.matches(original.copy(
            terms = original.terms + DerivationTerm("later", "अ", TermKind.PRATIPADIKA),
        )))
    }
}
