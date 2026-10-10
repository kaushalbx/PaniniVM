package dev.panini.derivation

import dev.panini.ashtadhyayi.adhyaya6.pada1.IkoYanAciSutra
import dev.panini.ashtadhyayi.adhyaya6.pada1.SavarnaDirghaSutra
import dev.panini.core.Lakara
import dev.panini.core.TingAffix
import kotlin.test.*

class VowelSandhiAffixIdentityTest {
    @Test fun `present ting guard requires affix identity rather than spelling alone`() {
        for (kind in listOf(TermKind.PRATYAYA, TermKind.PRATIPADIKA)) {
            val state = DerivationState(listOf(
                DerivationTerm("left", "इ", TermKind.PRATIPADIKA),
                DerivationTerm("right", "अ", kind, upadesha = TingAffix.JHI.upadesha),
            ), stage = DerivationStage.PADA_FORMED,
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LAT)))
            assertEquals(kind != TermKind.PRATYAYA, IkoYanAciSutra.matches(state))
        }
    }

    @Test fun `lot jhi guard does not treat an unrelated pada as an affix`() {
        for (kind in listOf(TermKind.PRATYAYA, TermKind.PRATIPADIKA)) {
            val state = DerivationState(listOf(
                DerivationTerm("left", "अ", TermKind.PRATIPADIKA),
                DerivationTerm("right", "अ", kind, upadesha = TingAffix.JHI.upadesha),
            ), stage = DerivationStage.PADA_FORMED,
                context = DerivationalContext(rupa = Rupa(lakara = Lakara.LOT)))
            assertEquals(kind != TermKind.PRATYAYA, SavarnaDirghaSutra.matches(state))
        }
    }
}
