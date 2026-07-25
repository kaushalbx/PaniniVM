package dev.panini.ashtadhyayi.adhyaya3

import dev.panini.ashtadhyayi.adhyaya3.pada1.ChinKarmaniChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.NishriDruSruBhyahKarthariChaSutra
import dev.panini.ashtadhyayi.adhyaya3.pada1.PusAdiDyutAdyLdtahParasmaipadesuSutra
import dev.panini.core.Lakara
import dev.panini.core.Prayoga
import dev.panini.derivation.DerivationEngine
import dev.panini.derivation.DerivationalContext
import dev.panini.derivation.DerivationState
import dev.panini.derivation.DerivationTerm
import dev.panini.derivation.Rupa
import dev.panini.derivation.TermKind
import kotlin.test.Test
import kotlin.test.assertTrue

class LungAoristSutrasTest {

    @Test
    fun `derives caṅ vikaraṇa in Luṅ via NishriDruSruBhyahKarthariChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "निश्री", TermKind.DHATU, upadesha = "निश्री")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUNG))
        )
        val result = DerivationEngine(listOf(NishriDruSruBhyahKarthariChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.48" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "चङ्" })
    }

    @Test
    fun `derives aṅ vikaraṇa in Luṅ Parasmaipada via PusAdiDyutAdyLdtahParasmaipadesuSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "पुष्", TermKind.DHATU, upadesha = "पुष्")),
            context = DerivationalContext(rupa = Rupa(lakara = Lakara.LUNG))
        )
        val result = DerivationEngine(listOf(PusAdiDyutAdyLdtahParasmaipadesuSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.55" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "अङ्" })
    }

    @Test
    fun `derives ciṇ vikaraṇa in Luṅ karmaṇi via ChinKarmaniChaSutra`() {
        val state = DerivationState(
            terms = listOf(DerivationTerm("root", "कृ", TermKind.DHATU, upadesha = "कृ")),
            context = DerivationalContext(rupa = Rupa(prayoga = Prayoga.KARMANI, lakara = Lakara.LUNG))
        )
        val result = DerivationEngine(listOf(ChinKarmaniChaSutra)).derive(state)
        assertTrue(result.applications.any { it.sutra == "3.1.60" })
        assertTrue(result.final.allEffectiveTerms.any { it.upadesha == "चिण्" })
    }
}
